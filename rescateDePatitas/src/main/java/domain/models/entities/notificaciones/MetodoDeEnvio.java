package domain.models.entities.notificaciones;

import domain.models.entities.notificaciones.estrategias.EstrategiaDeNotificacion;
import domain.models.entities.personas.Contacto;

public class MetodoDeEnvio {
    private EstrategiaDeNotificacion estrategia;

    public MetodoDeEnvio(EstrategiaDeNotificacion estrategia) {
        setEstrategia(estrategia);
    }

    public void setEstrategia(EstrategiaDeNotificacion estrategia) {
        this.estrategia = estrategia;
    }

    public void enviarNotificacion(Contacto contacto) {
        this.estrategia.enviarNotificacion(contacto);
    }

    public EstrategiaDeNotificacion getEstrategia() {
        return estrategia;
    }
}
