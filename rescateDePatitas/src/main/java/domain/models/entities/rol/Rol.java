package domain.models.entities.rol;

import domain.models.entities.Persistente;
import domain.models.entities.personas.Persona;

import javax.persistence.*;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Rol extends Persistente {
    @Column(name = "tipoDeRol")
    private String tipo;
    @ManyToOne
    private Persona persona;

    public Rol(String rol) {
        this.tipo = rol;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public boolean equals(Rol rol) {
        return this.tipo.equals(rol.tipo);
    }

    @Override
    public int hashCode() {
        return tipo != null ? tipo.hashCode() : 0;
    }
}
