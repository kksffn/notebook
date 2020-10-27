package cz.kksffn.model;

import cz.kksffn.model.interfaces.IEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author ivohr
 */
@Entity
@Table(name="category")
public class Category implements IEntity, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    
    @Column(name= "tag")
    private String tag;
    
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(
            name = "todo_item_category",
            joinColumns = {@JoinColumn(name = "category_id", 
                    foreignKey = @ForeignKey(name="FK_todo_item-category_category"))},
            inverseJoinColumns = {@JoinColumn(name = "item_id", 
                    foreignKey = @ForeignKey(name="FK_todo_item-category_todo_item"))}
            )
    Set<TodoItem> items = new HashSet<TodoItem>();
    
//------------------------- CONSTRUCTOR ----------------------------------------

    public Category() {
    }

//---------------------------- GETTERS & SETTERS -------------------------------    

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }    

    public Set<TodoItem> getItems() {
        return items;
    }

    public void setItems(Set<TodoItem> items) {
        this.items = items;
    }
    
    
    
//---------------------------- OVERRIDE METHODS -------------------------------    
        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return id.equals(other.getId()) && tag.equals(other.getTag());
    }

    @Override
    public String toString() {
        return "Category: id = " + id + ", tag = \"" + tag +"\"";
    }
    
}
