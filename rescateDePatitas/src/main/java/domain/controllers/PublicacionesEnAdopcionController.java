package domain.controllers;

import domain.models.entities.mascotas.*;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.Pregunta;
import domain.models.entities.publicaciones.PublicacionEnAdopcion;
import domain.models.entities.rol.Duenio;
import domain.models.repositories.*;
import domain.models.repositories.daos.DAOHibernate;
import domain.models.entities.publicaciones.*;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

public class PublicacionesEnAdopcionController {
    private RepositorioGenerico<PublicacionEnAdopcion> repo;
    private UsuarioController usuarioController = UsuarioController.getInstancia();
    private RolController rolController = RolController.getInstancia();


    public PublicacionesEnAdopcionController(){
        this.repo = FactoryRepositorio.get(PublicacionEnAdopcion.class);
    }

    public ModelAndView mostrarEnAdopcion(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        List<PublicacionEnAdopcion> enAdopcion = this.repo.buscarTodos();
        List<PublicacionEnAdopcion> aprobadas = new ArrayList<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        for (PublicacionEnAdopcion publicacion:enAdopcion) {
            if(publicacion.estaAprobada()){
                aprobadas.add(publicacion);
            }
        }

        parametros.put("adopciones", aprobadas);
        return new ModelAndView(parametros, "adopcion.hbs");
    }

    public ModelAndView mostrarPublicacionEnAdopcion(Request request, Response response) {
        PublicacionEnAdopcion publicacion = this.repo.buscar(new Integer(request.params("id")));
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        parametros.put("publicacion", publicacion);
        return new ModelAndView(parametros, "adopcion_publicacion.hbs");
    }

    public Response quieroAdoptarlo(Request request, Response response){
        int publicacionId = Integer.parseInt(request.params("id"));
        PublicacionEnAdopcion publicacion = this.repo.buscar(publicacionId);

        if (request.session().attribute("id") != null) {
            RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
            Persona persona = repoPersonas.dameLaPersona(request.session().attribute("id"));

            persona.quieroAdoptar(publicacion.getMascota());

        } else {
            String nombre = "";
            if (request.queryParams("nombre") != null) {
                nombre = request.queryParams("nombre");
            }

            String email = "";
            if (request.queryParams("email") != null) {
                email = request.queryParams("email");
            }

            String numero = "";
            if (request.queryParams("numero") != null) {
                numero = request.queryParams("numero");
            }

            Estrategia medioPreferido = Estrategia.valueOf("EMAIL");
            if (request.queryParams("medioPreferido") != null) {
                if (request.queryParams("medioPreferido").equals("Email")) {
                    medioPreferido = Estrategia.valueOf("EMAIL");
                } else {
                    if (request.queryParams("medioPreferido").equals("WhatsApp")) {
                        medioPreferido = Estrategia.valueOf("WHATSAPP");
                    } else medioPreferido = Estrategia.valueOf("SMS");
                }
            }

            Contacto contacto = new Contacto(nombre, "", numero, email, medioPreferido);
            List<Contacto> contactos = new ArrayList<>();
            contactos.add(contacto);
            Persona persona = new Persona();
            persona.setNombre(nombre);
            contacto.setPersona(persona);
            persona.setContactos(contactos);
            Mascota mascota = publicacion.getMascota();

            mascota.meQuiereAdoptar(persona);
        }

        response.redirect("/en_adopcion/ok");
        return response;
    }

    public ModelAndView mostrarOk(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();
        return new ModelAndView(parametros, "adopcion_publicacion_ok.hbs");
    }

    public void crearContacto(Persona persona, Request request){
        if(request.queryParams("nombre") != null){
            persona.setNombre(request.queryParams("nombre"));
        }
        Contacto contacto = new Contacto("","",
                request.queryParams("telefono"),
                request.queryParams("email"),
                Estrategia.EMAIL);
        persona.getContactos().add(contacto);
    }

