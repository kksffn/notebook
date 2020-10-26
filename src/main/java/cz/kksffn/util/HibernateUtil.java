package cz.kksffn.util;

import java.util.function.Consumer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.cfg.Configuration;


/**
 *
 * @author ivohr
 */
public class HibernateUtil {
    private static final HibernateUtil instance = new HibernateUtil();
    private final SessionFactory sessionFactory;

// A SessionFactory is set up once for an application!
    private HibernateUtil(){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure() // configures settings from hibernate.cfg.xml
			.build();
        
        sessionFactory = new MetadataSources( registry )
                .buildMetadata()
                .buildSessionFactory();
    }
        
    public static Session getSession() {
        return getInstance().sessionFactory.openSession();
    }
    
    private static HibernateUtil getInstance() {
        return instance;
    }
    
    public static void doWithSession(Consumer<Session> command) {
        try(Session session = getSession()) {
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
                
   /*
    static {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    */
    
      
    
    
    /*https://docs.jboss.org/hibernate/orm/5.4/quickstart/html_single/#hibernate-gsg-tutorial-basic-config
    protected void setUp() throws Exception {
	// A SessionFactory is set up once for an application!
	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure() // configures settings from hibernate.cfg.xml
			.build();
	try {
		sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
	}
	catch (Exception e) {
		// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
		// so destroy it manually.
		StandardServiceRegistryBuilder.destroy( registry );
	}
    }
    The setUp method first builds a org.hibernate.boot.registry.StandardServiceRegistry 
    instance which incorporates configuration information into a working set of 
    Services for use by the SessionFactory. In this tutorial we defined all 
    configuration information in hibernate.cfg.xml so there is not much interesting 
    to see here.
    Using the StandardServiceRegistry we create the org.hibernate.boot.MetadataSources 
    which is the start point for telling Hibernate about your domain model. 
    Again, since we defined that in hibernate.cfg.xml so there is not much 
    interesting to see here.
    org.hibernate.boot.Metadata represents the complete, partially validated 
    view of the application domain model which the SessionFactory will be based on.
    The final step in the bootstrap process is to build the SessionFactory. 
    The SessionFactory is a thread-safe object that is instantiated once to serve 
    the entire application.
    The SessionFactory acts as a factory for org.hibernate.Session instances, 
    which should be thought of as a corollary to a "unit of work".
    */
}
