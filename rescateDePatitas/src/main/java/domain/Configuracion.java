package domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public final class Configuracion {

    public static String leerPropiedad(String propiedad){
        String valor = null;

        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream("rescateDePatitas/src/main/resources/config.properties"));
             valor = propiedades.getProperty(propiedad);
        } catch (FileNotFoundException e) {
            System.out.println("Error: El archivo no existe.");
        } catch (IOException e) {
            System.out.println("Error: No se puede leer el archivo.");
        }
        return valor;
    }

    public static void leerArchivoCompleto(){

        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream("rescateDePatitas/src/main/resources/config.properties"));
            Enumeration<Object> claves = propiedades.keys();
            while (claves.hasMoreElements()) {
                Object clave = claves.nextElement();
                System.out.println(clave.toString() + " = " + propiedades.get(clave).toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: El archivo no existe 2.");
        } catch (IOException e) {
            System.out.println("Error: No se puede leer el archivo 2.");
        }
    }

    public static void modificarPropiedad(String key, String value){
        try{
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream("src/main/resources/config.properties"));
            propiedades.setProperty(key, value);
            propiedades.store(new FileOutputStream("src/main/resources/config.properties"), null);
        }catch (FileNotFoundException e){
            System.out.println("Error: El archivo no existe 3.");
        }catch (SecurityException e) {
            System.out.println("Error: Security Exception.");
        }catch (IOException e){
            System.out.println("Error: No se puede leer el archivo 3.");
        }
    }

}
