package domain.models.entities.publicaciones;

import domain.models.entities.Persistente;

import javax.persistence.*;

@Entity
@Table(name = "respuestaConcreta")
public class RespuestaConcreta extends Persistente {
    // TODO hacemos la prueba sin el atributo de cuestionario constestado -> No funciono
    @ManyToOne(cascade = {CascadeType.ALL})
    private CuestionarioContestado cuestionarioContestado;
    //Atributos
    @ManyToOne //(cascade = {CascadeType.PERSIST})
    private Pregunta pregunta;
    @Column(name = "respuesta")
    private String respuesta;

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public Pregunta getPregunta() {return pregunta; }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setCuestionarioContestado(CuestionarioContestado cuestionarioContestado) {this.cuestionarioContestado = cuestionarioContestado;}

    public CuestionarioContestado getCuestionarioContestado() {return cuestionarioContestado;}
}
