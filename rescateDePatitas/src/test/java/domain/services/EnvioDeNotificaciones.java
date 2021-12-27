package domain.services;

import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnvioDeNotificaciones {
    Contacto contactoNotificadoSMS;
    Contacto contactoNotificadoWhatsapp;
    Contacto contactoNotificadoEmail;

    @Before
    public void Instanciar() throws IOException {
        contactoNotificadoSMS = new Contacto("Nahuel", "Farias", "+541138338092", "nfarias@frba.utn.edu.ar", Estrategia.SMS);
        contactoNotificadoWhatsapp = new Contacto("Nahuel", "Farias", "+541138338092", "nfarias@frba.utn.edu.ar", Estrategia.WHATSAPP);
        contactoNotificadoEmail = new Contacto("Nahuel", "Farias", "+541138338092", "nfarias@frba.utn.edu.ar", Estrategia.EMAIL);
    }

    @Test
    public void EnvioNotificacionSMS() {
        contactoNotificadoSMS.notificarContacto("tu mascota fue encontrada!");
    }

    @Test
    public void EnvioNotificacionWhatsapp() {
        contactoNotificadoWhatsapp.notificarContacto("tu mascota fue encontrada!");
    }

    @Test
    public void EnvioNotificacionEmail() {

        try {
            contactoNotificadoEmail.notificarContacto("tu mascota fue encontrada!");;
        } catch (InvalidParameterException e) {
            Logger.getLogger("Error al enviar mail").log(Level.SEVERE, null, e);
        }
    }

}
