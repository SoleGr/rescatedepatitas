package domain.models.entities.publicaciones;

import domain.models.entities.Persistente;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cuestionario_contestado")
public class CuestionarioContestado extends Persistente {
    @OneToMany(mappedBy = "cuestionarioContestado", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<RespuestaConcreta> respuestas;

    public void setRespuestas(List<RespuestaConcreta> respuestas) {
        this.respuestas = respuestas;
    }

    public void agregarRespuestas(List<RespuestaConcreta> listaRespuestas ){
        respuestas.addAll(listaRespuestas);
    }

    public List<RespuestaConcreta> getRespuestas() {
        return respuestas;
    }
}
