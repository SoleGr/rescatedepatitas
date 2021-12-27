package domain.models.entities.mascotas;

import domain.models.entities.Persistente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ubicacion")
public class Lugar extends Persistente {
    @Column(name = "latitud")
    private double latitud;
    @Column(name = "longitud")
    private double longitud;

    public Lugar(){

    }

    public Lugar(double latitud, double longitud) {
        setLatitud(latitud);
        setLongitud(longitud);
    }

    public double getLatitud() {
        return this.latitud;
    }

    public double getLongitud() {
        return this.longitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
