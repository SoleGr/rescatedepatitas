package domain.models.entities.mascotas;

import com.google.zxing.WriterException;
import domain.models.entities.publicaciones.GestorDePublicaciones;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rescatista;
import services.Configuracion;
import services.GeneradorQR;
import domain.models.entities.Persistente;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mascota")
public class Mascota extends Persistente {
    @ManyToOne
    private Duenio duenio;
    @ManyToOne
    private Rescatista rescatista;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apodo")
    private String apodo;
    @Transient
    private static Integer codigo = 0; // TODO Ya no es necesario?
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "edad")
    private Integer edad;
    @Column(name = "especie")
    private String especie;
    @Column(name = "genero")
    private String genero;
    @OneToOne
    @JoinColumn(name = "organizacion_id", referencedColumnName = "id")
    private Organizacion organizacion;
    @OneToMany(mappedBy = "mascota", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<CaracteristicaConRta> caracteristicas;
    @OneToMany(mappedBy = "mascota", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Foto> fotos;
    @ManyToOne
    private Persona persona;
    private String tamanio;

    public Mascota(){}

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setDuenio(Duenio duenio) {
        this.duenio = duenio;
    }

    public void setRescatista(Rescatista rescatista) {
        this.rescatista = rescatista;
    }

    public Mascota(Persona persona) {
        this.caracteristicas = new ArrayList<>();
        this.fotos = new ArrayList<>();
        //this.idMascota = getIdMascota() + 1;
        this.persona = persona;
        this.fotos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public Integer getIdMascota() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getEspecie() {
        return especie;
    }

    public String getGenero() {
        return genero;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public List<CaracteristicaConRta> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(List<CaracteristicaConRta> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }

    public void agregarFoto(Foto foto) {
        fotos.add(foto);
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public void generarQR() throws IOException, WriterException {

        String url = Configuracion.leerPropiedad("url") + "/" + codigo.toString();

        String pathGuardar = Configuracion.leerPropiedad("pathQR") + codigo.toString() + ".jpg";

        GeneradorQR.generarQR(url, pathGuardar, "jpg", 500, 500);
    }

    public void avisarQueMePerdi() {
        GestorDePublicaciones gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionMascotaPerdida(this);
    }

    public void avisarQueMeEcontraron(Contacto contacto, DatosMascotaEncontrada datos) {
        this.persona.notificarContactos(this, contacto, datos);
    }

    public void inicializar(MascotaDTO mascota) {
        this.apodo = mascota.apodo;
        this.nombre = mascota.nombre;
        this.edad = mascota.edad;
        this.descripcion = mascota.descripcion;
        this.especie = mascota.especie;
        this.genero = mascota.genero;
        this.caracteristicas = mascota.caracteristicas;
        this.fotos = mascota.fotos;
        this.persona = mascota.persona;
    }

    public List<Foto> redimensionarFotos(List<Foto> fotosOriginales) {
        fotosOriginales.forEach(Foto::editarFoto);
        return fotosOriginales;
    }

    public void meQuiereAdoptar(Persona adoptante) {
        this.persona.notificarPosibleAdopcion(this, adoptante);
    }


    /////Aca comienza el DTO/////
    public static class MascotaDTO {
        private String nombre;
        private String apodo;
        private static Integer idMascota = 0;
        private String descripcion;
        private Integer edad;
        private String especie;
        private String genero;
        private List<CaracteristicaConRta> caracteristicas;
        private List<Foto> fotos;
        private Persona persona;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApodo() {
            return apodo;
        }

        public void setApodo(String apodo) {
            this.apodo = apodo;
        }

        public static Integer getIdMascota() {
            return idMascota;
        }

        public static void setIdMascota(Integer idMascota) {
            MascotaDTO.idMascota = idMascota;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public Integer getEdad() {
            return edad;
        }

        public void setEdad(Integer edad) {
            this.edad = edad;
        }

        public String getEspecie() {
            return especie;
        }

        public void setEspecie(String especie) {
            this.especie = especie;
        }

        public String getGenero() {
            return genero;
        }

        public void setGenero(String genero) {
            this.genero = genero;
        }

        public List<CaracteristicaConRta> getCaracteristicas() {
            return caracteristicas;
        }

        public void setCaracteristicas(List<CaracteristicaConRta> caracteristicas) {
            this.caracteristicas = caracteristicas;
        }

        public List<Foto> getFotos() {
            return fotos;
        }

        public void setFotos(List<Foto> fotos) {
            this.fotos = fotos;
        }

        public Persona getPersona() {
            return persona;
        }

        public void setPersona(Persona persona) {
            this.persona = persona;
        }

        public void inicializar(Persona persona, String nombre, String apodo, Integer edad, String descripcion,
                                String especie, String genero, List<CaracteristicaConRta> caracteristicas,
                                List<Foto> fotos) {
            this.apodo = apodo;
            this.nombre = nombre;
            this.edad = edad;
            this.descripcion = descripcion;
            this.especie = especie;
            this.genero = genero;
            this.caracteristicas = caracteristicas;
            this.fotos = fotos;
            this.persona = persona;
        }
    }

}
