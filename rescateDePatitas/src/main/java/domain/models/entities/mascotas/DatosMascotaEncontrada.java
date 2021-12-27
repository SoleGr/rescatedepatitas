package domain.models.entities.mascotas;

import domain.models.entities.Persistente;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "datos_mascota_encontrada")
public class DatosMascotaEncontrada extends Persistente {
    @OneToMany(mappedBy = "datosMascotaEncontrada", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Foto> fotos;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToOne(cascade = CascadeType.ALL)
    private Lugar lugar;

    public DatosMascotaEncontrada() {
    }

    public DatosMascotaEncontrada(List<Foto> fotos, String descripcion, Lugar lugar) {
        setFotos(fotos);
        setDescripcion(descripcion);
        setLugar(lugar);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

}
