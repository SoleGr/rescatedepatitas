package domain.models.entities;

import domain.Configuracion;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

//Seguramente habra que hacer cambios mas adelante
public class EditorDeFotos {
    public static void ajustarCalidad(){
        int anchoImagen = 488;
        int altoImagen = 274;

        try{
            Configuracion configuracion = new Configuracion();
            anchoImagen = Integer.parseInt(configuracion.leerPropiedad("widthPhoto"));
            altoImagen = Integer.parseInt(configuracion.leerPropiedad("heightPhoto"));
        }catch(NumberFormatException ex){
            System.out.println("Error: Formato de numero equivocado en EditorDeFotos.");
        }

        BufferedImage imgOriginal = null;
        try {
            imgOriginal = ImageIO.read(new File("rescateDePatitas/src/main/resources/FotoDePrueba.jpg"));
        } catch (IOException e) {System.out.println(e.toString());}
        BufferedImage imgRedimensionada = new BufferedImage(anchoImagen, altoImagen, imgOriginal.getType());
        Graphics2D g2d = imgRedimensionada.createGraphics();
        g2d.drawImage(imgOriginal, 0, 0, anchoImagen, altoImagen, null);
        g2d.dispose();
        try {
            ImageIO.write(imgRedimensionada, "jpg", new File("src/main/resources/FotoRedimensionada.jpg"));
        } catch (IOException e) {System.out.println(e.toString());}
    }
}