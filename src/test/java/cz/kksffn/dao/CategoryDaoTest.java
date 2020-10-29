package cz.kksffn.dao;

import cz.kksffn.dao.interfaces.ICategoryDao;
import cz.kksffn.dao.interfaces.ITodoItemDao;
import cz.kksffn.model.Category;
import cz.kksffn.model.TodoItem;
import cz.kksffn.service.interfaces.ADaoFactory;
import cz.kksffn.util.JPASessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDaoTest {

    private ICategoryDao categoryDao;
    private ITodoItemDao itemDao;
    private static Logger logger;


    @BeforeEach
    void init(){
        categoryDao = ADaoFactory.getInstance().getCategoryDao();
        itemDao = ADaoFactory.getInstance().getTodoItemDao();
        logger = LoggerFactory.getLogger(TodoItemDaoTest.class);
        logger.debug("\n====================================TESTING CATEGORYDAO=======================================");
    }
//    @AfterAll
//    static void closeEmF() {
//        JPASessionUtil.closeEMF();
//    }
    
    @Test
    void readAll() {
        logger.debug("Get all categories from DB and check if the count is not 0.");
        List<Category> items = categoryDao.readAll();
        items.forEach(item -> {
            logger.debug(item.toString());
        });
        int size = items.size();
        assertNotEquals(0, size);
    }

    @Test
    void readByIdWithId1() {
        logger.debug("Get category with id = 1.");
        Optional<Category> item = categoryDao.readById(1L);
        logger.debug(item.toString());
        assertTrue(item.isPresent());
    }
    @Test
    void readCategoryWithnonexistingId1000000(){
        logger.debug("Get non-existing category from DB");
        Optional<Category>  nonItem = categoryDao.readById(1_000_000L);
        logger.debug(nonItem.toString());
        assertTrue(nonItem.isEmpty());
    }

    @Test
    void create() {
        logger.debug("Creating new category 'TestingOne' with TodoItem id=1 in DB and checking if it's there...");
        Category category = new Category();
        category.setTag("TestingOne");

        TodoItem ukol = itemDao.readById(1L).get();
        ukol.getCategories().add(category);
        category.getItems().add(ukol);
        Long id = categoryDao.create(category);
        logger.debug("New category with id = " + id + "created!...");
        Optional<Category> zkouska = categoryDao.readById(id);
        assertTrue(zkouska.isPresent());
    }

    @Test
    void update() {
        logger.debug("Updating item with id = 276 setting tag to tag + 'T' and then checking the tag and id...");
        Optional<Category> item = categoryDao.readById(276L);
        String updateString = "T";

        if (item.isPresent()) {
            Category itemToUpdate = item.get();
            String updatedString = itemToUpdate.getTag().concat(updateString);
            itemToUpdate.setTag(updatedString);
            categoryDao.update(itemToUpdate);

            Optional<Category> updatedItem = categoryDao.readById(276L);
            assertAll(
                    () -> assertEquals(updatedString, updatedItem.get().getTag()),
                    () -> assertEquals(276L, updatedItem.get().getId())
            );
        }
    }

    @Test
    void delete() {
        logger.debug("Creating new category and then deleting it from DB. Checking if created id doesn't exist in DB anymore...");
        Category category = new Category();
        category.setTag("FFLLLLLLLLL");
        Long id = categoryDao.create(category);

        Optional<Category> deletak = categoryDao.readById(id);
        boolean isThere = deletak.isPresent();
        if (isThere) {
            categoryDao.delete(deletak.get());
        }
        assertAll(
                () -> assertEquals(true, isThere),
                () -> assertTrue(categoryDao.readById(id).isEmpty())
        );
    }
}