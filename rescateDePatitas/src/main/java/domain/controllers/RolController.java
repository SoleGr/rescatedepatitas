package domain.controllers;

import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.Usuario;
import domain.models.entities.rol.Rol;
import domain.models.entities.rol.Voluntario;
import domain.models.repositories.RepositorioDeOrganizaciones;
import domain.models.repositories.RepositorioDePersonas;
import domain.models.repositories.RepositorioDeUsuarios;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.factories.FactoryRepositorio;
import domain.models.repositories.factories.FactoryRepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RolController {
    private RepositorioGenerico<Rol> repo;
    private static RolController instancia;

    private RolController(){
        this.repo = FactoryRepositorio.get(Rol.class);
    }

    public static RolController getInstancia() {
        if (instancia == null) {
            instancia = new RolController();
        }
        return instancia;
    }


    public void asignarRolSiEstaLogueado(Request request, Map<String, Object> parametros){
        if(request.session().attribute("id") != null){
            String rol = request.session().attribute("rol");

            parametros.put("rol", rol);
        }
    }

    public ModelAndView cuestionario_rol_voluntario(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();

        List<Organizacion> organizaciones = new ArrayList<>();

        OrganizacionController cOrg = OrganizacionController.getInstancia();
        RepositorioGenerico<Organizacion> repoOrg = cOrg.getRepositorio();
        organizaciones = repoOrg.buscarTodos();

        parametros.put("organizaciones", organizaciones);

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        RolController rolController = RolController.getInstancia();
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "admin_voluntarios.hbs");
    }

    public Response dar_rol_voluntario(Request request, Response response){

        RepositorioDeUsuarios repoUsuarios = FactoryRepositorioUsuarios.get();
        Usuario usuario = repoUsuarios.buscarUsuario(request.queryParams("nombre"));
        if(usuario == null){
            response.redirect("/admin_voluntarios_error");
        }

        RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
        Persona persona = repoPersonas.dameLaPersona(usuario.getId());

        RepositorioDeOrganizaciones repoOrganizaciones = RepositorioDeOrganizaciones.getInstancia();
        Organizacion organizacion = repoOrganizaciones.buscarPorNombre(request.queryParams("asociacion"));

        Voluntario voluntario = new Voluntario();
        voluntario.setOrganizacion(organizacion);
        voluntario.setPersona(persona);
        persona.getRolesDisponibles().add(voluntario);

        repoPersonas.agregar(persona);

        response.redirect("/admin_ok");
        return response;
    }

    public ModelAndView dar_rol_voluntario_error(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        RolController rolController = RolController.getInstancia();
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "admin_voluntarios_error.hbs");
    }
}
