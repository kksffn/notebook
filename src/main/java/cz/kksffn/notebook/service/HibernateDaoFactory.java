package cz.kksffn.notebook.service;

import cz.kksffn.notebook.dao.CategoryDao;
import cz.kksffn.notebook.dao.interfaces.ICategoryDao;
import cz.kksffn.notebook.dao.interfaces.ITodoItemDao;
import cz.kksffn.notebook.dao.TodoItemDao;
import cz.kksffn.notebook.service.interfaces.ADaoFactory;


/**
 * Here we can create Hibernate DAOs
 */
public class HibernateDaoFactory extends ADaoFactory {

    @Override
    public ITodoItemDao getTodoItemDao() { return new TodoItemDao(); }

    @Override
    public ICategoryDao getCategoryDao() { return new CategoryDao(); }
}
