package cz.kksffn.dao;

import cz.kksffn.model.IEntity;
import cz.kksffn.model.TodoItem;
import cz.kksffn.util.JPASessionUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/*
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
*/

/**
 *
 * @author ivohr
 */
public class TodoItemDao implements ITodoItemDao{
/** Přes SessionFactory------------------------------------------------------------
 try (Session session = HibernateUtil.getSession()) {
 items = session.createQuery("from TodoItem", TodoItem.class).list();

 //Při FetchType.LAZY by stačilo dát:
 //, což ale vytvoří n+1 issues!
 //for (TodoItem item : items) {
 //    item.getCategories().size();
 //}

 //items
 //    .forEach(item -> {
 //        Set<Category> set = item.getCategories();
 //        Iterator<Category> iterator = set.iterator();
 //        while (iterator.hasNext()) {
 //            System.out.println("Category: " + iterator.next().getTag());
 //        }
 //    });
 session.close();
 }catch (Exception e) {
 e.printStackTrace();
 }
 */
/** Přes EntityManager: ---------------------------------------------------------
         EntityManager em = JPASessionUtil.getEntityManager("cz.kksffn.notebook");
            Session session = JPASessionUtil.getSession("cz.kksffn.notebook");

        try{
            //em.getTransaction().begin();
            Query<TodoItem> query = session.createQuery("from TodoItem", TodoItem.class);
            session.enableFilter("fulfilled").setParameter("fulfilled", fulfilled);
            items = query.list();
            //SELECT i.`*`, GROUP_CONCAT(c.tag SEPARATOR ', ') AS tag FROM todo_item i
            //JOIN todo_item_category tc ON (i.id = tc.item_id)
            //JOIN  category c ON (c.id = tc.category_id)
            //WHERE i.id = 1
            //GROUP BY i.id
            //em.getTransaction().commit();
        }catch(Exception e) {
            System.out.println("Something went wrong in Matrix");
        }finally{
            em.close();
        }
 */

    //CRUD--------------------------------------------------------------------------------------------------------------
    //read--------------------------------------------------------------

    /**
     * Get all items from DB
     * @return List of items from DB
     */
    @Override
    public List<TodoItem> readAll() {
        List<TodoItem> items = new ArrayList<>();
        JPASessionUtil.doWithEntityManager(em-> {
            List<TodoItem> list = em.createQuery("from TodoItem", TodoItem.class).getResultList();
            items.addAll(list);
        });
        return items;
    }

    /**
     * Get only fulfilled/unfulfilled items from DB
     * @param fulfilled
     * @return
     */
    public List<TodoItem> readAll(boolean fulfilled) {
        List<TodoItem> items = new ArrayList<>();

        JPASessionUtil.doWithSession(session-> {
            Query<TodoItem> query = session.createQuery("from TodoItem", TodoItem.class);
            session.enableFilter("fulfilled").setParameter("fulfilled", fulfilled);

            items.addAll(query.list());
        });
        return items;
    }

    /**
     * Get item from DB by id
     * @param id
     * @return
     */
    public Optional<TodoItem> readById(Long id) {
        //AtomicReference<TodoItem> item = new AtomicReference<>();
        final TodoItem[] item = {null};
        JPASessionUtil.doWithEntityManager(em-> {
                    TodoItem foundItem = em.find(TodoItem.class, id);
                    item[0] = foundItem;
                });
        return Optional.ofNullable(item[0]);
   }

    /**
     * Create item in DB
     * @param item
     * @throws ConstraintViolationException
     */
    //create-------------------------------------------------------------
    @Override
    public Long create(TodoItem item) throws ConstraintViolationException {
        JPASessionUtil.doWithEntityManager(em-> {
            em.persist(item);
        });
        return item.getId();
    }

    /**
     * Update TodoItem item in DB if it exists
     * @param item
     * @throws ConstraintViolationException
     * @throws NoResultException
     */
    //update-------------------------------------------------------------
    @Override
    public Long update(TodoItem item) throws ConstraintViolationException, NoResultException {
        JPASessionUtil.doWithEntityManager(em-> {
            Optional<TodoItem> entity = readById(item.getId());
            if (entity.isPresent()) {
                //The entity exists in DB, so we get it and set attributes to fit item attribute
                TodoItem todoToUpdate = entity.get();
                if (item.getAuthor() != null)  todoToUpdate.setAuthor(item.getAuthor());
                if (item.getCategories() != null) todoToUpdate.setCategories(item.getCategories());
                if (item.getDeadline() != null) todoToUpdate.setDeadline(item.getDeadline());
                todoToUpdate.setFulfilled(item.getFulfilled());
                if (item.getText() != null) todoToUpdate.setText(item.getText());
                //Update entity in DB
                em.merge(todoToUpdate);
            }else {
                throw new NoResultException();
            }
        });
        return item.getId();
    }

    /**
     * Delete TodoItem from DB if it exists
     * @param item
     * @throws NoResultException
     */
    @Override
    public void delete(TodoItem item) throws NoResultException {
        JPASessionUtil.doWithEntityManager(em->{
            Optional<TodoItem> entity = readById(item.getId());
            if (entity.isPresent()) {
                TodoItem itemToDelete = entity.get();
                System.out.println("Item id = " + itemToDelete.getId() + " found and will be deleed!!!!");
//POZOR!!! Následující řádek je důležitý, protože:
                //remove works only on entities which are managed in the current transaction/context... If you're
                // retrieving the entity in an earlier transaction, (storing it in the HTTP session) and then attempting
                // to remove it in a different transaction... this doesn¨t work!!!
                //https://stackoverflow.com/questions/17027398/java-lang-illegalargumentexception-removing-a-detached-instance-com-test-user5

                em.remove(em.contains(itemToDelete) ? itemToDelete : em.merge(itemToDelete));
                System.out.println("Deleted");
            }else{
                System.out.println("Not deleted!!!");
                throw new NoResultException();
            }
        });
    }
}