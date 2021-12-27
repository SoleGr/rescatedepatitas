package domain.controllers;

import domain.models.entities.mascotas.Lugar;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.publicaciones.Pregunta;
import domain.models.repositories.RepositorioDeCaracteristicas;
import domain.models.repositories.RepositorioDeOrganizaciones;
import domain.models.repositories.RepositorioDePreguntas;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.*;

public class OrganizacionController {
    private static OrganizacionController instancia;
    private RepositorioDeOrganizaciones repositorio;

    public OrganizacionController(){
        DAO<Organizacion> dao = new DAOHibernate<>(Organizacion.class);
        this.repositorio = new RepositorioDeOrganizaciones(dao);
    }

    public static OrganizacionController getInstancia() {
        if (instancia == null) {
            instancia=new OrganizacionController();
        }
        return instancia;
    }

    public RepositorioDeOrganizaciones getRepositorio() {
        return repositorio;
    }

    public Response crearOrganizacion(Request request, Response response){
        Organizacion organizacion = new Organizacion();
        Lugar ubicacion = new Lugar();

        ubicacion.setLatitud(Double.parseDouble(request.queryParams("latitud")));
        ubicacion.setLongitud(Double.parseDouble(request.queryParams("longitud")));

        organizacion.setUbicacion(ubicacion);
        organizacion.setNombre(request.queryParams("nombreOrg"));

        repositorio.agregar(organizacion);

        response.redirect("/admin_ok");
        return response;
    }

    public ModelAndView cuestionarioAgregarPregunta(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();

        List<Organizacion> organizaciones = repositorio.buscarTodos();
        parametros.put("organizaciones", organizaciones);

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        RolController rolController = RolController.getInstancia();
        rolController.asignarRolSiEstaLogueado(request, parametros);

        return new ModelAndView(parametros, "admin_preguntaOrg.hbs");
    }

    public Response agregarPregunta(Request request, Response response){
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta(request.queryParams("pregunta"));
        int cantidad = Integer.parseInt(request.queryParams("member"));
        pregunta.setRespuestas(new ArrayList<>());
        for(int i=0; i<cantidad; i++){
            pregunta.getRespuestas().add(request.queryParams("Respuesta" + i));
        }
        pregunta.setTipoDePregunta("asociacion");

        Organizacion organizacion = repositorio.buscarPorNombre(request.queryParams("asociacion"));
        if(organizacion.getPreguntasAdopcion() == null){
            organizacion.setPreguntasAdopcion(new ArrayList<>());
        }
        organizacion.getPreguntasAdopcion().add(pregunta);
        pregunta.setOrganizacion(organizacion);
        repositorio.agregar(organizacion);

        response.redirect("/admin_ok");
        return response;
    }
}
