package domain.models.repositories;

import domain.models.entities.mascotas.Mascota;
import domain.models.repositories.daos.DAO;
import java.util.List;
import java.util.Optional;

public class RepositorioDeMascotas extends RepositorioGenerico<Mascota>{
    public List<Mascota> mascotas;

    //TODO: ESTO ESTABA COMO PROTECTED, LO CAMBIE PARA PODER CREAR EL REPO EN EL CONTROLER DE PUBLICACIONES EN ADOPCION. RECORDAR POR SI ROMPE.
    public RepositorioDeMascotas(DAO<Mascota> dao) {
        super(dao);
    }

}
