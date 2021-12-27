package domain.controllers;

import domain.controllers.exceptions.CaracteristicaSinDescripcionException;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.publicaciones.Pregunta;
import domain.models.repositories.RepositorioDeCaracteristicas;
import domain.models.repositories.RepositorioDePreguntas;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;
import domain.models.repositories.factories.FactoryRepositorio;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreguntasController {
    private static PreguntasController instancia;
    private RepositorioDePreguntas repositorio;

    public PreguntasController(){
        DAO<Pregunta> dao = new DAOHibernate<>(Pregunta.class);
        this.repositorio = new RepositorioDePreguntas(dao);
    }

    public static PreguntasController getInstancia() {
        if (instancia == null) {
            instancia = new PreguntasController();
        }
        return instancia;
    }

    public void crearCaracteristica(String descripcion, List<String> respuestasPosibles,String tipo) {
        this.validarDatos(descripcion, respuestasPosibles);
        Pregunta caracteristica = new Pregunta();
        caracteristica.setPregunta(descripcion);
        caracteristica.setRespuestas(respuestasPosibles);
        caracteristica.setTipoDePregunta(tipo);
        this.repositorio.agregar(caracteristica);
    }

    private void validarDatos(String descripcion, List<String> respuestasPosibles) {
        if (descripcion == null) {
            throw new CaracteristicaSinDescripcionException("La caracteristica no tiene descripcion");
        }
        if (respuestasPosibles == null) {
            throw new CaracteristicaSinDescripcionException("La caracteristica no tiene respuestas posibles");
        }
    }

    public RepositorioDePreguntas getRepositorio() {
        return repositorio;
    }

    public ModelAndView cuestionarioAgregarPregunta(Request request, Response response){
        Map<String, Object> parametros = new HashMap<>();

        UsuarioController usuarioController = UsuarioController.getInstancia();
        usuarioController.asignarUsuarioSiEstaLogueado(request, parametros);

        RolController rolController = RolController.getInstancia();
        rolController.asignarRolSiEstaLogueado(request, parametros);

        List<String> tipo = new ArrayList<>();
        tipo.add("general");
        tipo.add("pyc");
        parametros.put("tipo", tipo);

        return new ModelAndView(parametros,"admin_caracteristica.hbs");
    }

    public Response agregarPregunta(Request request, Response response){
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta(request.queryParams("pregunta"));
        if(request.queryParams("preguntaMascota") != null){
            pregunta.setPreguntaMascota(request.queryParams("preguntaMascota"));
        }

        int cantidad = Integer.parseInt(request.queryParams("member"));
        pregunta.setRespuestas(new ArrayList<>());
        for(int i=0; i<cantidad; i++){
            pregunta.getRespuestas().add(request.queryParams("Respuesta" + i));
        }
        pregunta.setTipoDePregunta(request.queryParams("tipo"));

        repositorio.agregar(pregunta);

        response.redirect("/admin_ok");
        return response;
    }
}
