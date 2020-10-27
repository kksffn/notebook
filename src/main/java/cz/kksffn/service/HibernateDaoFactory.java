package cz.kksffn.service;

import cz.kksffn.dao.CategoryDao;
import cz.kksffn.dao.interfaces.ICategoryDao;
import cz.kksffn.dao.interfaces.ITodoItemDao;
import cz.kksffn.dao.TodoItemDao;
import cz.kksffn.service.interfaces.ADaoFactory;


/**
 * Here we can create Hibernate DAOs
 */
public class HibernateDaoFactory extends ADaoFactory {

    @Override
    public ITodoItemDao getTodoItemDao() { return new TodoItemDao(); }

    @Override
    public ICategoryDao getCategoryDao() { return new CategoryDao(); }
}
