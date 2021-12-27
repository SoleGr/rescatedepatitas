package domain.models.entities.publicaciones;

import domain.models.entities.mascotas.Mascota;
import domain.models.entities.mascotas.Organizacion;

import javax.persistence.*;

@Entity
@Table(name = "publicacion_en_adopcion")
public class PublicacionEnAdopcion extends PublicacionGenerica{
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Organizacion organizacion;
    @OneToOne(cascade = {CascadeType.PERSIST})
    private Mascota mascota;
    @OneToOne(cascade = {CascadeType.PERSIST})
    private CuestionarioContestado cuestionariodeAdopcion;

    public CuestionarioContestado getCuestionario() {
        return cuestionariodeAdopcion;
    }

    public void setCuestionario(CuestionarioContestado cuestionarioContestado) {
        this.cuestionariodeAdopcion = cuestionarioContestado;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

}
