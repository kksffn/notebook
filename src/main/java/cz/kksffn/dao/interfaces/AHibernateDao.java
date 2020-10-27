package cz.kksffn.dao.interfaces;

import cz.kksffn.model.interfaces.IEntity;
import cz.kksffn.util.JPASessionUtil;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AHibernateDao {//implements IDao{

    private Class persistentClass;

    public AHibernateDao(Class persistenceClass) {
        this.persistentClass = persistenceClass;
    }

//    @Override
    public Optional<IEntity> readById(Long id) {
        final IEntity[] item = {null};
        JPASessionUtil.doWithEntityManager( em ->{
            IEntity foundItem = (IEntity) em.find(persistentClass, id);
            item[0] = foundItem;
        });
        return Optional.of(item[0]);
    }

//    @Override
    public List readAll() {
        List items = new ArrayList<>();
        JPASessionUtil.doWithEntityManager(em-> {
            List list = em.createQuery("from "+persistentClass.getName(), persistentClass).getResultList();
            items.addAll(list);
        });
        return items;
    }

//    @Override
    public Long create(IEntity entity) throws ConstraintViolationException {
        JPASessionUtil.doWithEntityManager(em-> {
            em.persist(entity);
        });
        return entity.getId();
    }

}
