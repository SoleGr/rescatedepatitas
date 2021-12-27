package domain.rescatista;

import domain.models.entities.mascotas.*;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.GestorDePublicaciones;
import domain.models.entities.rol.Rescatista;
import org.junit.Before;
import org.junit.Test;
import services.EditorDeFotos;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EncontreSinChapita {

    Persona personaDuenio;
    Persona personaRescatista;
    Rescatista rescatista;

    Contacto contacto1,contacto2;
    List<Contacto> contactos;
    Foto foto;
    List<Foto> fotos;
    EditorDeFotos editor;
    GestorDePublicaciones gestor;
    Lugar lugar;
    String descripcion;
    DatosMascotaEncontrada datosMascota;
    Organizacion org1,org2,org3,org4,org5,orgMasCercana;
    List<Organizacion> organizaciones;

    @Before
    public void instanciar() throws IOException {
        personaRescatista = new Persona();
        contacto1 = new Contacto("Soledad", "Grilleta", "+541122222242", "sole.012@gmail.com", Estrategia.EMAIL);
        contacto2 = new Contacto("Nahuel", "Farias", "+541138338092", "nfarias@frba.utn.edu.ar", Estrategia.SMS);
        contactos = new ArrayList<>();
        contactos.add(contacto1);
        contactos.add(contacto2);
        personaRescatista.inicializar("Maria Victoria", "Sanchez", "Peru 1212,CABA", TipoDeDocumento.DNI, 3333333, LocalDate.of(1987, 9, 24),contactos);

        rescatista = new Rescatista();
        personaRescatista.setRolElegido(rescatista);
        //Hasta aca la creacion de un rescatista

        fotos = new ArrayList<>();
        foto = new Foto();
        foto.setURLfoto("src/main/resources/FotoDePrueba2.jpg");
        fotos.add(foto);
        editor = new EditorDeFotos();
        fotos= editor.redimensionarFotos(fotos);
        lugar = new Lugar(-34.6692966,-58.4766928);
        descripcion = "La encontre el dia 7/7 en buen estado.";

        datosMascota = new DatosMascotaEncontrada(fotos,descripcion,lugar);

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

        gestor = GestorDePublicaciones.getInstancia();
        gestor.setOrganizaciones(organizaciones);

    }

    @Test
    public void elRescatistaEncuentraUnaMascotaSinQR() {

        personaRescatista.encontreUnaMascotaPerdidaSinChapita(personaRescatista,datosMascota);
        personaRescatista.encontreUnaMascotaPerdidaSinChapita(personaRescatista,datosMascota);
        personaRescatista.encontreUnaMascotaPerdidaSinChapita(personaRescatista,datosMascota);

        gestor = GestorDePublicaciones.getInstancia();

        System.out.println("Fecha de publicacion:"+gestor.getPublicaciones().get(0).getFecha());
        System.out.println("Asociacion asignada: "+ gestor.getPublicaciones().get(0).getOrganizacion().getNombre());
        assertEquals(gestor.getPublicaciones().size(),3);
    }
}
