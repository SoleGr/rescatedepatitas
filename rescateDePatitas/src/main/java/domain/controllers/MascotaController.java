package domain.controllers;

import domain.models.entities.mascotas.*;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.Pregunta;
import domain.models.entities.rol.Duenio;
import domain.models.repositories.*;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MascotaController {
    private  static MascotaController instancia;
    private RepositorioGenerico<Mascota> repositorio;
    private final RolController rolController = RolController.getInstancia();
    private final UsuarioController usuarioController = UsuarioController.getInstancia();
    private RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();


    public MascotaController(){
        DAO<Mascota> dao = new DAOHibernate<>(Mascota.class);
        this.repositorio = new RepositorioGenerico<>(dao);
    }

    public static MascotaController getInstancia() {
        if (instancia == null) {
            instancia = new MascotaController();
        }
        return instancia;
    }

    public RepositorioGenerico<Mascota> getRepositorio(){
        return this.repositorio;
    }

    public ModelAndView registroMascota(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        List<TipoDeDocumento> tipo = new ArrayList<>();

        UsuarioController.getInstancia().asignarUsuarioSiEstaLogueado(request, parametros);
        RolController.getInstancia().asignarRolSiEstaLogueado(request, parametros);
        //usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        //rolController.asignarRolSiEstaLogueado(request, parametros);

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

        List<Organizacion> organizaciones;

        OrganizacionController cOrg = OrganizacionController.getInstancia();
        RepositorioGenerico<Organizacion> repoOrg = cOrg.getRepositorio();
        organizaciones = repoOrg.buscarTodos();

        parametros.put("organizaciones", organizaciones);

        //usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        //rolController.asignarRolSiEstaLogueado(request, parametros);
        UsuarioController.getInstancia().asignarUsuarioSiEstaLogueado(request, parametros);
        RolController.getInstancia().asignarRolSiEstaLogueado(request, parametros);

        PreguntasController cPreguntas = PreguntasController.getInstancia();
        RepositorioDePreguntas repoPreguntas = cPreguntas.getRepositorio();
        List<Pregunta> generales = repoPreguntas.buscarPorTipo("general");

        parametros.put("tipos", tipo);
        parametros.put("generales", generales);
        parametros.put("provincias", provincias);

        return new ModelAndView(parametros, "registro_mascota.hbs");
    }

    public Response guardarMascota(Request request, Response response) throws IOException {
        Mascota mascota = new Mascota();
        asignarAtributosA(mascota, request);

        if (request.session().attribute("id") != null) {
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
            } else {
                Persona persona = new Persona();
                asignarAtributosA(persona, request);
                persona.setUsuarioTemporal(hashPersona);
                mascota.setPersona(persona);
                mascota.setDuenio(persona.getDuenio());
                persona.getDuenio().registrarMascota(mascota);
                repoPersona.agregar(persona);
            }
        }

        this.repositorio.agregar(mascota);

        response.redirect("/ok");

        return response;
    }

    private void asignarAtributosA(Mascota mascota, Request request) throws IOException {

        File uploadDir = new File("rescateDePatitas/src/main/resources/public/img/fotosmascotas");
        //uploadDir.mkdir();
        Path tempFile = Files.createTempFile(uploadDir.toPath(), request.queryParams("nombre"), ".jpg");
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try (InputStream input = request.raw().getPart("foto").getInputStream()) {
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        List<Foto> fotos = new ArrayList<>();
        Foto foto = new Foto();
        foto.setURLfoto(tempFile.toString().replace("rescateDePatitas\\src\\main\\resources\\public\\img\\fotosmascotas\\", "img/fotosmascotas/"));
        foto.setMascota(mascota);
        fotos.add(foto);
        mascota.setFotos(fotos);

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
            mascota.setDescripcion("descripcion");
        }

        if (request.queryParams("edad") != null) {
            int edad = Integer.parseInt(request.queryParams("edad"));
            mascota.setEdad(edad);
        }

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
            if (request.queryParams(pregunta.getPreguntaMascota()) != null) {
                String pregunta_elegida = pregunta.getPregunta();
                String respuesta_elegida = request.queryParams(pregunta.getPreguntaMascota());

                CaracteristicaConRta caracteristicaConRta = new CaracteristicaConRta(pregunta_elegida, respuesta_elegida);
                caracteristicaConRta.setMascota(mascota);
                elegidas.add(caracteristicaConRta);
            }
        }
        mascota.setCaracteristicas(elegidas);

    }

    private void asignarAtributosA(Persona persona, Request request) {
        if (request.queryParams("nombrePersona") != null) {
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

    public ModelAndView creada(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController.getInstancia().asignarUsuarioSiEstaLogueado(request, parametros);
        RolController.getInstancia().asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "ok.hbs");
    }

    public ModelAndView registroMascotaAsoc(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        List<Organizacion> organizaciones;

        UsuarioController.getInstancia().asignarUsuarioSiEstaLogueado(request, parametros);
        RolController.getInstancia().asignarRolSiEstaLogueado(request, parametros);

        OrganizacionController cOrg = OrganizacionController.getInstancia();
        RepositorioGenerico<Organizacion> repoOrg = cOrg.getRepositorio();
        organizaciones = repoOrg.buscarTodos();

        parametros.put("organizaciones", organizaciones);
        return new ModelAndView(parametros, "registro_mascota_asociacion.hbs");
    }

    public Response guardarAsociacion(Request request, Response response) {
        Organizacion organizacion;
        String nombre;

        if (request.queryParams("asociacion") != null) {
            nombre = request.queryParams("asociacion");
            OrganizacionController cOrg = OrganizacionController.getInstancia();
            RepositorioDeOrganizaciones repoOrg = cOrg.getRepositorio();
            organizacion = repoOrg.buscarPorNombre(nombre);

            if (request.session().attribute("id") != null) {
                RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
                Persona duenio = repoPersonas.dameLaPersona(request.session().attribute("id"));
                List<Mascota> mascotas = duenio.getMascotas();
                Mascota mascota = mascotas.get(mascotas.size() - 1);
                mascota.setOrganizacion(organizacion);
                this.repositorio.modificar(mascota);
            }

        }

        response.redirect("/ok");
        return response;
    }

    public ModelAndView darEnAdopcion(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController.getInstancia().asignarUsuarioSiEstaLogueado(request, parametros);
        RolController.getInstancia().asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "dar_adopcion.hbs");
    }


}
