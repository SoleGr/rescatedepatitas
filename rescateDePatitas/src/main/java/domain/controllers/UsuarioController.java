package domain.controllers;

import domain.models.entities.mascotas.Mascota;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.personas.Usuario;
import domain.models.entities.publicaciones.Pregunta;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rescatista;
import domain.models.entities.validacion.AlgoritmoPassword;
import domain.models.repositories.RepositorioDePersonas;
import domain.models.repositories.RepositorioDePreguntas;
import domain.models.repositories.RepositorioDeUsuarios;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioController {
    private RepositorioDeUsuarios repositorio;
    private static UsuarioController instancia;

    public UsuarioController() {
        DAO<Usuario> dao = new DAOHibernate<>(Usuario.class);
        this.repositorio = new RepositorioDeUsuarios(dao);
    }

    public static UsuarioController getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioController();
        }
        return instancia;
    }

    public RepositorioDeUsuarios getRepositorio() {
        return repositorio;
    }

    public void asignarUsuarioSiEstaLogueado(Request request, Map<String, Object> parametros) {
        List<Usuario> usuarios = this.repositorio.buscarTodos();
        parametros.put("usuarios", usuarios);

        if (!request.session().isNew() && request.session().attribute("id") != null) {
            Usuario usuario = repositorio.buscar(request.session().attribute("id"));
            parametros.put("usuario", usuario);
        }
    }

    public void asignarUsuarioYPersonaSiEstaLogueado(Request request, Map<String, Object> parametros) {
        List<Usuario> usuarios = this.repositorio.buscarTodos();
        parametros.put("usuarios", usuarios);

        if (!request.session().isNew() && request.session().attribute("id") != null) {
            Usuario usuario = repositorio.buscar(request.session().attribute("id"));
            parametros.put("usuario", usuario);
        }
    }



    public ModelAndView crearUsuario(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        return new ModelAndView(parametros, "registro_usuario.hbs");
    }

    public ModelAndView crearUsuarioError(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("error", "El usuario ya existe.");
        return new ModelAndView(parametros, "registro_usuario_error.hbs");
    }

    public Response validarUsuario(Request request, Response response) {

        if (request.queryParams("usrname") != null) {
            String nombre = request.queryParams("usrname");

            if (this.repositorio.existe(nombre)) {
                //El usuario ya existe, error
                response.redirect("/registro_usuario/error_usr");
            } else {
                //el usuario no existe, compruebo la contrasenia y si está ok busco la persona
                if (request.queryParams("psw") != null) {
                    String contrasenia = request.queryParams("psw");

                    if (!AlgoritmoPassword.verificarValidez(contrasenia)) {
                        // String mensaje = "La contraseña es débil.";
                        //request.session().attribute("errorRegistro",mensaje);
                        response.redirect("/registro_usuario");
                    } else {
                        //nombre de usuario y contrasenia ok
                        Usuario usuario = new Usuario();
                        usuario.setNombreDeUsuario(nombre);
                        usuario.hashPassword(contrasenia);
                        this.repositorio.agregar(usuario);

                        request.session().attribute("usuarioNuevo", String.valueOf(usuario.getId()));
                        response.redirect("/registro_usuario_datos");
                    }
                }
            }
        }

        return response;
    }


    public ModelAndView ingresarDatosUsuario(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController.getInstancia().asignarUsuarioSiEstaLogueado(request, parametros);
        RolController.getInstancia().asignarRolSiEstaLogueado(request, parametros);

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

        PreguntasController cPreguntas = PreguntasController.getInstancia();
        RepositorioDePreguntas repoPreguntas = cPreguntas.getRepositorio();
        List<Pregunta> generales = repoPreguntas.buscarPorTipo("general");

        parametros.put("tipos", tipo);
        parametros.put("generales", generales);
        parametros.put("provincias", provincias);

        return new ModelAndView(parametros, "registro_usuario_datos.hbs");
    }

    public ModelAndView usuarioCreado(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        return new ModelAndView(parametros, "usuario_creado.hbs");
    }

    public Response cargarDatosUsuario(Request request, Response response) {
        Usuario usuario = this.repositorio.buscar(Integer.parseInt(request.session().attribute("usuarioNuevo")));

        PersonaController cPersona = PersonaController.getInstancia();
        RepositorioDePersonas repoPersona = cPersona.getRepositorio();

        String cadena = request.queryParams("fnacPersona") + request.queryParams("nroDoc");
        String hashPersona = org.apache.commons.codec.digest.DigestUtils.md5Hex(cadena);
        Persona personaEncontrada = repoPersona.buscarPersona(hashPersona);

        if (personaEncontrada != null) {
            personaEncontrada.setUsuario(usuario);
            repoPersona.modificar(personaEncontrada);
        } else {
            Persona persona = new Persona();
            asignarAtributosA(persona, request);
            Duenio rolDuenio = new Duenio();
            rolDuenio.setPersona(persona);
            Rescatista rolRescatista = new Rescatista();
            rolRescatista.setPersona(persona);
            persona.addRol(rolDuenio);
            persona.addRol(rolRescatista);
            persona.setUsuario(usuario);
            repositorio.agregar(usuario);
            repoPersona.agregar(persona);
        }

        response.redirect("usuario_creado");
        return response;
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
    }

    public ModelAndView mostrarMisMascotas(Request request, Response response) throws Exception {
        Map<String, Object> parametros = new HashMap<>();

        PersonaController cPersona = PersonaController.getInstancia();
        RepositorioDePersonas repoPersona = cPersona.getRepositorio();
        Persona persona = repoPersona.dameLaPersona(request.session().attribute("id"));
        persona.setRolElegido(new Duenio());
        List<Mascota> mascotas = new ArrayList<>();

        try {
            mascotas = persona.getMascotas();
        } catch (Exception e) {
            System.out.println("No tiene mascotas");
        }

        parametros.put("mascotas", mascotas);
        return new ModelAndView(parametros, "mis_mascotas.hbs");
    }
}
