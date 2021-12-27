package domain.controllers;

import domain.models.entities.mascotas.Mascota;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.Usuario;
import domain.models.entities.publicaciones.PublicacionEnAdopcion;
import domain.models.entities.publicaciones.PublicacionGenerica;
import domain.models.entities.publicaciones.PublicacionIntencionAdopcion;
import domain.models.entities.publicaciones.PublicacionMascotaEncontrada;
import domain.models.entities.rol.*;
import domain.models.repositories.*;
import domain.models.repositories.daos.DAOHibernate;
import domain.models.repositories.factories.FactoryRepositorio;
import domain.models.repositories.factories.FactoryRepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginController {
    private DAOHibernate daoHibernate = new DAOHibernate();

    private Usuario usuarioHasheador;
    private RepositorioDePersonas repoPersonas = new RepositorioDePersonas(daoHibernate);
    private UsuarioController usuarioController = UsuarioController.getInstancia();

    public ModelAndView inicio(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        return new ModelAndView(parametros, "login.hbs");
    }

    public Response login(Request request, Response response) {
        try {
            RepositorioDeUsuarios repoUsuarios = FactoryRepositorioUsuarios.get();

            String nombreDeUsuario = request.queryParams("nombreDeUsuario");
            String contraseniaBase = request.queryParams("contrasenia");
            String hashed_password = org.apache.commons.codec.digest.DigestUtils.md5Hex(contraseniaBase);

            if (repoUsuarios.existe(nombreDeUsuario) && hashed_password.equals(
                    repoUsuarios.dameElUsuario(nombreDeUsuario).getContrasenia())) {

                Usuario usuario = repoUsuarios.buscarUsuario(nombreDeUsuario);

                request.session(true);
                request.session().attribute("id", usuario.getId());

                response.redirect("/cambiar_rol");
            } else {
                response.redirect("/login");
            }
        } catch (Exception e) {
            //Funcionalidad disponible solo con persistencia en Base de Datos
            response.redirect("/404"); // TODO Cambiar a otra pagina
        } finally {
            return response;
        }
    }

    public Response logout(Request request, Response response) {
        request.session().invalidate();
        response.redirect("/");
        return response;
    }

    public ModelAndView mostrarRoles(Request request, Response response) {
        Persona persona = this.repoPersonas.dameLaPersona(request.session().attribute("id"));
        Map<String, Object> parametros = new HashMap<>();

        Set<Rol> roles = null;
        System.out.println(1);
        try {
            roles = persona.getRolesDisponibles();
            System.out.println(2);
        } catch (Exception e) {
            System.out.println(3);
            persona.addRol(new Duenio());
            persona.addRol(new Rescatista());
            System.out.println(4);
            roles = persona.getRolesDisponibles();
            System.out.println(5);
        }
        System.out.println(6);

        parametros.put("roles", roles);
        return new ModelAndView(parametros, "seleccionar_rol.hbs");
    }

    public Response rol_elegido_duenio(Request request, Response response) {
        Persona persona = this.repoPersonas.dameLaPersona(request.session().attribute("id"));
        request.session().attribute("rol", "Duenio");
        persona.setRolElegido(new Duenio());

        response.redirect("/");
        return response;
    }

    public Response rol_elegido_voluntario(Request request, Response response) {
        Persona persona = this.repoPersonas.dameLaPersona(request.session().attribute("id"));
        request.session().attribute("rol", "Voluntario");
        persona.setRolElegido(new Voluntario());
        response.redirect("/");
        return response;
    }

    public Response rol_elegido_rescatista(Request request, Response response) {
        Persona persona = this.repoPersonas.dameLaPersona(request.session().attribute("id"));
        request.session().attribute("rol", "Rescatista");
        persona.setRolElegido(new Rescatista());
        response.redirect("/");
        return response;
    }

    public Response rol_elegido_admin(Request request, Response response) {
        Persona persona = this.repoPersonas.dameLaPersona(request.session().attribute("id"));
        request.session().attribute("rol", "Administrador");
        persona.setRolElegido(new Administrador());
        response.redirect("/admin");
        return response;
    }

    public ModelAndView admin(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        RolController rolController = RolController.getInstancia();
        rolController.asignarRolSiEstaLogueado(request, parametros);

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        Persona persona = this.repoPersonas.dameLaPersona(request.session().attribute("id"));
        parametros.put("persona", persona);

        RepositorioGenerico<Mascota> repoMacotas = new RepositorioGenerico<>(new DAOHibernate<>(Mascota.class));
        int cantMascotas = repoMacotas.buscarTodos().size();
        parametros.put("cantMascotas", cantMascotas);

        RepositorioGenerico<PublicacionEnAdopcion> repoAdopcion = FactoryRepositorio.get(PublicacionEnAdopcion.class);
        RepositorioGenerico<PublicacionMascotaEncontrada> repoEncontrada = FactoryRepositorio.get(PublicacionMascotaEncontrada.class);
        RepositorioGenerico<PublicacionIntencionAdopcion> repoIntencion = FactoryRepositorio.get(PublicacionIntencionAdopcion.class);

        int cantPubliEnAdopcion = repoAdopcion.buscarTodos().size();
        int cantMascotasEncontradas = repoEncontrada.buscarTodos().size();
        int cantIntencionAdopcion = repoIntencion.buscarTodos().size();


        parametros.put("cantPubliEnAdopcion", cantPubliEnAdopcion);
        parametros.put("cantMascotasEncontradas", cantMascotasEncontradas);
        parametros.put("cantIntencionAdopcion", cantIntencionAdopcion);

        return new ModelAndView(parametros, "index_admin.hbs");
    }

    public ModelAndView cuestionarioNuevaOrg(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        Persona persona = this.repoPersonas.dameLaPersona(request.session().attribute("id"));
        parametros.put("persona", persona);

        RolController rolController = RolController.getInstancia();
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "admin_nuevaOrg.hbs");
    }

}
