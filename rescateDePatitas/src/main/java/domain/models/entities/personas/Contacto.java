package domain.models.entities.personas;

import domain.models.entities.Persistente;
import domain.models.entities.notificaciones.MetodoDeEnvio;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.notificaciones.estrategias.NotificadorEmail;
import domain.models.entities.notificaciones.estrategias.NotificadorSMS;
import domain.models.entities.notificaciones.estrategias.NotificadorWhatsapp;
import domain.models.entities.notificaciones.estrategias.adapters.email.AdapterJavaMailEmail;
import domain.models.entities.notificaciones.estrategias.adapters.sms.AdapterTwilioSMS;
import domain.models.entities.notificaciones.estrategias.adapters.wpp.AdapterTwilioWhatsapp;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "contacto")
public class Contacto extends Persistente {
    @ManyToOne
    private Persona persona;
    // Atributos
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "telefono")
    private String numeroCompleto;
    @Column(name = "email")
    private String email;
    @Enumerated(EnumType.STRING)
    private Estrategia estrategiaDeEnvio;
    @Transient
    private MetodoDeEnvio metodoDeEnvio;
    @Transient
    private String mensaje;

    public Contacto(String nombre, String apellido, String numeroCompleto, String email, Estrategia estrategiaDeEnvio) {
        setNombre(nombre);
        setApellido(apellido);
        setNumeroCompleto(numeroCompleto);
        setEmail(email);
        this.estrategiaDeEnvio = estrategiaDeEnvio;
        setEstrategiaDeEnvio(estrategiaDeEnvio);
    }

    public Contacto(){
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public void notificarContacto(String mensaje) {
        setMensaje(mensaje);
        enviarNotificacion();
    }

    public void enviarNotificacion() {
        setEstrategiaDeEnvio(this.estrategiaDeEnvio);
        metodoDeEnvio.enviarNotificacion(this);
    }

    public void setEstrategiaDeEnvio(Estrategia estrategiaDeEnvio) {
        this.estrategiaDeEnvio = estrategiaDeEnvio;
        try {
            setMetodoDeEnvio(estrategiaDeEnvio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMensaje(String mensaje) {
        this.mensaje = "Hola " + nombre + ", " + mensaje;
    }

    public void setMetodoDeEnvio(Estrategia estrategiaDeEnvio) throws IOException {
        switch (estrategiaDeEnvio) {
            case SMS:
                AdapterTwilioSMS adapterTwilioSMS = new AdapterTwilioSMS();
                NotificadorSMS notificadorSMS = new NotificadorSMS(adapterTwilioSMS);
                MetodoDeEnvio metodoDeEnvioSMS = new MetodoDeEnvio(notificadorSMS);
                this.metodoDeEnvio = metodoDeEnvioSMS;
                break;
            case WHATSAPP:
                AdapterTwilioWhatsapp adapterTwilioWhatsapp = new AdapterTwilioWhatsapp();
                NotificadorWhatsapp notificadorWhatsapp = new NotificadorWhatsapp(adapterTwilioWhatsapp);
                this.metodoDeEnvio = new MetodoDeEnvio(notificadorWhatsapp);
                break;
            case EMAIL:
                AdapterJavaMailEmail adapterJavaMailEmail = new AdapterJavaMailEmail("rescateDePatitas/src/main/resources/public/config/configuration.prop",
                        "Notificación de Patitas ✨");
                NotificadorEmail notificadorEmail = new NotificadorEmail(adapterJavaMailEmail);
                MetodoDeEnvio metodoDeEnvioEmail = new MetodoDeEnvio(notificadorEmail);
                this.metodoDeEnvio = metodoDeEnvioEmail;
                break;
            default:
                System.out.println("El valor ingresado es invalido");
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumeroCompleto(String numeroCompleto) {
        this.numeroCompleto = numeroCompleto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNumeroCompleto() {
        return numeroCompleto;
    }

    public String getEmail() {
        return email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Estrategia getEstrategiaDeEnvio() {
        return estrategiaDeEnvio;
    }
}
