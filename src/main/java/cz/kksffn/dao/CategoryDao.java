package cz.kksffn.dao;

import cz.kksffn.dao.interfaces.ICategoryDao;
import cz.kksffn.model.Category;
import cz.kksffn.util.JPASessionUtil;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDao implements ICategoryDao {

//===================================CRUD===============================================================================
//read==================================================================================================================

    @Override
    public List<Category> readAll() {
        List<Category> categories = new ArrayList<>();
        JPASessionUtil.doWithEntityManager(em-> {
            List<Category> list = em.createQuery("from Category", Category.class).getResultList();
            categories.addAll(list);
        });
        return categories;
    }
    @Override
    public Optional<Category> readById(Long id) {
        final Category[] category = {null};
        JPASessionUtil.doWithEntityManager(em-> {
            Category foundCategoryItem = em.find(Category.class, id);
            category[0] = foundCategoryItem;
        });
        return Optional.ofNullable(category[0]);
    }
//create================================================================================================================
    @Override
    public Long create(Category category) throws ConstraintViolationException {
        JPASessionUtil.doWithEntityManager(em-> {
            em.persist(category);
        });
        return category.getId();
    }
//update================================================================================================================
    @Override
    public Long update(Category category) throws ConstraintViolationException, NoResultException {
        JPASessionUtil.doWithEntityManager(em-> {
            Optional<Category> entity = readById(category.getId());
            if (entity.isPresent()) {
                //The entity exists in DB, so we get it and set attributes to fit item attribute
                Category categoryToUpdate = entity.get();
                categoryToUpdate.setTag(category.getTag());
                //Update entity in DB
                em.merge(categoryToUpdate);
            }else {
                throw new NoResultException();
            }
        });
        return category.getId();
    }
//delete================================================================================================================
    @Override
    public void delete(Category category) throws NoResultException {
        JPASessionUtil.doWithEntityManager(em->{
            Optional<Category> entity = readById(category.getId());
            if (entity.isPresent()) {
                Category categoryToDelete = entity.get();
//POZOR!!! Následující řádek je důležitý, protože:
                //remove works only on entities which are managed in the current transaction/context... If you're
                // retrieving the entity in an earlier transaction, (storing it in the HTTP session) and then attempting
                // to remove it in a different transaction... this doesn't work!!!
                //https://stackoverflow.com/questions/17027398/java-lang-illegalargumentexception-removing-a-detached-instance-com-test-user5
                em.remove(em.contains(categoryToDelete) ? categoryToDelete : em.merge(categoryToDelete));
            }else{
                throw new NoResultException();
            }
        });
    }
}
