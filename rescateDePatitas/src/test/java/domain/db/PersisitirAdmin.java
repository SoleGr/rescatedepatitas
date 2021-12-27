package domain.db;

import db.EntityManagerHelper;
import domain.models.entities.mascotas.Lugar;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.personas.Persona;
import domain.models.entities.publicaciones.GestorDePublicaciones;
import domain.models.entities.rol.Administrador;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rescatista;
import domain.models.entities.rol.Voluntario;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PersisitirAdmin {

    @Test
    public void todosLosRoles(){
        Persona persona = new Persona();
        persona.crearUsuario("Victoria09", "MiPerro22!!##");

        Duenio duenio = new Duenio();
        duenio.setPersona(persona);

        Voluntario voluntario = new Voluntario();
        voluntario.setPersona(persona);

        Rescatista rescatista = new Rescatista();
        rescatista.setPersona(persona);

        Administrador administrador = new Administrador();
        administrador.setPersona(persona);

        persona.addRol(duenio);
        persona.addRol(voluntario);
        persona.addRol(rescatista);
        persona.addRol(administrador);
        persona.setRolElegido(duenio);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(persona);
        EntityManagerHelper.commit();
    }

    @Test
    public void organizaciones(){
        List<Organizacion> organizaciones = new ArrayList<>();
        Organizacion org1 = new Organizacion();
        org1.setNombre("Huellitas");
        org1.setUbicacion(new Lugar(-34.6335328,-58.4921025));
        organizaciones.add(org1);

        Organizacion org2 = new Organizacion();
        org2.setNombre("Naricitas Frias");
        org2.setUbicacion(new Lugar(-34.5888834,-58.5455626));
        organizaciones.add(org2);

        Organizacion org3 = new Organizacion();
        org3.setNombre("El Hogar de Claudia");
        org3.setUbicacion(new Lugar(-34.6038713,-58.5754228));
        organizaciones.add(org3);

        Organizacion org4 = new Organizacion();
        org4.setNombre("Ayudacan");
        org4.setUbicacion(new Lugar(-34.6321582,-58.468661));
        organizaciones.add(org4);

        Organizacion org5 = new Organizacion();
        org5.setNombre("El refugio"); //Lugano
        org5.setUbicacion(new Lugar(-34.6766714,-58.4790033));
        organizaciones.add(org5);
        GestorDePublicaciones gestor = GestorDePublicaciones.getInstancia();
        gestor.setOrganizaciones(organizaciones);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(org1);
        EntityManagerHelper.getEntityManager().persist(org2);
        EntityManagerHelper.getEntityManager().persist(org3);
        EntityManagerHelper.getEntityManager().persist(org4);
        EntityManagerHelper.getEntityManager().persist(org5);
        EntityManagerHelper.commit();
    }
}
