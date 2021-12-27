package domain.models.entities.notificaciones.estrategias;

import domain.models.entities.notificaciones.estrategias.adapters.sms.AdapterNotificadorSMS;
import domain.models.entities.personas.Contacto;

public class NotificadorSMS implements EstrategiaDeNotificacion {
    private AdapterNotificadorSMS adapter;

    public NotificadorSMS(AdapterNotificadorSMS adapter) {
        setAdapter(adapter);
    }

    public void setAdapter(AdapterNotificadorSMS adapter) {
        this.adapter = adapter;
    }

    public void enviarNotificacion(Contacto contacto) {
        this.adapter.enviarSMS(contacto);
    }

}
