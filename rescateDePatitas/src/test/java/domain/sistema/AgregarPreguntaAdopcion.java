package domain.sistema;

import domain.controllers.PreguntasAdopcionController;
import domain.models.entities.publicaciones.Pregunta;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarPreguntaAdopcion {
    PreguntasAdopcionController controller;
    Pregunta pregunta;
    List<String> respuestas;

    @Before
    public void instanciar() throws IOException {
        controller = PreguntasAdopcionController.getInstancia();
        pregunta = new Pregunta();
        respuestas = new ArrayList<>();
        respuestas.add("si");
        respuestas.add("no");
        pregunta.setPregunta("Acepta gatos");
        pregunta.setRespuestas(respuestas);
        controller.crearPregunta(pregunta);

        pregunta = new Pregunta();
        respuestas = new ArrayList<>();
        respuestas.add("si");
        respuestas.add("no");
        pregunta.setPregunta("Acepta perros");
        pregunta.setRespuestas(respuestas);
        controller.crearPregunta(pregunta);

        pregunta = new Pregunta();
        respuestas = new ArrayList<>();
        respuestas.add("si");
        respuestas.add("no");
        pregunta.setPregunta("Acepta mascotas adultas");
        pregunta.setRespuestas(respuestas);
        controller.crearPregunta(pregunta);

        pregunta = new Pregunta();
        respuestas = new ArrayList<>();
        respuestas.add("chico");
        respuestas.add("mediano");
        respuestas.add("grande");
        respuestas.add("cualquiera");
        pregunta.setPregunta("Tamaño que prefiere");
        pregunta.setRespuestas(respuestas);
        controller.crearPregunta(pregunta);
    }

    @Test
    public void agregarPreguntasAdopcionTest() {

        List<Pregunta> preguntasAdopcion = controller.getRepositorio().preguntas;

        for(int i = 0; i < preguntasAdopcion.size(); i++) {
            System.out.println(preguntasAdopcion.get(i).getPregunta() + ":");
            for(int j = 0; j < preguntasAdopcion.get(i).getRespuestas().size(); j++){
                System.out.println(preguntasAdopcion.get(i).getRespuestas().get(j));
            }
        }

    }
}
