package domain.models.entities.notificaciones.estrategias.adapters.email;

import domain.models.entities.personas.Contacto;

import javax.mail.MessagingException;

public interface AdapterNotificadorEmail {
    void enviarEmail(Contacto contacto) throws MessagingException;
}
