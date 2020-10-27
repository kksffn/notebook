package cz.kksffn.service.interfaces;

import cz.kksffn.dao.interfaces.ICategoryDao;
import cz.kksffn.dao.interfaces.ITodoItemDao;
import cz.kksffn.service.HibernateDaoFactory;


/**
 * Centralization of creating DAOs (==>easy implementation switching)
 */
public abstract class ADaoFactory {
    private static ADaoFactory instance = new HibernateDaoFactory();

    public static ADaoFactory getInstance() {return instance;}

    public abstract ITodoItemDao getTodoItemDao();
    public abstract ICategoryDao getCategoryDao();
}

