package domain.services;

import services.BuscadorDeHogaresDeTransito;
import services.FiltradorDeHogaresDeTransito;
import domain.models.entities.hogares.ListadoDeHogares;
import domain.models.entities.hogares.Hogar;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BuscadorYFiltradorDeHogaresTest {
    @Test
    public void buscaHogaresDeTransitoDeLaAPI() {
        BuscadorDeHogaresDeTransito buscadorDeHogaresDeTransito = BuscadorDeHogaresDeTransito.getInstancia();
        ListadoDeHogares listadoDeHogares = null;
        try {
            listadoDeHogares = new ListadoDeHogares();
            listadoDeHogares = buscadorDeHogaresDeTransito.listadoDeHogares(1);
        } catch (Exception e) {e.printStackTrace();}
        Assert.assertNotNull(listadoDeHogares);
        List<Hogar> listaHogares = new FiltradorDeHogaresDeTransito().filtrarPorAnimalAceptado(listadoDeHogares.hogares, "perro");
        int i = 0;
        for(Hogar hogar: listaHogares){
            System.out.println(i + ") " + hogar.nombre);
            System.out.println(hogar.telefono);
            System.out.println(hogar.capacidad);
            System.out.println(hogar.lugares_disponibles);
            if(hogar.ubicacion!=null)
                System.out.println(hogar.ubicacion.direccion);
            i++;
            if(hogar.admisiones!=null){
                if(hogar.admisiones.gatos){
                    System.out.println("Acepta gatos");
                    System.out.println(hogar.ubicacion.direccion);
                }
                System.out.println("1");
            }
            System.out.println(hogar.lugares_disponibles);

        }


    }
}
