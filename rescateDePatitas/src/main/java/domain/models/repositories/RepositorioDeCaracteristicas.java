package domain.models.repositories;

import domain.models.entities.publicaciones.Pregunta;
import org.hibernate.jpa.spi.ParameterRegistration;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeCaracteristicas {
    private static RepositorioDeCaracteristicas instancia;
    public List<Pregunta> caracteristicas = new ArrayList<Pregunta>();

    public static RepositorioDeCaracteristicas getInstancia() {
        if (instancia == null) {
            instancia=new RepositorioDeCaracteristicas();
        }
        return instancia;
    }

    public void agregar(Pregunta caracteristica){
        this.caracteristicas.add(caracteristica);
    }

    public List<Pregunta> buscarTodos(){
        return caracteristicas;

    }

    public void eliminar(Pregunta caracteristica){
        //TODO
    }

    public Pregunta buscar(String descripcion){
        //TODO caracteristicas.stream().filter(d -> d.getDescripcion() == descripcion);
        Pregunta caracteristica = new Pregunta();
        return caracteristica;
    }
}
