package domain.controllers;

import domain.models.entities.mascotas.Mascota;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.*;
import domain.models.entities.rol.Duenio;
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


public class PublicacionesPerdidasController {
    private RepositorioGenerico<PublicacionPerdidaRegistrada> repo;
    private UsuarioController usuarioController = UsuarioController.getInstancia();
    private RolController rolController = RolController.getInstancia();


    public PublicacionesPerdidasController() {
        this.repo = FactoryRepositorio.get(PublicacionPerdidaRegistrada.class);
    }

//    public void crearPublicacion(PublicacionGenerica publicacion) {
//        this.validarDatos();
//        this.repo.agregar(publicacion);
//    }

    private void validarDatos() {
        //TODO
    }

//    public RepositorioGenerico<PublicacionPerdidaRegistrada> getRepo() {
//        return repo;
//    }

    public ModelAndView mostrarPerdidas(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        List<PublicacionPerdidaRegistrada> perdidas = new ArrayList<>();
        List<PublicacionPerdidaRegistrada> publicaciones = this.repo.buscarTodos();
        for (PublicacionPerdidaRegistrada publi : publicaciones) {
            if (publi.estaAprobada()) {
                perdidas.add(publi);
            }
        }
        parametros.put("perdidas", perdidas);
        return new ModelAndView(parametros, "perdidas.hbs");
    }

    public ModelAndView revisar_perdida(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        rolController.asignarRolSiEstaLogueado(request, parametros);

        List<PublicacionPerdidaRegistrada> perdidas = this.repo.buscarTodos();
        List<PublicacionPerdidaRegistrada> sin_revisar = new ArrayList<>();
        for (PublicacionPerdidaRegistrada publicacion:perdidas) {
            if(publicacion.getEstadoDePublicacion().equals(EstadoDePublicacion.SIN_REVISAR)){
                sin_revisar.add(publicacion);
            }
        }
        parametros.put("perdidas", sin_revisar);

        return new ModelAndView(parametros, "revisar_perdida.hbs");
    }

    public ModelAndView revisar_publi(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        rolController.asignarRolSiEstaLogueado(request, parametros);

        PublicacionPerdidaRegistrada publicacion = this.repo.buscar(new Integer(request.params("id")));
        parametros.put("publicacion", publicacion);
        return new ModelAndView(parametros, "revisar_perdida_publi.hbs");

    }

    public Response aprobar(Request request, Response response) {
        PublicacionPerdidaRegistrada publicacion = this.repo.buscar(new Integer(request.params("id")));
        publicacion.setEstadoDePublicacion(EstadoDePublicacion.ACEPTADO);
        this.repo.modificar(publicacion);

        response.redirect("/revisar_perdida");

        return response;
    }

    public Response rechazar(Request request, Response response) {
        PublicacionPerdidaRegistrada publicacion = this.repo.buscar(new Integer(request.params("id")));
        publicacion.setEstadoDePublicacion(EstadoDePublicacion.RECHAZADO);
        this.repo.modificar(publicacion);

        response.redirect("/revisar_perdida");

        return response;
    }

    // Generar publicacion de mascota perdida

    public ModelAndView registrarPerdida(Request request, Response response) {
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

        List<Organizacion> organizaciones;

        OrganizacionController cOrg = OrganizacionController.getInstancia();
        RepositorioGenerico<Organizacion> repoOrg = cOrg.getRepositorio();
        organizaciones = repoOrg.buscarTodos();

        parametros.put("organizaciones", organizaciones);

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        PreguntasController cPreguntas = PreguntasController.getInstancia();
        RepositorioDePreguntas repoPreguntas = cPreguntas.getRepositorio();
        List<Pregunta> generales = repoPreguntas.buscarPorTipo("general");

        parametros.put("tipos", tipo);
        parametros.put("generales", generales);
        parametros.put("provincias", provincias);

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "registrar_perdida.hbs");
    }

    public Response guardarPerdida(Request request, Response response) {
        //Si está loguedo no va a ver esta página, solo considero los personas no logueadas.
        Mascota mascota = new Mascota();

        PersonaController controllerPersona = PersonaController.getInstancia();
        RepositorioDePersonas repoPersona = controllerPersona.getRepositorio();
        String cadena = request.queryParams("fnacPersona") + request.queryParams("nroDoc");
        String hashPersona = org.apache.commons.codec.digest.DigestUtils.md5Hex(cadena);
        Persona personaEncontrada = repoPersona.buscarPersona(hashPersona);

        if (personaEncontrada != null) {
            mascota.setPersona(personaEncontrada);
            personaEncontrada.addRol(new Duenio());
        } else {
            Persona persona = new Persona();
            asignarAtributosA(persona, request);
            persona.setUsuarioTemporal(hashPersona);
            mascota.setPersona(persona);
            mascota.setDuenio((Duenio) persona.getRolElegido());
            repoPersona.agregar(persona);
        }

        response.redirect("/registrar_perdida_asociacion");

        return response;
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


}

