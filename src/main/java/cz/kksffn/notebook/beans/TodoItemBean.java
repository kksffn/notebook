package cz.kksffn.notebook.beans;

import cz.kksffn.notebook.dao.interfaces.ITodoItemDao;
import cz.kksffn.notebook.model.NewTodoItem;
import cz.kksffn.notebook.model.TodoItem;
import cz.kksffn.notebook.service.interfaces.ADaoFactory;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//https://stackoverflow.com/questions/23281500/this-web-container-has-not-yet-been-started-glassfish-4-0-01-web
//JE POTŘEBA ZAVÍRAT ENTITYMANAGERFACTORY, jinak vznikne exception a data se nenačtou!!!
@Named
@SessionScoped
public class TodoItemBean implements Serializable{
     
    private List<TodoItem> items;
    private ITodoItemDao dao;    
    private int fulfilled;
    private Logger logger = LoggerFactory.getLogger(TodoItemBean.class);
    private static final long serialVersionUID = 1L;
    
    @PostConstruct
    private void init(){
        dao = ADaoFactory.getInstance().getTodoItemDao();
        logger.debug("Jsem v init metodě (PostConstruct) TodoItemBean");
        switch (fulfilled) {
            case 0:
                setItems(dao.readAll(false));
                logger.debug(fulfilled +": Přečetl jsem všechny nesplněné úkoly");
                break;
            case 1:
                setItems(dao.readAll(true));
                logger.debug(fulfilled + ": Přečetl jsem všechny splněné úkoly");
                break;                
            default:
                setItems(dao.readAll());
                logger.debug(fulfilled + ": Přečetl jsem všechny úkoly");
                break;
        }
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

    public int getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(int fulfilled) {
        this.fulfilled = fulfilled;
        init();
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
//============================== Manage user inputs ============================
    public String createNewTask(NewTodoItem item){
        System.out.println("Snažím se vytvořit nový úkol v DB!");
        logger.debug("Snažím se vytvořit nový úkol v DB!");
        logger.debug(item.getText());
        System.out.println(item.getText());
        logger.debug(item.getDeadline().toString());
       
        TodoItem entity = new TodoItem();
        entity.setText(item.getText());
        entity.setDeadline(item.getDeadline());
        entity.setAuthor(item.getAuthor());
        dao.create(entity);
        return "";
    }   
    
    public void setEnglish() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("en_GB"));
    }

}

