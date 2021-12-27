package domain.models.entities.mascotas;

import domain.models.entities.Persistente;
import domain.models.entities.publicaciones.PublicacionMascotaEncontrada;
import services.EditorDeFotos;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "foto")
public class Foto extends Persistente {
    @ManyToOne
    private Mascota mascota;
    @ManyToOne
    private DatosMascotaEncontrada datosMascotaEncontrada;
    @ManyToOne
    private PublicacionMascotaEncontrada publicacionMascotaEncontrada;
    // Atributos
    @Column(name = "urlFoto")
    private String URLfoto;
    @Column(name = "fecha", columnDefinition = "DATE")
    private LocalDate fecha;

    public Foto(){
        this.fecha = LocalDate.now();
    }

    public Foto(String url){
        this.fecha = LocalDate.now();
        this.URLfoto = url;
    }

    public Foto editarFoto() {
        EditorDeFotos editor = new EditorDeFotos();
        return editor.ajustarCalidad(this);
    }

    public String getURLfoto() {
        return URLfoto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setURLfoto(String URLfoto) {
        this.URLfoto = URLfoto;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public void setDatosMascotaEncontrada(DatosMascotaEncontrada datosMascotaEncontrada) {
        this.datosMascotaEncontrada = datosMascotaEncontrada;
    }
}
