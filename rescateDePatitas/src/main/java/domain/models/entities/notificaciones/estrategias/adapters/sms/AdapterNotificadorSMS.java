package domain.models.entities.notificaciones.estrategias.adapters.sms;

import domain.models.entities.personas.Contacto;

public interface AdapterNotificadorSMS {
    void enviarSMS(Contacto contacto);
}
