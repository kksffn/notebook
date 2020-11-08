package cz.kksffn.notebook.dao.interfaces;

import cz.kksffn.notebook.model.interfaces.IEntity;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public interface IDao<T extends IEntity> {

    Optional<T> readById(Long id);

    List<T> readAll();

    Long create(T entity) throws ConstraintViolationException;

    Long update(T entity) throws ConstraintViolationException, NoResultException;

    void delete(T entity) throws NoResultException;
}
