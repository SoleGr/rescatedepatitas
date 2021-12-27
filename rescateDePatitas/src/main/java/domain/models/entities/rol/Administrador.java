package domain.models.entities.rol;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "administrador")
public class Administrador extends Rol{
    public Administrador(){
        super("Administrador");
    }
}