    public ModelAndView mostrarCuestionarioAdopcion(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();

        List<TipoDeDocumento> tipo = new ArrayList<>();
        tipo.add(TipoDeDocumento.valueOf("DNI"));
        tipo.add(TipoDeDocumento.valueOf("LIBRETA_CIVICA"));
        tipo.add(TipoDeDocumento.valueOf("PASAPORTE"));
        tipo.add(TipoDeDocumento.valueOf("CEDULA"));
        tipo.add(TipoDeDocumento.valueOf("LIBRETA_ENROLAMIENTO"));

        List<String> provincias = new ArrayList<>();
        provincias.add("Buenos Aires");
        provincias.add("CABA");
        provincias.add("Córdoba");
        provincias.add("Santa Fe");

        List<Organizacion> organizaciones = new ArrayList<>();

        OrganizacionController cOrg = OrganizacionController.getInstancia();
        RepositorioGenerico<Organizacion> repoOrg = cOrg.getRepositorio();
        organizaciones = repoOrg.buscarTodos();

        parametros.put("organizaciones", organizaciones);

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        PreguntasController cPreguntas = PreguntasController.getInstancia();
        RepositorioDePreguntas repoPreguntas = cPreguntas.getRepositorio();
        List<Pregunta> generales = repoPreguntas.buscarPorTipo("general");
        List<Pregunta> pyc = repoPreguntas.buscarPorTipo("pyc");

        parametros.put("tipos", tipo);
        parametros.put("generales", generales);
        parametros.put("provincias", provincias);
        parametros.put("pyc", pyc);

        //Guardo la lista de preguntas en la sesion
        request.session().attribute("preguntasgenerales", generales);
        request.session().attribute("preguntaspyc", pyc);

        return new ModelAndView(parametros, "dar_adopcion.hbs");
    }

    public Response recibirDatosCuestionarioDarEnAdopcion(Request request, Response response) throws IOException {

        File uploadDir = new File("rescateDePatitas/src/main/resources/public/img/fotosmascotas");
        //uploadDir.mkdir();
        Path tempFile = Files.createTempFile(uploadDir.toPath(), request.queryParams("nombre"), ".jpg");

        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try (InputStream input = request.raw().getPart("foto").getInputStream()) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        Mascota mascota = new Mascota();
        asignarAtributosA(mascota, request);

        List<Foto> fotos = new ArrayList<>();
        Foto foto = new Foto();
        foto.setURLfoto(tempFile.toString().replace("rescateDePatitas\\src\\main\\resources\\public\\img\\fotosmascotas\\", "img/fotosmascotas/"));
        foto.setMascota(mascota);
        fotos.add(foto);
        mascota.setFotos(fotos);

        if (request.session().attribute("id") != null) {
            RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
            Persona duenio = repoPersonas.dameLaPersona(request.session().attribute("id"));
            mascota.setPersona(duenio);
            mascota.setDuenio(duenio.getDuenio());
            duenio.getDuenio().registrarMascota(mascota);
        } else {
            PersonaController cPersona = PersonaController.getInstancia();
            RepositorioDePersonas repoPersona = cPersona.getRepositorio();
            String cadena = request.queryParams("fnacPersona") + request.queryParams("nroDoc");
            String hashPersona = org.apache.commons.codec.digest.DigestUtils.md5Hex(cadena);
            Persona personaEncontrada = repoPersona.buscarPersona(hashPersona);

            if (personaEncontrada != null) {
                mascota.setPersona(personaEncontrada);
                mascota.setDuenio(personaEncontrada.getDuenio());
                personaEncontrada.getDuenio().registrarMascota(mascota);
                //request.session().attribute("personaformulario", personaEncontrada);
                //request.session().attribute("persistirpersonaformulario", false);
            } else {
                Persona persona = new Persona();
                asignarAtributosA(persona, request);
                persona.setUsuarioTemporal(hashPersona);
                mascota.setPersona(persona);
                mascota.setDuenio(persona.getDuenio());
                persona.getDuenio().registrarMascota(mascota);
                repoPersona.agregar(persona);
                //request.session().attribute("personaformulario", persona);
                //request.session().attribute("persistirpersonaformulario", true);
            }
        }

        //Guardo a la mascota en la base de datos
        MascotaController mascotaController = MascotaController.getInstancia();
        RepositorioGenerico<Mascota> repoMascotas = mascotaController.getRepositorio();
        //RepositorioDeMascotas repoMascotas = new RepositorioDeMascotas(new DAOHibernate<>(Mascota.class));
        repoMascotas.agregar(mascota);
        request.session().attribute("idmascotaenadopcion", mascota.getId());

        //Guardo en la sesion a la organizacion
        String nombreAsociacion = request.queryParams("asociacion");
        OrganizacionController cOrg = OrganizacionController.getInstancia();
        RepositorioDeOrganizaciones repoOrg = cOrg.getRepositorio();
        Organizacion organizacion = repoOrg.buscarPorNombre(nombreAsociacion);
        request.session().attribute("organizacionformulario", organizacion);

        //Creo un cuestionarioContestado con las respuestas de la mascota y lo guardo en la sesion
        CuestionarioContestado cuestionario = new CuestionarioContestado();
        ArrayList<RespuestaConcreta> respuestas = new ArrayList<>();
        List<Pregunta> generales = request.session().attribute("preguntasgenerales");
        int cantidad = generales.size();
        for(int i = 0; i < cantidad; i++){
            RespuestaConcreta respuesta = new RespuestaConcreta();

            respuesta.setRespuesta(request.queryParams("respuestaGeneral" + i));
            respuesta.setPregunta(generales.get(i));

            respuestas.add(respuesta);
        }
        cuestionario.setRespuestas(respuestas);

        List<Pregunta> pyc = request.session().attribute("preguntaspyc");
        ArrayList<RespuestaConcreta> respuestaspyc = new ArrayList<>();
        cantidad = pyc.size();
        for(int i=0; i < cantidad;i++){
            RespuestaConcreta respuesta = new RespuestaConcreta();
            respuesta.setRespuesta(request.queryParams("respuestapyc" + i));
            respuesta.setPregunta(pyc.get(i));

            respuestaspyc.add(respuesta);
        }
        cuestionario.getRespuestas().addAll(respuestaspyc);

        PublicacionEnAdopcion publicacionEnAdopcion = new PublicacionEnAdopcion();
        publicacionEnAdopcion.setTipoPublicacion("Mascota dada en adopcion");
        publicacionEnAdopcion.setCuestionario(cuestionario);
        publicacionEnAdopcion.setMascota(mascota);
        publicacionEnAdopcion.setEstadoDePublicacion(EstadoDePublicacion.SIN_REVISAR);
        repo.agregar(publicacionEnAdopcion);
        request.session().attribute("publiEnAdopcion", publicacionEnAdopcion);

        /*
        request.session().removeAttribute("preguntasgenerales");
        request.session().attribute("cuestionarioContestado", cuestionario);
        request.session().attribute("mascotaformulario", mascota);
        */
        response.redirect("/dar_adopcion_asociacion");

        return response;
    }

