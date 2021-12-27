package domain.controllers;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import domain.Configuracion;
import domain.models.entities.personas.Persona;
import domain.models.repositories.RepositorioDePersonas;
import domain.models.repositories.daos.DAOHibernate;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.*;

public class AdminController {
    //private DAOHibernate daoHibernate = new DAOHibernate();

    private RepositorioDePersonas repoPersonas = RepositorioDePersonas.getInstancia();
    private UsuarioController usuarioController = UsuarioController.getInstancia();
    private PreguntasController preguntasController = new PreguntasController();

    public ModelAndView admin_preguntas(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);
        Persona persona = this.repoPersonas.dameLaPersona(request.session().attribute("id"));
        parametros.put("persona", persona);

        return new ModelAndView(parametros, "admin_preguntas.hbs");
    }

    public Response nueva_pregunta(Request request, Response response) {
        String descripcion = request.queryParams("descripcion");
        String tipo = request.queryParams("tipo");
        String respuestasPosiblesBase = request.queryParams("respuestasPosibles");

        ArrayList<String> respuestasPosibles = new ArrayList<>(Arrays.asList(respuestasPosiblesBase.split(",")));

        preguntasController.crearCaracteristica(descripcion, respuestasPosibles, tipo);

        response.redirect("/admin");
        return response;
    }

    public ModelAndView dimensiones_fotos(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        RolController rolController = RolController.getInstancia();
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "admin_fotos.hbs");
    }

    public Response nuevas_dimensiones(Request request, Response response){
        String nueva_altura = request.queryParams("altura");
        String nuevo_ancho = request.queryParams("ancho");

        Configuracion.modificarPropiedad("heightPhoto", nueva_altura);
        Configuracion.modificarPropiedad("widthPhoto", nuevo_ancho);
        response.redirect("/admin_ok");
        return response;
    }

    public ModelAndView mensajeOk(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        RolController rolController = RolController.getInstancia();
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "admin_ok.hbs");
    }
}
