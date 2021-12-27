package domain.models.repositories;

import domain.models.entities.publicaciones.Pregunta;
import domain.models.repositories.daos.DAO;
import domain.models.repositories.daos.DAOHibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class RepositorioDePreguntas extends RepositorioGenerico<Pregunta> {
    private static RepositorioDePreguntas instancia;

    public static RepositorioDePreguntas getInstancia() {
        if (instancia == null) {
            instancia=new RepositorioDePreguntas(new DAOHibernate<>());
        }
        return instancia;
    }

    public RepositorioDePreguntas(DAO<Pregunta> dao) {
        super(dao);
    }

    public List<Pregunta> buscarPorTipo(String tipo) {
        return this.dao.buscarPorTipo(condicionTipo(tipo));
    }

    private BusquedaCondicional condicionTipo(String tipo) {
        CriteriaBuilder criteriaBuilder = criteriaBuilder();
        CriteriaQuery<Pregunta> preguntaQuery = criteriaBuilder.createQuery(Pregunta.class);

        Root<Pregunta> condicionRaiz = preguntaQuery.from(Pregunta.class);

        Predicate condicionTipo = criteriaBuilder.equal(condicionRaiz.get("tipoDePregunta"), tipo);

        preguntaQuery.where(condicionTipo);

        return new BusquedaCondicional(null, preguntaQuery);
    }

    public Pregunta buscarPregunta(String pregunta) {
        return this.dao.buscar(condicionPregunta(pregunta));
    }

    private BusquedaCondicional condicionPregunta(String pregunta) {
        CriteriaBuilder criteriaBuilder = criteriaBuilder();
        CriteriaQuery<Pregunta> preguntaQuery = criteriaBuilder.createQuery(Pregunta.class);

        Root<Pregunta> condicionRaiz = preguntaQuery.from(Pregunta.class);

        Predicate condicionTipo = criteriaBuilder.equal(condicionRaiz.get("pregunta"), pregunta);

        preguntaQuery.where(condicionTipo);

        return new BusquedaCondicional(null, preguntaQuery);
    }


}