    public ModelAndView mostrarCuestionarioAsociacionAdopcion(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        Organizacion organizacion = request.session().attribute("organizacionformulario");

        parametros.put("organizacion",organizacion);
        parametros.put("preguntas", organizacion.getPreguntasAdopcion());

        return new ModelAndView(parametros, "dar_adopcion_asociacion.hbs");
    }

    public Response guardarPublicacion(Request request, Response response){
        //Recupero a la persona y a la mascota de la sesion
        //Persona persona = request.session().attribute("personaformulario");
        //Mascota mascota = request.session().attribute("mascotaformulario");

        //Guardo a la persona en la base de datos de ser necesario
        //Boolean persistirpersonaformulario = request.session().attribute("persistirpersonaformulario");
        /*if(persistirpersonaformulario){
            PersonaController cpersona = PersonaController.getInstancia();
            RepositorioDePersonas repoPersona = cpersona.getRepositorio();
            repoPersona.agregar(persona);
        }*/

        //Guardo a la mascota en la base de datos
        //MascotaController mascotaController = MascotaController.getInstancia();
        //RepositorioGenerico<Mascota> repoMascotas = mascotaController.getRepositorio();
        //RepositorioDeMascotas repoMascotas = new RepositorioDeMascotas(new DAOHibernate<>(Mascota.class));
        //repoMascotas.agregar(mascota);
        //Mascota mascota = repoMascotas.buscar(request.session().attribute("idmascotaenadopcion"));

        //Crear publicacion y guardarla
        //PublicacionEnAdopcion publicacion = new PublicacionEnAdopcion();
        PublicacionEnAdopcion publicacion = request.session().attribute("publiEnAdopcion");
        Organizacion organizacion = request.session().attribute("organizacionformulario");
        int numero = organizacion.getPreguntasAdopcion().size();
        //CuestionarioContestado cuestionario = request.session().attribute("cuestionarioContestado");

        ArrayList<RespuestaConcreta> respuestasOrg = new ArrayList<>();
        for(int i = 0; i < numero; i++){
            RespuestaConcreta respuesta = new RespuestaConcreta();

            respuesta.setRespuesta(request.queryParams("respuestaOrg" + i));
            respuesta.setPregunta(organizacion.getPreguntasAdopcion().get(i));

            respuestasOrg.add(respuesta);
        }
        CuestionarioContestado cuestionario = publicacion.getCuestionario();
        cuestionario.agregarRespuestas(respuestasOrg);
        for(RespuestaConcreta respuestaConcreta: cuestionario.getRespuestas()) {
            respuestaConcreta.setCuestionarioContestado(cuestionario);
            if(respuestaConcreta.getPregunta().getRespuestasConcretas() != null)
                respuestaConcreta.getPregunta().getRespuestasConcretas().add(respuestaConcreta);
            else{
                List<RespuestaConcreta> lista = new ArrayList<>();
                lista.add(respuestaConcreta);
                respuestaConcreta.getPregunta().setRespuestasConcretas(lista);
            }
        }

        //publicacion.setMascota(mascota);
        //publicacion.setTipoPublicacion("Mascota dada en adopcion");
        publicacion.setOrganizacion(organizacion);
        publicacion.setCuestionario(cuestionario);
        publicacion.setFecha(java.time.LocalDate.now());

        //organizacion.getPublicaciones().add(publicacion);
        //RepositorioDePublicaciones repoPublicaciones = new RepositorioDePublicaciones(new DAOHibernate<>(PublicacionEnAdopcion.class));
        repo.modificar(publicacion);

        response.redirect("/ok");

        return response;
    }

