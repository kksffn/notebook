package cz.kksffn.dao.interfaces;

import cz.kksffn.model.TodoItem;

import java.util.List;

public interface ITodoItemDao extends IDao<TodoItem>{
    List<TodoItem> readAll(boolean fulfilled);
}
