package domain.models.entities.notificaciones.estrategias;

import domain.models.entities.notificaciones.estrategias.adapters.wpp.AdapterNotificadorWhatsapp;
import domain.models.entities.personas.Contacto;

public class NotificadorWhatsapp implements EstrategiaDeNotificacion{
    private AdapterNotificadorWhatsapp adapter;

    public NotificadorWhatsapp(AdapterNotificadorWhatsapp adapter) {
        this.adapter = adapter;
    }

    public void enviarNotificacion(Contacto contacto) {
        this.adapter.enviarWhatsapp(contacto);
    }

}
