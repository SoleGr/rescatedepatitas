package domain.models.entities.notificaciones;

import domain.models.entities.personas.Contacto;

public class Notificacion {
    private Contacto contacto;
    private MetodoDeEnvio metodoDeEnvio;
    private String mensaje;

    public Notificacion(Contacto contacto, MetodoDeEnvio metodoDeEnvio, String mensaje) {
        setContacto(contacto);
        setMetodoDeEnvio(metodoDeEnvio);
        setMensaje(mensaje);
    }

    private void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    private void setMetodoDeEnvio(MetodoDeEnvio metodoDeEnvio) {
        this.metodoDeEnvio = metodoDeEnvio;
    }

    private void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return contacto.getNombre();
    }

    public String getApellido() {
        return contacto.getApellido();
    }

    public String getEmail() {
        return contacto.getEmail();
    }

    public String getNumeroCompleto() {
        return contacto.getNumeroCompleto();
    }

    public String getMensaje() {
        return mensaje;
    }
}
