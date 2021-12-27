package domain.models.repositories.factories;

import config.Config;
import domain.models.entities.mascotas.Mascota;
import domain.models.repositories.RepositorioDeMascotas;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;
import domain.models.repositories.daos.DAOMemoria;
import domain.models.repositories.testMemoData.Data;

public class FactoryRepositorioMascotas {
    private static RepositorioDeMascotas repo;

    static {
        repo = null;
    }

    public static RepositorioDeMascotas get(){
        if(repo == null){
            if(Config.useDataBase){
                DAO<Mascota> dao = new DAOHibernate<>(Mascota.class);
                repo = new RepositorioDeMascotas(dao);
            }
            else{
                repo = new RepositorioDeMascotas(new DAOMemoria<>(Data.getData(Mascota.class)));
            }
        }
        return repo;
    }
}
