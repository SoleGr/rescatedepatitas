package domain.models.entities.notificaciones.estrategias.adapters.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import domain.models.entities.personas.Contacto;
import services.Configuracion;

public class AdapterTwilioSMS implements AdapterNotificadorSMS {
    public static final String ACCOUNT_SID = "ACf9a15ce926e3f281df16a70f1f7624ea";
    public static final String AUTH_TOKEN = "2fbf0173773dddeb20c92eabf81114f2";

    public void enviarSMS(Contacto contacto) {
        System.out.println("Enviando SMS a "+ contacto.getNumeroCompleto()+" por Twilio: '"+ contacto.getMensaje()+"'");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber(contacto.getNumeroCompleto()),
                new PhoneNumber("+12242231661"),
                contacto.getMensaje()).create();

        System.out.println(message.getSid());
    }

}
