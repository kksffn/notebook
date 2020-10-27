package cz.kksffn.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

/**
 *
 * @author ivohr
 */
@Entity
@FilterDef(name = "fulfilled", parameters = @ParamDef(name="fulfilled", type = "boolean"))
@Filter(name = "fulfilled", condition = "fulfilled = :fulfilled")
@Table(name="todo_item")
public class TodoItem implements IEntity, Serializable {
    
//-------------------------private fields------------------------------------
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    
    @Column(name="text")
    private String text;
    
    @Column(name="author")
    private String author;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", insertable = false, updatable = false)
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    
    @Column(name = "deadline")
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deadline;
    
    @Column(name = "fulfilled")
    private boolean fulfilled;  
    
    @ManyToMany(mappedBy = "items", cascade={CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
    private Set<Category> categories = new HashSet<Category>();
    
    
//------------------------- CONSTRUCTOR ----------------------------------------

    public TodoItem() {
    }

//---------------------------- GETTERS & SETTERS -------------------------------
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public Set<Category> getCategories() {        
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

//----------------------------------- Auxiliary method to add (or remove) category --------------------------------
    public void addCategory(Category category) {
            this.categories.add(category);
            category.getItems().add(this);
    }
    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getItems().remove(this);
    }

//-------------------------------------------OVERRIDE METHODS----------------------------------------------


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return id.equals(todoItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String printCategories = "";
        Iterator<Category> iterator = categories.iterator();
        while (iterator.hasNext()) { 
            printCategories += iterator.next().getTag() + " ";                    
        }

        return "ITEM " + id + ": "
                + "\n\t" + text + " do " + deadline.format(DateTimeFormatter.ofPattern("dd.MMM.yyyy, HH:mm"))
                + "\n\tAuthor: " + author 
                + "\n\tCreated at: " + createdAt.format(DateTimeFormatter.ofPattern("dd.MMM.yyyy, HH:mm"))
                + "\n\tUpdated at: " + updatedAt.format(DateTimeFormatter.ofPattern("dd.MMM.yyyy, HH:mm"))
                + "\n\tFulfilled: " + (fulfilled ? "splněno:-)" : "ještě nesplněno")
                + "\n\tCategories: " + printCategories;

    }
}
