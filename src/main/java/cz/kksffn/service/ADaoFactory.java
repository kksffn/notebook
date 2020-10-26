package cz.kksffn.service;

import cz.kksffn.dao.ITodoItemDao;


/**
 * Centralization of creating DAOs (==>easy implementation switching)
 */
public abstract class ADaoFactory {
    private static ADaoFactory instance = new HibernateDaoFactory();

    public static ADaoFactory getInstance() {return instance;}

    public abstract ITodoItemDao getTodoItemDao();
}

