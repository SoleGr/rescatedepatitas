package domain.models.repositories;


import domain.models.entities.mascotas.Organizacion;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class RepositorioDeOrganizaciones extends RepositorioGenerico<Organizacion> {
    private static RepositorioDeOrganizaciones instancia;

    public static RepositorioDeOrganizaciones getInstancia() {
        if (instancia == null) {
            instancia=new RepositorioDeOrganizaciones(new DAOHibernate<>());
        }
        return instancia;
    }
    public RepositorioDeOrganizaciones(DAO<Organizacion> dao) {
        super(dao);
    }

    public Organizacion buscarPorNombre(String pregunta) {
        return this.dao.buscar(condicionPregunta(pregunta));
    }


    private BusquedaCondicional condicionPregunta(String nombre) {
        CriteriaBuilder criteriaBuilder = criteriaBuilder();
        CriteriaQuery<Organizacion> preguntaQuery = criteriaBuilder.createQuery(Organizacion.class);

        Root<Organizacion> condicionRaiz = preguntaQuery.from(Organizacion.class);

        Predicate condicionTipo = criteriaBuilder.equal(condicionRaiz.get("nombre"), nombre);

        preguntaQuery.where(condicionTipo);

        return new BusquedaCondicional(null, preguntaQuery);
    }
}
