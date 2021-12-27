package domain.models.entities.notificaciones.estrategias;

import domain.models.entities.personas.Contacto;

public interface EstrategiaDeNotificacion {
    void enviarNotificacion(Contacto contacto);
}