    private void asignarAtributosA(Mascota mascota, Request request) {
        if (request.queryParams("nombre") != null) {
            mascota.setNombre(request.queryParams("nombre"));
        }

        if (request.queryParams("apodo") != null) {
            mascota.setApodo(request.queryParams("apodo"));
        }

        if (request.queryParams("especie") != null) {
            mascota.setEspecie(request.queryParams("especie"));
        }

        if (request.queryParams("sexo") != null) {
            mascota.setGenero(request.queryParams("sexo"));
        }

        if (request.queryParams("tamanio") != null) {
            mascota.setTamanio(request.queryParams("tamanio"));
        }

        if (request.queryParams("descripcion") != null) {
            mascota.setDescripcion(request.queryParams("descripcion"));
        }

        if (request.queryParams("edad") != null) {
            int edad = Integer.parseInt(request.queryParams("edad"));
            mascota.setEdad(edad);
        }

       /* if (request.queryParams("foto") != null) {
            //TODO hacerlo para muchas fotos
            List<Foto> fotos = new ArrayList<>();
            Foto foto = new Foto();
            foto.setURLfoto(request.queryParams("foto"));
            foto.setMascota(mascota);
            fotos.add(foto);
            mascota.setFotos(fotos);
        }*/

        if (request.queryParams("asociacion") != null) {
            String nombreAsociacion = request.queryParams("asociacion");
            OrganizacionController cOrg = OrganizacionController.getInstancia();
            RepositorioDeOrganizaciones repoOrg = cOrg.getRepositorio();
            Organizacion organizacion = repoOrg.buscarPorNombre(nombreAsociacion);
            mascota.setOrganizacion(organizacion);

        }

        PreguntasController cPreguntas = PreguntasController.getInstancia();
        RepositorioDePreguntas repoPreguntas = cPreguntas.getRepositorio();
        List<Pregunta> generales = repoPreguntas.buscarPorTipo("general");
        List<CaracteristicaConRta> elegidas = new ArrayList<>();
        for (Pregunta pregunta : generales) {
            if (request.queryParams(pregunta.getPregunta()) != null) {
                String pregunta_elegida = pregunta.getPregunta();
                String respuesta_elegida = request.queryParams(pregunta.getPregunta());

                CaracteristicaConRta caracteristicaConRta = new CaracteristicaConRta(pregunta_elegida, respuesta_elegida);
                caracteristicaConRta.setMascota(mascota);
                elegidas.add(caracteristicaConRta);
            }
        }
        mascota.setCaracteristicas(elegidas);

    }

