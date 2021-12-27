package domain.models.entities.publicaciones;

import domain.models.entities.mascotas.Mascota;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "publicacion_perdida_registrada")
public class PublicacionPerdidaRegistrada extends PublicacionGenerica {
    @OneToOne
    private Mascota mascota;

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}
