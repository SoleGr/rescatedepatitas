package domain.models.entities.notificaciones.estrategias;

import domain.models.entities.notificaciones.estrategias.adapters.email.AdapterNotificadorEmail;
import domain.models.entities.personas.Contacto;

import javax.mail.MessagingException;

public class NotificadorEmail implements EstrategiaDeNotificacion{
    private AdapterNotificadorEmail adapter;

    public NotificadorEmail(AdapterNotificadorEmail adapter) {
        setAdapter(adapter);
    }

    public void setAdapter(AdapterNotificadorEmail adapter) {
        this.adapter = adapter;
    }

    public void enviarNotificacion(Contacto contacto) {
        try {
            this.adapter.enviarEmail(contacto);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
