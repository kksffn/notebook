package cz.kksffn.notebook;


import cz.kksffn.notebook.dao.interfaces.ICategoryDao;
import cz.kksffn.notebook.dao.interfaces.ITodoItemDao;
import cz.kksffn.notebook.model.Category;
import cz.kksffn.notebook.model.TodoItem;
import cz.kksffn.notebook.service.interfaces.ADaoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author ivohr
 */
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        ITodoItemDao itemDao = ADaoFactory.getInstance().getTodoItemDao();
        ICategoryDao categoryDao = ADaoFactory.getInstance().getCategoryDao();



        String[] authors = {"Pepa", "Karel", "Jarda", "Olinka", "Ivo", "Karolína", "Anička", "Marie"};
        String itemText = "Toto je nový úkol";
        LocalDateTime[] data = {LocalDateTime.parse("2020-10-31T21:15"),
                LocalDateTime.parse("2022-11-03T01:15"),
                        LocalDateTime.parse("2020-10-31T15:00"),
                                LocalDateTime.parse("2021-01-12T12:00"),
                                        LocalDateTime.parse("2020-10-28T00:00"),
                                                LocalDateTime.parse("2020-11-27T05:30")

        };
        List<Long> categoryId = new ArrayList();
        categoryId.add(1L);
        categoryId.add(2L);
        categoryId.add(3L);
        categoryId.add(4L);
        categoryId.add(5L);
        Random rand = new Random();

        logger.warn("Testing DB. Creating 1000 TodoItems and 20 categories with 'random' fields...");
        for (int i = 1; i < 21; i++) {
            Category category = new Category();
            category.setTag("Category"+i);
            logger.debug("Creating category" + category.getTag());
            categoryId.add(categoryDao.create(category));
        }

        for (int i = 1; i < 1001; i++) {
            TodoItem ukol = new TodoItem();
            LocalDateTime datum = data[rand.nextInt(6)];
            String author = authors[rand.nextInt(8)];
            LocalDateTime deadline = datum;

            Category category1 = categoryDao.readById((Long) categoryId.get(rand.nextInt(25))).get();
            Category category2 = categoryDao.readById((Long) categoryId.get(rand.nextInt(25))).get();

            Set<Category> categories = new HashSet();
            categories.add(category1);
            categories.add(category2);

            ukol.setText(itemText + i);
            ukol.setAuthor(author);
            ukol.setDeadline(deadline);
            ukol.setCategories(categories);
            categories.forEach(ukol::addCategory);

            //System.out.println(ukol);
            Long id = itemDao.create(ukol);
            logger.debug("New TodoItem with id = " + id + "created!...");
        }
    }
}
