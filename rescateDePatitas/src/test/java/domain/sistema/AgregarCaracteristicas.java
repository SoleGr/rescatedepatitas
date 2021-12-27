package domain.sistema;

import domain.controllers.CaracteristicasController;
import domain.models.entities.publicaciones.Pregunta;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

public class AgregarCaracteristicas {

    @Test
    public void agregarCaracteristicasTest() {

        CaracteristicasController c = CaracteristicasController.getInstancia();
        ArrayList<String> rtas = new ArrayList<String>();
        rtas.add("Si");
        rtas.add("No");
        c.crearCaracteristica("Esta castrado", rtas);

        ArrayList<String> rtas2 = new ArrayList<String>();
        rtas2.add("Negro");
        rtas2.add("Marron");
        rtas2.add("Rubio");
        rtas2.add("Gris");
        rtas2.add("Otro");
        c.crearCaracteristica("Color principal", rtas2);

        ArrayList<String> rtas3 = new ArrayList<String>();
        rtas3.add("Negro");
        rtas3.add("Marron");
        rtas3.add("Rubio");
        rtas3.add("Gris");
        rtas3.add("Otro");
        c.crearCaracteristica("Color secundario", rtas3);

        ArrayList<String> rtas4 = new ArrayList<>();
        rtas4.add("Si");
        rtas4.add("No");
        c.crearCaracteristica("Toma medicacion", rtas4);

        List<Pregunta> caracteristicas = c.getRepositorio().caracteristicas;

        for(int i = 0; i < caracteristicas.size(); i++) {
            System.out.println(caracteristicas.get(i).getPregunta() + ":");
            for(int j = 0; j < caracteristicas.get(i).getRespuestas().size(); j++){
                System.out.println(caracteristicas.get(i).getRespuestas().get(j));
            }
        }

    }

}
