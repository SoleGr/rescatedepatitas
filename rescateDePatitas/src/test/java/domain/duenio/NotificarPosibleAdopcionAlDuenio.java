package domain.duenio;

import domain.models.entities.mascotas.CaracteristicaConRta;
import domain.models.entities.mascotas.Foto;
import domain.models.entities.mascotas.Mascota;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.rol.Duenio;

import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificarPosibleAdopcionAlDuenio {

    Persona personaDuenio;
    Persona adoptante;
    Duenio duenio;
    ArrayList<CaracteristicaConRta> caracteristicasConRtas;
    Contacto contacto1,contacto2;
    List<Contacto> contactos, contactosRescatista;
    List<Foto> fotos;

    @Before
    public void instanciar() throws IOException {
        personaDuenio = new Persona();

        contacto1 = new Contacto("Soledad", "Grilleta", "+541157530658", "sole.012@gmail.com", Estrategia.EMAIL);
        contactos = new ArrayList<>();
        contactos.add(contacto1);
        personaDuenio.inicializar("Sole", "Grilletta", "Peru 1212,CABA", TipoDeDocumento.DNI, 3333333, LocalDate.of(1987, 9, 24),contactos);
        duenio = new Duenio();
        personaDuenio.setRolElegido(duenio);

        caracteristicasConRtas = new ArrayList<>();
        fotos = new ArrayList<>();
        Mascota.MascotaDTO mascotaDTO = new Mascota.MascotaDTO();
        mascotaDTO.inicializar(personaDuenio,"Susana","Susi",2,"tiene una mancha blanca en una pata.",
                "gato", "hembra", caracteristicasConRtas, fotos);

        personaDuenio.registrarMascota(mascotaDTO);

        adoptante = new Persona();

        contacto2 = new Contacto("Roberto Francisco", "Ginez", "+541138138227", "rginez@gmail.com", Estrategia.SMS);

        contactosRescatista = new ArrayList<>();
        contactosRescatista.add(contacto2);

        adoptante.inicializar("Roberto Francisco", "Ginez", "Brasil 1112,CABA", TipoDeDocumento.DNI, 33311111, LocalDate.of(1987, 9, 24), contactosRescatista);

    }

    @Test
    public void notificarPosibleAdopcionAlDuenio() {
        Mascota mascota = personaDuenio.getMascotas().get(0);
        mascota.meQuiereAdoptar(adoptante);
    }
}
