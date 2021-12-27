package domain.controllers;

import domain.models.entities.publicaciones.Pregunta;
import domain.models.repositories.RepositorioDePreguntasAdopcion;


import java.util.List;

public class PreguntasAdopcionController {
    private static PreguntasAdopcionController instancia;
    private RepositorioDePreguntasAdopcion repositorio;


    public static PreguntasAdopcionController getInstancia() {
        if (instancia == null) {
            instancia = new PreguntasAdopcionController();
        }
        return instancia;
    }

    private PreguntasAdopcionController() {
        this.repositorio = new RepositorioDePreguntasAdopcion();
    }


    public void crearPregunta(Pregunta pregunta) {
        this.validarDatos(pregunta.getPregunta(), pregunta.getRespuestas());
        this.repositorio.agregar(pregunta);
    }


    private void validarDatos(String descripcion, List<String> respuestasPosibles) {
        //TODO
    }

    public RepositorioDePreguntasAdopcion getRepositorio() {
        return repositorio;
    }


}
