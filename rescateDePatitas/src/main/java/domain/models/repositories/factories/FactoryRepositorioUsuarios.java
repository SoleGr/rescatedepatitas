package domain.models.repositories.factories;

import config.Config;
import domain.models.entities.personas.Usuario;
import domain.models.repositories.RepositorioDeUsuarios;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;
import domain.models.repositories.daos.DAOMemoria;
import domain.models.repositories.testMemoData.Data;

public class FactoryRepositorioUsuarios {
    private static RepositorioDeUsuarios repo;

    static {
        repo = null;
    }

    public static RepositorioDeUsuarios get(){
        if(repo == null){
            if(Config.useDataBase){
                DAO<Usuario> dao = new DAOHibernate<>(Usuario.class);
                repo = new RepositorioDeUsuarios(dao);
            }
            else{
                repo = new RepositorioDeUsuarios(new DAOMemoria<>(Data.getData(Usuario.class)));
            }
        }
        return repo;
    }
}
