package domain.models.entities.publicaciones;

import domain.models.entities.Persistente;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cuestionario")
public class Cuestionario extends Persistente {
    @OneToMany(mappedBy = "cuestionario", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Pregunta> preguntas;

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }
}
