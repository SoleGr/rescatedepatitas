package domain.models.entities.publicaciones;

import domain.models.entities.mascotas.DatosMascotaEncontrada;
import domain.models.entities.mascotas.Foto;
import domain.models.entities.mascotas.Lugar;
import domain.models.entities.mascotas.Organizacion;
import domain.models.entities.personas.Persona;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "publicacion_mascota_encontrada")
public class PublicacionMascotaEncontrada extends PublicacionGenerica{
    @OneToOne(cascade = CascadeType.ALL)
    private DatosMascotaEncontrada datosMascotaEncontrada;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Organizacion organizacion;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Persona rescatista;

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion){
        this.organizacion = organizacion;
    }

    public Persona getRescatista() {
        return rescatista;
    }

    public void setRescatista(Persona rescatista) {
        this.rescatista = rescatista;
    }

    public DatosMascotaEncontrada getDatosMascotaEncontrada() {
        return datosMascotaEncontrada;
    }

    public void setDatosMascotaEncontrada(DatosMascotaEncontrada datosMascotaEncontrada) {
        this.datosMascotaEncontrada = datosMascotaEncontrada;
    }
}
