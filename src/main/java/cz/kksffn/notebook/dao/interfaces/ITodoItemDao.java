package cz.kksffn.notebook.dao.interfaces;

import cz.kksffn.notebook.model.TodoItem;

import java.util.List;

public interface ITodoItemDao extends IDao<TodoItem>{
    List<TodoItem> readAll(boolean fulfilled);
}
