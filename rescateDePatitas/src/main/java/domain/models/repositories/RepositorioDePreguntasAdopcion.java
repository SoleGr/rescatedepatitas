package domain.models.repositories;

import domain.models.entities.publicaciones.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDePreguntasAdopcion {

    private static RepositorioDePreguntasAdopcion instancia;
    public List<Pregunta> preguntas = new ArrayList<Pregunta>();


    public static RepositorioDePreguntasAdopcion getInstancia() {
        if (instancia == null) {
            instancia=new RepositorioDePreguntasAdopcion();
        }
        return instancia;
    }

    public void agregar(Pregunta pregunta){
        this.preguntas.add(pregunta);
    }

    public List<Pregunta> buscarTodos(){
        return preguntas;

    }

    public void eliminar(Pregunta pregunta){
        //TODO
    }

    public Pregunta buscar(String descripcion){
        //TODO caracteristicas.stream().filter(d -> d.getDescripcion() == descripcion);
        Pregunta pregunta = new Pregunta();
        return pregunta;
    }
}
