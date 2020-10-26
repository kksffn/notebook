package cz.kksffn.service;

import cz.kksffn.dao.ITodoItemDao;
import cz.kksffn.dao.TodoItemDao;


/**
 * Here we can create Hibernate DAOs
 */
public class HibernateDaoFactory extends ADaoFactory {

    @Override
    public ITodoItemDao getTodoItemDao() { return new TodoItemDao(); }
}
