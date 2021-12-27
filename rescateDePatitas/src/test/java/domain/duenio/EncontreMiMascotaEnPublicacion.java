package domain.duenio;

import domain.models.entities.mascotas.*;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.GestorDePublicaciones;
import domain.models.entities.publicaciones.PublicacionGenerica;
import domain.models.entities.publicaciones.PublicacionMascotaEncontrada;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rescatista;
import org.junit.Before;
import org.junit.Test;
import services.EditorDeFotos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EncontreMiMascotaEnPublicacion {


    Persona personaDuenio;
    Persona personaRescatista;
    Rescatista rescatista;
    Duenio duenio;

    Contacto contactoR1,contactoR2,contactoD1;
    List<Contacto> contactosRescatista;
    Foto foto;
    List<Foto> fotos;
    EditorDeFotos editor;

    PublicacionGenerica publicacion;
    DatosMascotaEncontrada datosMascota;
    Lugar lugar;
    GestorDePublicaciones gestor;
    Organizacion org1,org2,org3,org4,org5;
    List<Organizacion> organizaciones;

    @Before
    public void instanciar() throws IOException {

        personaDuenio = new Persona();
        duenio = new Duenio();
        //Llena los contactos en el formulario
        contactoD1 = new Contacto("Aracely", "Amaro", "+541168648864", "ara6amaro@gmail.com", Estrategia.EMAIL);
        List<Contacto> contactosDuenio = new ArrayList<>();
        contactosDuenio.add(contactoD1);
        personaDuenio.setContactos(contactosDuenio);


        //rescatista
        contactosRescatista = new ArrayList<>();
        contactoR1 = new Contacto("Sole", "Grilletta", "+541157530658", "sole.012@gmail.com", Estrategia.EMAIL);
        contactoR2 = new Contacto("Sole", "Grilletta", "+541157530658", "sole.012@gmail.com", Estrategia.WHATSAPP);
        contactosRescatista.add(contactoR1);
        contactosRescatista.add(contactoR2);
        personaRescatista = new Persona();
        rescatista = new Rescatista();
        personaRescatista.addRol(rescatista);
        personaRescatista.setRolElegido(rescatista);

        personaRescatista.inicializar("Sole", "Grilletta", "Urquiza 3835,CABA", TipoDeDocumento.DNI, 33333333, LocalDate.of(1987, 9, 24), contactosRescatista);
        fotos = new ArrayList<>();
        foto = new Foto();
        foto.setURLfoto("src/main/resources/FotoDePrueba2.jpg");
        fotos.add(foto);
        editor = new EditorDeFotos();
        fotos= editor.redimensionarFotos(fotos);
        lugar = new Lugar(-34.6692966,-58.4766928);
        datosMascota = new DatosMascotaEncontrada(fotos,"La encontre en buen estado.",lugar);

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
    public void duenioEncuentraASuMascotaPerdida(){
        personaRescatista.encontreUnaMascotaPerdidaSinChapita(personaRescatista,datosMascota); //genera Publicacion
        publicacion = gestor.getPublicaciones().get(0);

        personaDuenio.encontreMiMascotaPerdida((PublicacionMascotaEncontrada) publicacion);
    }

}
