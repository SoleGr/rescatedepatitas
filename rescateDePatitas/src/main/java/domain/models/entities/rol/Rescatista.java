package domain.models.entities.rol;

import domain.models.entities.mascotas.*;
import domain.models.entities.publicaciones.GestorDePublicaciones;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "rescatista")
public class Rescatista extends Rol {
    @OneToMany(mappedBy = "rescatista", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Mascota> mascotasRescatadas = new ArrayList<>();

    public Rescatista(){
        super("Rescatista");
    }

    public void registrarMascota(Mascota.MascotaDTO mascota, Persona persona) {

    }

    public List<Mascota> getMascotas() {
        return mascotasRescatadas;
    }

    public void agregarMascota(Mascota.MascotaDTO mascotaDTO){
        Persona duenio = mascotaDTO.getPersona();
        Mascota mascota = new Mascota(duenio);
        mascota.inicializar(mascotaDTO);
        mascotasRescatadas.add(mascota);
    }

    public void encontreUnaMascotaPerdida(Mascota mascotaPerdida, Contacto contacto, DatosMascotaEncontrada datos) {
        //con chapita
        mascotaPerdida.avisarQueMeEcontraron(contacto,datos);
    }

    public void encontreUnaMascotaPerdidaSinChapita(Persona rescatista, DatosMascotaEncontrada datosMascota) {
        GestorDePublicaciones gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionMascotaEncontrada(rescatista,datosMascota);
    }


}

