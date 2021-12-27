package domain.models.entities.rol;

import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.publicaciones.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Voluntario")
public class Voluntario extends Rol {
    //Podemos tener un ManyToOne que no tenga correspondencia en la otra clase
    @ManyToOne
    private Organizacion organizacion;

    public Voluntario(){
        super("Voluntario");
    }

    public void setOrganizacion(Organizacion organizacion){ this.organizacion = organizacion; }

    public void aprobarPublicacion(PublicacionGenerica unaPublicacion, Organizacion organizacion) {
        unaPublicacion.setEstadoDePublicacion(EstadoDePublicacion.ACEPTADO);
    }

    public void rechazarPublicacion(PublicacionGenerica unaPublicacion, Organizacion organizacion) {
        unaPublicacion.setEstadoDePublicacion(EstadoDePublicacion.RECHAZADO);
    }

    public void enRevisionPublicacion(PublicacionGenerica unaPublicacion, Organizacion organizacion){
        unaPublicacion.setEstadoDePublicacion(EstadoDePublicacion.EN_REVISION);
    }

    public void agregarPreguntaAdopcion(Pregunta pregunta){
        this.organizacion.agregarPreguntaAdopcion(pregunta);
    }

}
