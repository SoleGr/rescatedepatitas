package domain.models.entities.rol;

import com.google.zxing.WriterException;
import domain.models.entities.mascotas.*;
import domain.models.entities.publicaciones.*;
import domain.models.entities.personas.Persona;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "duenio")
public class Duenio extends Rol{
    @OneToMany(mappedBy = "duenio", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Mascota> mascotas = new ArrayList<>();

    public Duenio(){
        super("Duenio");
    }

    public void perdiUnaMascota(Mascota mascota) {
        mascota.avisarQueMePerdi();
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void registrarMascota(Mascota.MascotaDTO mascotaDTO, Persona persona){

        Mascota mascota = new Mascota(persona);

        try {
            mascota.generarQR();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

        mascota.inicializar(mascotaDTO);

        mascotas.add(mascota);
    }

    public void registrarMascota(Mascota mascota){
        mascotas.add(mascota);
    }

    public void darEnAdopcion(Mascota mascota, Organizacion organizacion, List<RespuestaConcreta> respuestasDeLaOrg, List<RespuestaConcreta> respuestasGenerales) {
        GestorDePublicaciones gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionEnAdopcion(mascota,respuestasDeLaOrg,respuestasGenerales,organizacion);

    }


}
