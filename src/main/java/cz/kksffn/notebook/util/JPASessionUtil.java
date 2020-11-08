package cz.kksffn.notebook.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ivohr
 */
public class JPASessionUtil {


    private static final String PUNAME = "cz.kksffn.notebook"; 
    private static Map<String, EntityManagerFactory> persistenceUnits = new HashMap<>();
    private static Logger logger = LoggerFactory.getLogger(JPASessionUtil.class);


    public static synchronized EntityManager getEntityManager(String persistenceUnitName) {
        logger.debug("Getting Entity Manager============================" + persistenceUnits.size());
        persistenceUnits.putIfAbsent(persistenceUnitName,
            Persistence.createEntityManagerFactory(persistenceUnitName));
        return persistenceUnits.get(persistenceUnitName)
            .createEntityManager();
    }
    
    public static Session getSession(String persistenceUnitName) {
        return getEntityManager(persistenceUnitName).unwrap(Session.class);
    }
    
    public static void doWithEntityManager(Consumer<EntityManager> command) {
        EntityManager em = JPASessionUtil.getEntityManager(PUNAME);
        logger.debug("In doWithEntityManager method...");
        try {
            em.getTransaction().begin();
            command.accept(em);
            if (em.getTransaction().isActive() &&
                    !em.getTransaction().getRollbackOnly()) {
                em.getTransaction().commit();
            }
        }catch (Exception e) {
            logger.error(String.valueOf(e));
             try {
                logger.error("Exception! Trying to rollback!");
                em.getTransaction().rollback();
             }catch(Exception e2) {
                 logger.error("Didn't manage to rollback!!" + String.valueOf(e2));
             }
        }finally {
            em.close();
        }
    }
    public static void doWithSession(Consumer<Session> command) {
        try(Session session = getSession(PUNAME)) {
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
    public static void closeEMF(){
        try{
            EntityManagerFactory emf = persistenceUnits.get(PUNAME);
        emf.close();
        }catch(Exception e){
            logger.error("Exception while trying to close EMF!!");
        }        
    }

}
