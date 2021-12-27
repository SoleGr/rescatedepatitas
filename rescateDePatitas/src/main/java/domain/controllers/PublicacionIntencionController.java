package domain.controllers;

import domain.models.entities.mascotas.CaracteristicaConRta;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.*;
import domain.models.entities.rol.Duenio;
import domain.models.repositories.RepositorioDeOrganizaciones;
import domain.models.repositories.RepositorioDePersonas;
import domain.models.repositories.RepositorioDePreguntas;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicacionIntencionController {
    private final RepositorioGenerico<PublicacionIntencionAdopcion> repo;
    private final UsuarioController usuarioController = UsuarioController.getInstancia();
    private final RolController rolController = RolController.getInstancia();

    public PublicacionIntencionController() {
        this.repo = FactoryRepositorio.get(PublicacionIntencionAdopcion.class);
    }


    public ModelAndView mostrarAdoptantes(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        List<PublicacionIntencionAdopcion> adoptantes = this.repo.buscarTodos();
        List<PublicacionIntencionAdopcion> publicaciones = new ArrayList<>();

        for (PublicacionIntencionAdopcion publicacion : adoptantes) {
            if (publicacion.estaAprobada()) {
                publicaciones.add(publicacion);
            }
        }

        parametros.put("publicaciones", publicaciones);
        return new ModelAndView(parametros, "adoptantes.hbs");
    }

    public ModelAndView mostrarPublicacionAdoptante(Request request, Response response) {
        PublicacionIntencionAdopcion publicacion = this.repo.buscar(new Integer(request.params("id")));
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        parametros.put("publicacion", publicacion);
        return new ModelAndView(parametros, "intencion_publicacion.hbs");
    }

    public ModelAndView nuevoAdoptante(Request request, Response response) {
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

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        PreguntasController preguntasController = PreguntasController.getInstancia();
        RepositorioDePreguntas repositorioPreguntas = preguntasController.getRepositorio();
        //OrganizacionController organizacionController = OrganizacionController.getInstancia();
        //RepositorioDeOrganizaciones repoOrganizaciones = organizacionController.getRepositorio();

        List<Pregunta> preferenciasYcomodidades = repositorioPreguntas.buscarPorTipo("pyc");

        parametros.put("preguntas", preferenciasYcomodidades);
        parametros.put("provincias",provincias);
        parametros.put("tipos", tipo);

        return new ModelAndView(parametros, "adopcion_suscripcion.hbs");
    }

    public Response cargarAdoptante(Request request, Response response) {
        PublicacionIntencionAdopcion publi = new PublicacionIntencionAdopcion();

        Persona adoptante = new Persona();
        if (request.session().attribute("id") != null) {
            RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
            adoptante = repoPersonas.dameLaPersona(request.session().attribute("id"));
        } else {
            PersonaController cPersona = PersonaController.getInstancia();
            RepositorioDePersonas repoPersona = cPersona.getRepositorio();
            String cadena = request.queryParams("fnacPersona") + request.queryParams("nroDoc");
            String hashPersona = org.apache.commons.codec.digest.DigestUtils.md5Hex(cadena);
            Persona personaEncontrada = repoPersona.buscarPersona(hashPersona);

            if (personaEncontrada != null) {
                adoptante = personaEncontrada;
            } else {
                Persona persona = new Persona();
                asignarAtributosA(persona, request);
                persona.setUsuarioTemporal(hashPersona);
                repoPersona.agregar(persona);
                adoptante = persona;
            }
        }
        CuestionarioContestado cuestionarioContestado = new CuestionarioContestado();

        PreguntasController cPreguntas = PreguntasController.getInstancia();
        RepositorioDePreguntas repoPreguntas = cPreguntas.getRepositorio();
        List<Pregunta> preferenciasYComodidades = repoPreguntas.buscarPorTipo("pyc");
        List<RespuestaConcreta> elegidas = new ArrayList<>();
        for (Pregunta pregunta : preferenciasYComodidades) {
            if (request.queryParams(pregunta.getPregunta()) != null) {
                String respuesta_elegida = request.queryParams(pregunta.getPregunta());

                RespuestaConcreta respuestaConcreta = new RespuestaConcreta();
                respuestaConcreta.setPregunta(pregunta);
                respuestaConcreta.setRespuesta(respuesta_elegida);
                respuestaConcreta.setCuestionarioContestado(cuestionarioContestado);

                elegidas.add(respuestaConcreta);
            }
        }

        cuestionarioContestado.setRespuestas(elegidas);
        publi.setCuestionarioPreferenciasYComodidades(cuestionarioContestado);
        publi.setAdoptante(adoptante);
        publi.setEstadoDePublicacion(EstadoDePublicacion.ACEPTADO);
        this.repo.agregar(publi);
        adoptante.notificarSuscripcion(publi.getId());

        response.redirect("/suscripcion_ok");
        return response;
    }


    private void asignarAtributosA(Persona persona, Request request) {
        String nombre = "";
        if (request.queryParams("nombrePersona") != null) {
            nombre = request.queryParams("nombrePersona");
            persona.setNombre(nombre);
        }

        String apellido = "";
        if (request.queryParams("apellido") != null) {
            apellido = request.queryParams("apellido");
            persona.setApellido(apellido);
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

        String cCorreo = "";
        String cNumero = "";
        Estrategia medioPreferido = Estrategia.valueOf("EMAIL");

        if (request.queryParams("cNumero") != null) {
            cNumero = request.queryParams("cNumero");
        }

        if (request.queryParams("cCorreo") != null) {
            cCorreo = request.queryParams("cCorreo");
        }

        Contacto contacto = new Contacto(nombre, apellido, cNumero, cCorreo, medioPreferido);
        contacto.setPersona(persona);

        List<Contacto> contactos = new ArrayList<>();
        contactos.add(contacto);

        persona.setContactos(contactos);
    }

    public ModelAndView suscriptoOK(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros,"/suscripcion_ok.hbs");
    }
}
