package domain.models.repositories.daos;

import db.EntityManagerHelper;
import domain.models.repositories.BusquedaCondicional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class DAOHibernate<T> implements DAO<T> {
    private Class<T> type;

    public DAOHibernate(Class<T> type) {
        this.type = type;
    }

    public DAOHibernate() {

    }

    @Override
    public List<T> buscarTodos() {
        CriteriaBuilder builder = EntityManagerHelper.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(this.type);
        criteria.from(type);
        List<T> entities = EntityManagerHelper.getEntityManager().createQuery(criteria).getResultList();
        return entities;
    }

    @Override
    public T buscar(int id) {
        return EntityManagerHelper.getEntityManager().find(type, id);
    }

    @Override
    public void agregar(Object unObjeto) {
        EntityManagerHelper.getEntityManager().getTransaction().begin();
        EntityManagerHelper.getEntityManager().persist(unObjeto);
        EntityManagerHelper.getEntityManager().getTransaction().commit();
    }

    @Override
    public void modificar(Object unObjeto) {
        EntityManagerHelper.getEntityManager().getTransaction().begin();
        EntityManagerHelper.getEntityManager().merge(unObjeto);
        EntityManagerHelper.getEntityManager().getTransaction().commit();
    }

    @Override
    public void eliminar(Object unObjeto) {
        EntityManagerHelper.getEntityManager().getTransaction().begin();
        EntityManagerHelper.getEntityManager().remove(unObjeto);
        EntityManagerHelper.getEntityManager().getTransaction().commit();
    }

    @Override
    public T buscar(BusquedaCondicional condicional) {
        try {
            return (T) EntityManagerHelper.getEntityManager()
                    .createQuery(condicional.getCondicionCritero())
                    .getSingleResult();
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<T> buscarPorTipo(BusquedaCondicional condicional) {
        try {
            return (List<T>) EntityManagerHelper.getEntityManager()
                    .createQuery(condicional.getCondicionCritero())
                    .getResultList();
        }
        catch (Exception e) {
            return null;
        }
    }

}