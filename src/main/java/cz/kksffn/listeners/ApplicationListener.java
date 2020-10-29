package cz.kksffn.listeners;

import cz.kksffn.util.JPASessionUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The application needs to close entityManagerFactory after it's beeing destroyed!
 * @author ivohr
 */
public class ApplicationListener implements ServletContextListener{

    private static Logger logger = LoggerFactory.getLogger(ApplicationListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Application is beeing initialized...");
        ServletContextListener.super.contextInitialized(sce);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Application is beeing destroyed...");
        JPASessionUtil.closeEMF();
        ServletContextListener.super.contextDestroyed(sce);
    }
}
