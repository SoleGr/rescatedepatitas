package domain.services;

import org.junit.Assert;
import org.junit.Test;
import services.Configuracion;


public class LeerArchivoConfiguracion {

    @Test
    public void leerArchivoConfiguracion() {
        Configuracion.leerArchivoCompleto();
    }

    @Test
    public void leerUnaPropiedad(){
        Assert.assertEquals("www.rescatedepatitas.com.ar",Configuracion.leerPropiedad("url"));
    }
}
