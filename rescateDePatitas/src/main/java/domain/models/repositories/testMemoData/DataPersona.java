package domain.models.repositories.testMemoData;

import domain.models.entities.Persistente;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.Usuario;
import domain.models.repositories.factories.FactoryRepositorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataPersona {
    private static List<Persona> personas = new ArrayList<>();

    public static List<Persistente> getList(){
        if(personas.size() == 0) {
            //Repositorio<Rol> repoRol = FactoryRepositorio.get(Rol.class);

            Persona lucas = new Persona();
            lucas.setNombre("Lucas");
            lucas.setApellido("Saclier");
            //lucas.setId(1);
            //lucas.setTelefono(1156585936);
            //lucas.setLegajo(112233);
            //lucas.setRol(repoRol.buscar(1));
            Usuario lucasU = new Usuario("lucas@mail.com","1234");
            lucas.setUsuario(lucasU);

            Persona eze = new Persona();
            eze.setNombre("Eze");
            eze.setApellido("Escobar");
            //eze.setId(2);
            //eze.setTelefono(1156339837);
            //eze.setLegajo(112244);
            //eze.setRol(repoRol.buscar(2));
            Usuario ezeU = new Usuario("eze@mail.com","eze1");
            lucas.setUsuario(ezeU);

            Persona gaston = new Persona();
            gaston.setNombre("Gaston");
            gaston.setApellido("Prieto");
            //gaston.setId(3);
            //gaston.setTelefono(1156449831);
            //gaston.setLegajo(112255);
            //gaston.setRol(repoRol.buscar(2));
            Usuario gastonU = new Usuario("gaston@mail.com","1234");
            lucas.setUsuario(gastonU);

            Persona ezeS = new Persona();
            ezeS.setNombre("Eze");
            ezeS.setApellido("Sosa");
            //ezeS.setId(4);
            //ezeS.setTelefono(1156559832);
            //ezeS.setLegajo(112266);
            //ezeS.setRol(repoRol.buscar(2));
            Usuario ezeSU = new Usuario("ezes@mail.com","boca");
            lucas.setUsuario(ezeSU);

            DataUsuario.addAll(lucasU, ezeU, gastonU, ezeSU);
            addAll(lucas, eze, gaston, ezeS);
        }
        return (List<Persistente>)(List<?>) personas;
    }

    protected static void addAll(Persona ... personas){
        Collections.addAll(DataPersona.personas, personas);
    }
}
