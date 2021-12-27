package domain.models.repositories;

import domain.models.entities.personas.Usuario;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class RepositorioDeUsuarios extends RepositorioGenerico<Usuario> {
    private static RepositorioDeUsuarios instancia;

    public static RepositorioDeUsuarios getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioDeUsuarios(new DAOHibernate<>());
        }
        return instancia;
    }

    public RepositorioDeUsuarios(DAO<Usuario> dao) {
        super(dao);
    }

    public Boolean existe(String nombreDeUsuario) {
        return buscarUsuario(nombreDeUsuario) != null;
    }

    public Usuario dameElUsuario(String nombreDeUsuario) {
        return buscarUsuario(nombreDeUsuario);
    }

    public Usuario buscarUsuario(String nombreDeUsuario) {
        return this.dao.buscar(condicionUsuario(nombreDeUsuario));
    }

    private BusquedaCondicional condicionUsuario(String nombreDeUsuario) {
        CriteriaBuilder criteriaBuilder = criteriaBuilder();
        CriteriaQuery<Usuario> usuarioQuery = criteriaBuilder.createQuery(Usuario.class);

        Root<Usuario> condicionRaiz = usuarioQuery.from(Usuario.class);

        Predicate condicionNombreDeUsuario = criteriaBuilder.equal(condicionRaiz.get("nombreDeUsuario"), nombreDeUsuario);

        Predicate condicionExisteUsuario = criteriaBuilder.and(condicionNombreDeUsuario);

        usuarioQuery.where(condicionExisteUsuario);

        return new BusquedaCondicional(null, usuarioQuery);
    }

    public Usuario buscarUsuario(int usuario_id){

        return new Usuario();
    }
}
