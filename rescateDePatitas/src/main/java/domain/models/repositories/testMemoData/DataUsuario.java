package domain.models.repositories.testMemoData;

import domain.models.entities.Persistente;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.Usuario;
import domain.models.repositories.factories.FactoryRepositorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataUsuario {

    private static List<Usuario> usuarios = new ArrayList<>();

    public static List<Persistente> getList(){
        if(usuarios.size() == 0) {
            //Repositorio<Rol> repoRol = FactoryRepositorio.get(Rol.class);

            Persona lucas = new Persona();
            lucas.setNombre("Lucas2");
            lucas.setApellido("Saclier2");
            Usuario lucasU = new Usuario("lucas2@mail.com","12342");
            lucas.setUsuario(lucasU);

            Persona eze = new Persona();
            eze.setNombre("Eze2");
            eze.setApellido("Escobar2");
            Usuario ezeU = new Usuario("eze2@mail.com","eze12");
            lucas.setUsuario(ezeU);

            Persona gaston = new Persona();
            gaston.setNombre("Gaston2");
            gaston.setApellido("Prieto2");
            Usuario gastonU = new Usuario("gaston2@mail.com","12342");
            lucas.setUsuario(gastonU);

            Persona ezeS = new Persona();
            ezeS.setNombre("Eze2");
            ezeS.setApellido("Sosa2");
            Usuario ezeSU = new Usuario("ezes2@mail.com","boca2");
            lucas.setUsuario(ezeSU);

            addAll(lucasU, ezeU, gastonU, ezeSU);
            DataPersona.addAll(lucas,eze,gaston,ezeS);
        }
        return (List<Persistente>)(List<?>) usuarios;
    }

    protected static void addAll(Usuario ... usuarios){
        Collections.addAll(DataUsuario.usuarios, usuarios);
    }
}
