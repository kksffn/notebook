package cz.kksffn.beans;

import cz.kksffn.dao.interfaces.ITodoItemDao;
import cz.kksffn.model.TodoItem;
import cz.kksffn.service.interfaces.ADaoFactory;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


//https://stackoverflow.com/questions/23281500/this-web-container-has-not-yet-been-started-glassfish-4-0-01-web
//JE POTŘEBA ZAVÍRAT ENTITYMANAGERFACTORY, jinak vznikne exception a data se nenačtou!!!
@Named
@RequestScoped
public class TodoItemBean implements Serializable{
     
    private List<TodoItem> items;
    private ITodoItemDao dao;
    private static final long serialVersionUID = 1L;
    
    @PostConstruct
    private void init(){
        dao = ADaoFactory.getInstance().getTodoItemDao();
        setItems(dao.readAll());        
    }     
    
//==================== GETTERS & SETTERS =======================================    
    public List<TodoItem> getItems() {
        return items;
    }
    public void setItems(List<TodoItem> items) {
        this.items = items;
    }
    public ITodoItemDao getDao() {
        return dao;
    }
    public void setDao(ITodoItemDao dao) {
        this.dao = dao;
    }
    
}
