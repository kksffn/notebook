package cz.kksffn.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ivohr
 */
public class JPASessionUtil {
    private static Map<String, EntityManagerFactory> persistenceUnits = new HashMap<>();
    //@SuppressWarnings("WeakerAccess")
    public static synchronized EntityManager getEntityManager(String persistenceUnitName) {
        persistenceUnits.putIfAbsent(persistenceUnitName,
            Persistence.createEntityManagerFactory(persistenceUnitName));
        return persistenceUnits.get(persistenceUnitName)
            .createEntityManager();
    }
    
    public static Session getSession(String persistenceUnitName) {
        return getEntityManager(persistenceUnitName).unwrap(Session.class);
    }
    
    public static void doWithEntityManager(Consumer<EntityManager> command) {
        EntityManager em = JPASessionUtil.getEntityManager("cz.kksffn.notebook");

        //What about try catch?????????????????????????????
        em.getTransaction().begin();
        command.accept(em);
        if (em.getTransaction().isActive() &&
            !em.getTransaction().getRollbackOnly()) {
                    em.getTransaction().commit();
        } else {
            em.getTransaction().rollback();
        }

        em.close();
    }

    public static void doWithSession(Consumer<Session> command) {
        try(Session session = getSession("cz.kksffn.notebook")) {
            Transaction tx = session.beginTransaction();
            command.accept(session);
            if (tx.isActive() &&
                    !tx.getRollbackOnly()) {
                tx.commit();
            } else {
                tx.rollback();
            }
        }
    }

}
