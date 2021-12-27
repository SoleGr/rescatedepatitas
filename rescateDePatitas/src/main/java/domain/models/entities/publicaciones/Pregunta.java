package domain.models.entities.publicaciones;

import domain.models.entities.Persistente;
import domain.models.entities.mascotas.Organizacion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pregunta")
public class Pregunta extends Persistente {
    @ManyToOne
    private Organizacion organizacion;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Cuestionario cuestionario;
    // TODO que pasa si lo borramos? -> Hicimos una prueba con Cuestionario Contestado
    @OneToMany(mappedBy = "pregunta", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<RespuestaConcreta> respuestasConcretas;
    // Atributos
    @Column(name = "pregunta")
    private String pregunta;
    @Column(name = "preguntaMascota")
    private String preguntaMascota;
    @ElementCollection
    //Se genera la tabla pregunta_respuestas
    private List<String> respuestas;
    @Column(name = "tipoDePregunta")
    private String tipoDePregunta;
    @Column(name = "visible")
    private Boolean visible;

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public List<RespuestaConcreta> getRespuestasConcretas() {
        return respuestasConcretas;
    }

    public void setRespuestasConcretas(List<RespuestaConcreta> respuestasConcretas) {
        this.respuestasConcretas = respuestasConcretas;
    }

    public Pregunta() {
        this.respuestas = new ArrayList<String>();
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getTipoDePregunta() {
        return tipoDePregunta;
    }

    public void setTipoDePregunta(String tipoDePregunta) {
        this.tipoDePregunta = tipoDePregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<String> respuestas) {
        this.respuestas = respuestas;
    }

    public void agregarRespuesta(String respuesta) {
        respuestas.add(respuesta);
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getVisible() {
        return visible;
    }

    public String getPreguntaMascota() {
        return preguntaMascota;
    }

    public void setPreguntaMascota(String preguntaMascota) {
        this.preguntaMascota = preguntaMascota;
    }
}
