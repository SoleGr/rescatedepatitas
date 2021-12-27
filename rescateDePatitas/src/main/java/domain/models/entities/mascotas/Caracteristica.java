package domain.models.entities.mascotas;

import java.util.ArrayList;
import java.util.List;

public class Caracteristica {
    private String descripcion;
    private List<String> respuestasPosibles;

    public Caracteristica(){
        this.respuestasPosibles = new ArrayList<String>();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getRespuestasPosibles() {
        return respuestasPosibles;
    }

    public void setRespuestasPosibles(List<String> respuestas) { this.respuestasPosibles = respuestas; }
    public void agregarRespuesta(String respuesta){ respuestasPosibles.add(respuesta); }


}
