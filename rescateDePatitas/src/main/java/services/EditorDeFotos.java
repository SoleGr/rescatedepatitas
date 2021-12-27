package services;

import domain.models.entities.mascotas.Foto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

//Seguramente habra que hacer cambios mas adelante
public class EditorDeFotos {
    public static Foto ajustarCalidad(Foto foto){
        int anchoImagen = 488;
        int altoImagen = 274;
        BufferedImage imgOriginal = null;
        Foto fotoModificada = new Foto();
        fotoModificada.setURLfoto(foto.getURLfoto().replaceAll(".jpg", "Redimensionada.jpg"));

        try {
            imgOriginal = ImageIO.read(new File(foto.getURLfoto()));
        } catch (IOException e) {System.out.println(e.toString());}
        if (imgOriginal != null) {
            BufferedImage imgRedimensionada = new BufferedImage(anchoImagen, altoImagen, imgOriginal.getType());
            Graphics2D g2d = imgRedimensionada.createGraphics();
            g2d.drawImage(imgOriginal, 0, 0, anchoImagen, altoImagen, null);
            g2d.dispose();
            try {
                ImageIO.write(imgRedimensionada, "jpg", new File(fotoModificada.getURLfoto()));
            } catch (IOException e) {
                System.out.println(e.toString());
            }

        }return fotoModificada;
    }

    public List<Foto> redimensionarFotos(List<Foto> fotosOrigianles) {
        fotosOrigianles.forEach(foto -> foto.editarFoto());
        return fotosOrigianles;
    }
}