package domain.services;

import services.EditorDeFotos;
import domain.models.entities.mascotas.Foto;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;

public class EditorDeFotosTest {

    @Test
    public void creaFotoConNuevasDimensiones(){
        Foto foto = new Foto();
        foto.setURLfoto("src/main/resources/FotoDePrueba.jpg");
        EditorDeFotos editorDeFotos = new EditorDeFotos();
        Foto fotoModificada = editorDeFotos.ajustarCalidad(foto);


        Assert.assertTrue(new File(fotoModificada.getURLfoto()).exists());
    }
}