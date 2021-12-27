package domain.models.entities.notificaciones.estrategias.adapters.wpp;

import domain.models.entities.personas.Contacto;

public interface AdapterNotificadorWhatsapp {
    void enviarWhatsapp(Contacto contacto);
}
