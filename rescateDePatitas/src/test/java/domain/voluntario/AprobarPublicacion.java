package domain.voluntario;

import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.personas.Persona;
import domain.models.entities.publicaciones.PublicacionEnAdopcion;
import domain.models.entities.publicaciones.PublicacionGenerica;
import domain.models.entities.publicaciones.PublicacionIntencionAdopcion;
import domain.models.entities.rol.Voluntario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AprobarPublicacion {
    Persona persona;
    Organizacion veteCan;
    PublicacionGenerica publicacion;
    PublicacionGenerica publicacion2;
    PublicacionGenerica publicacion3;
    Voluntario voluntario;

    @Before

    public void Instanciar(){
        persona = new Persona();
        veteCan = new Organizacion();
        publicacion = new PublicacionEnAdopcion();
        publicacion2 = new PublicacionIntencionAdopcion();
        publicacion3 = new PublicacionEnAdopcion();
        voluntario = new Voluntario();
        persona.setRolElegido(voluntario);

    }

    @Test
    public void aprobarPublicacionTest(){

        veteCan.generarVoluntario(persona);

        veteCan.getPublicaciones().add(publicacion);
        veteCan.getPublicaciones().add(publicacion2);
        veteCan.getPublicaciones().add(publicacion3);

        Assert.assertEquals("SIN_REVISAR",publicacion.getEstadoDePublicacion().toString());
        Assert.assertEquals("SIN_REVISAR",publicacion2.getEstadoDePublicacion().toString());
        Assert.assertEquals("SIN_REVISAR",publicacion2.getEstadoDePublicacion().toString());

        persona.aprobarPublicacion(veteCan.getPublicaciones().get(0),veteCan);
        persona.rechazarPublicacion(veteCan.getPublicaciones().get(1),veteCan);
        persona.enRevisionPublicacion(veteCan.getPublicaciones().get(2),veteCan);

        Assert.assertEquals("ACEPTADO",publicacion.getEstadoDePublicacion().toString());
        Assert.assertEquals("RECHAZADO",publicacion2.getEstadoDePublicacion().toString());
        Assert.assertEquals("EN_REVISION",publicacion3.getEstadoDePublicacion().toString());

    }

}
