package domain.duenio;

import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class crearUsuarioEIniciarSesion {
    Persona persona;
    Rol duenio;
    List<Contacto> contactos;
    Contacto contacto1,contacto2;


    @Before
    public void Instanciar() {
        persona = new Persona();
        duenio = new Duenio();
        contactos = new ArrayList<>();

        ArrayList<String> rtas2 = new ArrayList<>();
        rtas2.add("Negro");
        rtas2.add("Marron");
        rtas2.add("Rubio");
        rtas2.add("Ninguno de estos");
    }

    @Test
    public void crearUsuarioPorPrimeraVez() {

        contacto1 = new Contacto("Maria Victoria","Sanchez","1155555555","mvicsanchez@gmail.com", Estrategia.SMS);
        contacto2 = new Contacto("Agustin","Greco","1166666666","agugreco@gmail.com", Estrategia.SMS);
        contactos.add(contacto1);
        contactos.add(contacto2);

        persona.inicializar("Maria Victoria","Sanchez","Peru 1212,CABA", TipoDeDocumento.DNI,3333333, LocalDate.of(1987, 9, 24),contactos);
        persona.crearUsuario("Victoria09", "MiPerro22!!##");


        System.out.println ("Datos del Dueño");
        System.out.println ("Nombre:" + persona.getNombre());
        System.out.println ("Apellido:" + persona.getApellido());
        System.out.println ("Direccion:" + persona.getDireccion());
        System.out.println ("Tipo de documento:" + persona.getTipoDoc());
        System.out.println ("Numero de documento:" + persona.getNroDoc());
        System.out.println ("Fecha de nacimiento:" + persona.getFechaDeNacimiento());
        System.out.println("Usuario en la plataforma: " + persona.getUsuario().getNombreDeUsuario());
        System.out.println("Contrasenia en la plataforma: " + persona.getUsuario().getContrasenia());
        System.out.println ("-------------");
    }

    @Test
    public void crearUsuarioEIniciarSesionConDatosCorrectos(){

        contacto1 = new Contacto("Maria Victoria","Sanchez","1155555555","mvicsanchez@gmail.com", Estrategia.SMS);
        contacto2 = new Contacto("Agustin","Greco","1166666666","agugreco@gmail.com", Estrategia.SMS);
        contactos.add(contacto1);
        contactos.add(contacto2);

        persona.inicializar("Maria Victoria","Sanchez","Peru 1212,CABA", TipoDeDocumento.DNI,3333333,LocalDate.of(1987, 9, 24),contactos);
        persona.crearUsuario("Victoria09", "MiPerro22!!##");

        Assert.assertTrue(persona.iniciarSesion("Victoria09", "MiPerro22!!##"));
    }

    @Test
    public void crearUsuarioEIniciarSesionConUsuarioIncorrecto(){

        contacto1 = new Contacto("Maria Victoria","Sanchez","1155555555","mvicsanchez@gmail.com", Estrategia.SMS);
        contacto2 = new Contacto("Agustin","Greco","1166666666","agugreco@gmail.com", Estrategia.SMS);
        contactos.add(contacto1);
        contactos.add(contacto2);

        persona.inicializar("Maria Victoria","Sanchez","Peru 1212,CABA", TipoDeDocumento.DNI,3333333,LocalDate.of(1987, 9, 24),contactos);
        persona.crearUsuario("Victoria09", "MiPerro22!!##");

        Assert.assertFalse(persona.iniciarSesion("Victoria091", "MiPerro22!!##"));
    }

    @Test
    public void crearUsuarioEIniciarSesionConContraseniaIncorrecta(){

        contacto1 = new Contacto("Maria Victoria","Sanchez","1155555555","mvicsanchez@gmail.com", Estrategia.SMS);
        contacto2 = new Contacto("Agustin","Greco","1166666666","agugreco@gmail.com", Estrategia.SMS);
        contactos.add(contacto1);
        contactos.add(contacto2);

        persona.inicializar("Maria Victoria","Sanchez","Peru 1212,CABA", TipoDeDocumento.DNI,3333333,LocalDate.of(1987, 9, 24),contactos);
        persona.crearUsuario("Victoria09", "MiPerro22!!##");

        Assert.assertFalse(persona.iniciarSesion("Victoria09", "MiPerr22!!##"));
    }

    @Test
    public void crearUsuarioEIniciarSesionConDatosIncorrectos(){

        contacto1 = new Contacto("Maria Victoria","Sanchez","1155555555","mvicsanchez@gmail.com", Estrategia.SMS);
        contacto2 = new Contacto("Agustin","Greco","1166666666","agugreco@gmail.com", Estrategia.SMS);
        contactos.add(contacto1);
        contactos.add(contacto2);

        persona.inicializar("Maria Victoria","Sanchez","Peru 1212,CABA", TipoDeDocumento.DNI,3333333,LocalDate.of(1987, 9, 24),contactos);
        persona.crearUsuario("Victoria09", "MiPerro22!!##");

        Assert.assertFalse(persona.iniciarSesion("Victoria092", "MiPerr22!!##"));
    }

    @Test
    public void crearUsuarioEIniciarSesionConDatosIncorrectosMasDe3Veces() throws IOException {

        contacto2 = new Contacto("Nahuel", "Farias", "+541138338092", "nfarias@frba.utn.edu.ar", Estrategia.EMAIL);

        contacto1 = new Contacto("Maria Victoria","Sanchez","+541138338092","mvicsanchez@gmail.com", Estrategia.SMS);
        contactos.add(contacto1);
        contactos.add(contacto2);

        persona.inicializar("Maria Victoria","Sanchez","Peru 1212,CABA", TipoDeDocumento.DNI,3333333,LocalDate.of(1987, 9, 24), contactos);
        persona.crearUsuario("Victoria09", "MiPerro22!!##");

        Assert.assertFalse(persona.iniciarSesion("Victoria092", "MiPerr22!!##"));
        Assert.assertFalse(persona.iniciarSesion("Victoria09", "MiPerro2!!##"));
        Assert.assertFalse(persona.iniciarSesion("Victoria09", "MiPerr22!!##"));
    }
}