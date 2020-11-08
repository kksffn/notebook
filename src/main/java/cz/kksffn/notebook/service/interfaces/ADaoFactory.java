package cz.kksffn.notebook.service.interfaces;

import cz.kksffn.notebook.dao.interfaces.ICategoryDao;
import cz.kksffn.notebook.dao.interfaces.ITodoItemDao;
import cz.kksffn.notebook.service.HibernateDaoFactory;


/**
 * Centralization of creating DAOs (==>easy implementation switching)
 */
public abstract class ADaoFactory {
    private static ADaoFactory instance = new HibernateDaoFactory();

    public static ADaoFactory getInstance() {return instance;}

    public abstract ITodoItemDao getTodoItemDao();
    public abstract ICategoryDao getCategoryDao();
}

