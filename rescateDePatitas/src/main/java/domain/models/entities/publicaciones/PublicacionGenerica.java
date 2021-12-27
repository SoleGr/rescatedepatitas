package domain.models.entities.publicaciones;

import domain.models.entities.mascotas.Mascota;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.personas.Persona;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PublicacionGenerica {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    @Column(name = "fecha", columnDefinition = "DATE")
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    private EstadoDePublicacion estadoDePublicacion = EstadoDePublicacion.SIN_REVISAR;
    @Column(name = "tipoPublicacion")
    private String tipoPublicacion;

    public PublicacionGenerica(){
            this.setFecha(LocalDate.now());
    }

    public EstadoDePublicacion getEstadoDePublicacion() {
        return estadoDePublicacion;
    }

    public void setEstadoDePublicacion(EstadoDePublicacion estadoDePublicacion) {
        this.estadoDePublicacion = estadoDePublicacion;
    }

    public Organizacion getOrganizacion(){return new Organizacion();}

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipoPublicacion() {
        return tipoPublicacion;
    }

    public void setTipoPublicacion(String tipoPublicacion) {
        this.tipoPublicacion = tipoPublicacion;
    }

    public Mascota getMascota(){return new Mascota(new Persona());};

    public Persona getAdoptante() {
        return null;
    }

    public int getId(){return id;}

    public boolean estaAprobada(){
        return this.estadoDePublicacion.equals(EstadoDePublicacion.ACEPTADO);
    }
}
