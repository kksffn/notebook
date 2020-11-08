package cz.kksffn.notebook.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;


/**
 * This class will hold user inputs for creating new TodoItem 
 * @author ivohr
 */
@Named
@RequestScoped
public class NewTodoItem {

    @NotNull(message="Text of your task cannot be empty. You must create some text when creating a task!")
    private String text;    
    @NotNull(message="C'mon, have some deadline, otherwise you will not fulfill the task!")
    private LocalDateTime deadline;    
    private Date userInputDate;    
    private String author;
    
    
//========================== CONSTRUCTOR =======================================    

    public NewTodoItem() {
    }
    
//==================== GETTERS & SETTERS =======================================    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {            
        this.deadline = deadline;
    }

    public Date getUserInputDate() {
        return userInputDate;
    }

    public void setUserInputDate(Date userInputDate) {
        LocalDateTime datetime = LocalDateTime.ofInstant(userInputDate.toInstant(), ZoneId.systemDefault());
        this.deadline = datetime;
        this.userInputDate = userInputDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    } 
    
}