    private void asignarAtributosA(Persona persona, Request request) {
        if (request.queryParams("nombre") != null) {
            persona.setNombre(request.queryParams("nombrePersona"));
        }

        if (request.queryParams("apellido") != null) {
            persona.setApellido(request.queryParams("apellido"));
        }

        if (request.queryParams("fnacPersona") != null) {
            persona.setFechaDeNacimiento(LocalDate.parse(request.queryParams("fnacPersona")));
        }

        if (request.queryParams("tipoDoc") != null) {
            persona.setTipoDoc(TipoDeDocumento.valueOf(request.queryParams("tipoDoc")));
        }

        if (request.queryParams("nroDoc") != null) {
            persona.setNroDoc(Integer.valueOf(request.queryParams("nroDoc")));
        }
        String pais = "";
        String provincia = "";
        String direccion = "";
        if (request.queryParams("provincia") != null) {
            provincia = request.queryParams("provincia");
        }

        if (request.queryParams("pais") != null) {
            pais = request.queryParams("pais");
        }

        if (request.queryParams("direccion") != null) {
            direccion = request.queryParams("direccion");
        }
        persona.setDireccion(direccion + "," + provincia + "," + pais);

        String cNombre = "";
        String cApellido = "";
        String cCorreo = "";
        String cNumero = "";
        Estrategia medioPreferido = Estrategia.valueOf("WHATSAPP");

        if (request.queryParams("cNombre") != null) {
            cNombre = request.queryParams("cNombre");
        }

        if (request.queryParams("cApellido") != null) {
            cApellido = request.queryParams("cApellido");
        }

        if (request.queryParams("cNumero") != null) {
            cNumero = request.queryParams("cNumero");
        }

        if (request.queryParams("cCorreo") != null) {
            cCorreo = request.queryParams("cCorreo");
        }

        if (request.queryParams("medioPreferido") != null) {
            if (request.queryParams("medioPreferido").equals("Email")) {
                medioPreferido = Estrategia.valueOf("EMAIL");
            } else {
                if (request.queryParams("medioPreferido").equals("WhatsApp")) {
                    medioPreferido = Estrategia.valueOf("WHATSAPP");
                } else medioPreferido = Estrategia.valueOf("SMS");
            }
        }

        Contacto contacto = new Contacto(cNombre, cApellido, cNumero, cCorreo, medioPreferido);
        contacto.setPersona(persona);

        List<Contacto> contactos = new ArrayList<>();
        contactos.add(contacto);

        persona.setContactos(contactos);

        Duenio rolDuenio = new Duenio();
        persona.addRol(rolDuenio);
        persona.setRolElegido(rolDuenio);
    }


    public ModelAndView revisar_adopcion(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        rolController.asignarRolSiEstaLogueado(request, parametros);

        List<PublicacionEnAdopcion> encontradas = this.repo.buscarTodos();
        List<PublicacionEnAdopcion> sin_revisar = new ArrayList<>();
        for (PublicacionEnAdopcion publicacion:encontradas) {
            if(publicacion.getEstadoDePublicacion().equals(EstadoDePublicacion.SIN_REVISAR)){
                sin_revisar.add(publicacion);
            }
        }
        parametros.put("adopciones", sin_revisar);
        return new ModelAndView(parametros, "revisar_adopcion.hbs");
    }

    public ModelAndView revisar_publi(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioYPersonaSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        PublicacionEnAdopcion publicacion = this.repo.buscar(new Integer(request.params("id")));
        parametros.put("publicacion", publicacion);
        return new ModelAndView(parametros, "revisar_adopcion_publi.hbs");

    }

    public Response aprobar(Request request, Response response) {
        PublicacionEnAdopcion publicacion = this.repo.buscar(new Integer(request.params("id")));
        publicacion.setEstadoDePublicacion(EstadoDePublicacion.ACEPTADO);
        this.repo.modificar(publicacion);

        response.redirect("/revisar_adopcion");

        return response;
    }

    public Response rechazar(Request request, Response response) {
        PublicacionEnAdopcion publicacion = this.repo.buscar(new Integer(request.params("id")));
        publicacion.setEstadoDePublicacion(EstadoDePublicacion.RECHAZADO);
        this.repo.modificar(publicacion);

        response.redirect("/revisar_adopcion");

        return response;
    }

    public Object enviarDatosLog(Request request, Response response) {
        PublicacionEnAdopcion publicacion = this.repo.buscar(new Integer(request.params("id")));

        RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
        Persona adoptante = repoPersonas.dameLaPersona(request.session().attribute("id"));

        adoptante.quieroAdoptar(publicacion.getMascota());
        response.redirect("/datos_enviados");
        return response;
    }
}
