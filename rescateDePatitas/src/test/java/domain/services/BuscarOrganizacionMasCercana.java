package domain.services;


import domain.models.entities.mascotas.Lugar;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.publicaciones.GestorDePublicaciones;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.ComparadorDistancias;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuscarOrganizacionMasCercana {
    Organizacion org1,org2,org3,org4,org5,orgMasCercana;
    Lugar puntoEncuentro;
    GestorDePublicaciones gestor;
    List<Organizacion> organizaciones;

    @Before
    public void instanciar() throws IOException {
        organizaciones = new ArrayList<>();

        org1 = new Organizacion();
        org1.setNombre("Huellitas");
        org1.setUbicacion(new Lugar(-34.6335328,-58.4921025));
        organizaciones.add(org1);

        org2 = new Organizacion();
        org2.setNombre("Naricitas Frias");
        org2.setUbicacion(new Lugar(-34.5888834,-58.5455626));
        organizaciones.add(org2);


        org3 = new Organizacion();
        org3.setNombre("El Hogar de Claudia");
        org3.setUbicacion(new Lugar(-34.6038713,-58.5754228));
        organizaciones.add(org3);

        org4 = new Organizacion();
        org4.setNombre("Ayudacan");
        org4.setUbicacion(new Lugar(-34.6321582,-58.468661));
        organizaciones.add(org4);

        org5 = new Organizacion();
        org5.setNombre("El refugio"); //Lugano
        org5.setUbicacion(new Lugar(-34.6766714,-58.4790033));
        organizaciones.add(org5);

        puntoEncuentro = new Lugar(-34.6621347,-58.4803575); //Campus

        gestor = GestorDePublicaciones.getInstancia();
        gestor.setOrganizaciones(organizaciones);

    }

    @Test
    public void compararDistancias(){
        orgMasCercana = gestor.buscarOrganizacionMasCercana(puntoEncuentro);
        Assert.assertEquals("El refugio",orgMasCercana.getNombre());
    }
}
