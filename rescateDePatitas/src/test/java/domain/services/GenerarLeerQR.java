package domain.services;

import services.GeneradorQR;
import services.LectorQR;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;


public class GenerarLeerQR {

    String data = "http://www.google.com";
    String pathQRaGenerar = "src/main/resources/qr.jpg";
    String pathQRKaspersky = "src/main/resources/qr_kaspersky.png";


    @Test
    public void generarQRcorrecto() throws Exception {
        File imagenGenerada = new File("src/main/resources/qr.jpg");
        GeneradorQR generador = new GeneradorQR();

        if(imagenGenerada.exists()) { imagenGenerada.delete(); }

        assertFalse(imagenGenerada.exists());

        generador.generarQR(data,pathQRaGenerar,"jpg",500,500);

        assertTrue(imagenGenerada.exists());

    }

    @Test
    public void leerQRcorrecto(){

        LectorQR lector = new LectorQR();

        lector.leerQR(pathQRaGenerar);
        lector.leerQR(pathQRKaspersky);
    }

}

