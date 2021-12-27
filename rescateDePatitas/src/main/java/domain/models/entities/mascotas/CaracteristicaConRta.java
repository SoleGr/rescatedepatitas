package domain.models.entities.mascotas;

import domain.models.entities.Persistente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "caracteristica_mascota")
public class CaracteristicaConRta extends Persistente {
    @ManyToOne
    private Mascota mascota;
    // Atributos
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "respuestaElegida")
    private String respuestaElegida;

    public CaracteristicaConRta(String descripcion, String respuestaElegida) {
        this.descripcion = descripcion;
        this.respuestaElegida = respuestaElegida;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public void setRespuestaElegida(String respuestaElegida) {
        this.respuestaElegida = respuestaElegida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRespuestaElegida() {
        return this.respuestaElegida;
    }

    public Mascota getMascota() {
        return mascota;
    }
}
