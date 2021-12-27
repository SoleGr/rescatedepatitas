package domain.models.repositories.testMemoData;

import domain.models.entities.Persistente;
import domain.models.entities.mascotas.Mascota;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Data {

    public static List<Persistente> getData(Class type) {
        List<Persistente> entidades = new ArrayList<>();

        if (type.getName().equals(Usuario.class.getName())) {
            entidades = DataUsuario.getList();
        } else if (type.getName().equals(Persona.class.getName())) {
            entidades = DataPersona.getList();
        } else if (type.getName().equals(Mascota.class.getName())) {
            entidades = DataMascota.getList();
        }

        return entidades;
    }
}
