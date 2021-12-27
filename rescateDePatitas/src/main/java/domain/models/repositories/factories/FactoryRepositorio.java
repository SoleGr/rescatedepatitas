package domain.models.repositories.factories;

import config.Config;
import domain.models.repositories.RepositorioGenerico;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;
import domain.models.repositories.daos.DAOMemoria;
import domain.models.repositories.testMemoData.Data;

import java.util.HashMap;

public class FactoryRepositorio {

    private static HashMap<String, RepositorioGenerico> repos;

    static {
        repos = new HashMap<>();
    }

    public static <T> RepositorioGenerico<T> get(Class<T> type) {
        RepositorioGenerico<T> repo;
        if (repos.containsKey(type.getName())) {
            repo = repos.get(type.getName());
        } else {
            if (Config.useDataBase) {
                DAO<T> dao = new DAOHibernate<>(type);
                repo = new RepositorioGenerico<T>(dao);
            } else {
                repo = new RepositorioGenerico<T>(new DAOMemoria<>(Data.getData(type)));
            }
            repos.put(type.toString(), repo);
        }
        return repo;
    }


}
