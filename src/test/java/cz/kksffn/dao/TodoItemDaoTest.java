package cz.kksffn.dao;

import cz.kksffn.dao.interfaces.ITodoItemDao;
import cz.kksffn.model.Category;
import cz.kksffn.model.TodoItem;
import cz.kksffn.service.interfaces.ADaoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TodoItemDaoTest {

    private ITodoItemDao dao;
    private static Logger logger;


    @BeforeEach
    void init(){
        dao = ADaoFactory.getInstance().getTodoItemDao();
        logger = LoggerFactory.getLogger(TodoItemDaoTest.class);
        logger.debug("\n====================================TESTING TODOITEMDAO=======================================");
    }

    @Nested
    class readTest{
        @Test
        @DisplayName("Get all fulfilled items from DB and check if the count is correct.")
        void readAllFulfilled() {
            logger.debug("Get all fulfilled items from DB and check if the count is correct.");
            Boolean fulfilled = true;
            List<TodoItem> fulfilledItems = dao.readAll(fulfilled);
            logger.debug("Getting all " + ((fulfilled) ? "fulfilled" : "not fulfilled") + " items............." );
            fulfilledItems.forEach(item-> {
               logger.debug(item.toString());
            });
            long expectedSize = fulfilledItems.size();
            long providedSize =  fulfilledItems.stream()
                    .filter(item -> item.getFulfilled()==fulfilled).count();
            assertEquals(expectedSize, providedSize);
        }
        @Test
        @DisplayName("Get all unfulfilled items from DB and check if the count is correct.")
        void readAllUnfulfilled(){
            logger.debug("Get all unfulfilled items from DB and check if the count is correct.");
            Boolean fulfilled = false;
            List<TodoItem> fulfilledItems = dao.readAll(fulfilled);
            logger.debug("\n\nGetting all " + ((fulfilled) ? "fulfilled" : "not fulfilled") + " items............." );
            fulfilledItems.forEach(item-> {
                logger.debug(String.valueOf(item));
            });
            long expectedSize = fulfilledItems.size();
            long providedSize =  fulfilledItems.stream()
                    .filter(item -> item.getFulfilled()==fulfilled).count();
            assertEquals(expectedSize, providedSize);
        }
        @Test
        @DisplayName("Get all items from DB and check if the count is not 0.")
        void testReadAll() {
            logger.debug("Get all items from DB and check if the count is not 0.");
            List<TodoItem> items = dao.readAll();
            items.forEach(item -> {
                logger.debug(item.toString());
            });
            int size = items.size();
            assertNotEquals(0, size);
        }
        @Test
        @DisplayName("Get existing item from DB")
        void readItemWithId1() {
            logger.debug("Get TodoItem with id = 1.");
            Optional<TodoItem>  item = dao.readById(1L);
            logger.debug(item.toString());
            assertTrue(item.isPresent());
        }
        @Test
        @DisplayName("Get non-existing item from DB")
        void readItemWithId1000000() {
            logger.debug("Get non-existing item from DB");
            Optional<TodoItem>  nonItem = dao.readById(1_000_000L);
            logger.debug(nonItem.toString());
            assertTrue(nonItem.isEmpty());
        }

    }

    @Nested
    class CreateTest{
        @Test
        @DisplayName("Creating new item in DB and checking if it's there...")
        void create() {
            logger.debug("Creating new TodoItem with category set to 'Foufka' in DB and checking if it's there...");
            TodoItem ukol = new TodoItem();
            LocalDateTime deadline = LocalDateTime.parse("2020-10-31T21:15");
            Category novaKategorie = new Category();
            novaKategorie.setTag("Foufka");

            Set<Category> categories = new HashSet();
            categories.add(novaKategorie);

            ukol.setText("Nový zkušební úkol!");
            ukol.setAuthor("Ivo");
            ukol.setDeadline(deadline);
            categories.forEach(ukol::addCategory);


            Long id = dao.create(ukol);
            logger.debug("New item with id = " + id + "created!...");
            Optional<TodoItem> zkouska = dao.readById(id);

            assertTrue(zkouska.isPresent());
        }
    }

    @Nested
    class UpdateTest{
        @Test
        @DisplayName("Updating non-existing item in DB throws NoResultException")
        void updateItemWithId1000000(){
            logger.debug("Updating non-existing item in DB throws NoResultException");
            TodoItem itemToUpdate = new TodoItem();
            itemToUpdate.setDeadline(LocalDateTime.of(2021,12,24,12,0));
            itemToUpdate.setAuthor("Pepa");
            itemToUpdate.setText("This text will not occur in DB!");
            itemToUpdate.setId(1_000_000L);
            assertEquals(-1L, dao.update(itemToUpdate));
        }
        @Test
        @DisplayName("Updating item with id = 1 setting text to 'Testing update' and then checking the text, id and " +
                "the Timestamp of update...")
        void updateItemWithId1(){
            logger.debug("Updating item with id = 1 setting text to 'Testing update' and then checking the text, id and " +
                    "the Timestamp of update...");
            Optional<TodoItem> item = dao.readById(1L);
            String updateString = "Testing update";
            LocalDateTime oldUpdatedAt;
            if (item.isPresent()) {
                TodoItem itemToUpdate = item.get();
                oldUpdatedAt = itemToUpdate.getUpdatedAt();
                String updatedString = itemToUpdate.getText().concat(". ").concat(updateString);
                itemToUpdate.setText(updatedString);

                dao.update(itemToUpdate);

                Optional<TodoItem> updatedItem = dao.readById(1L);
                assertAll(
                        () -> assertEquals(updatedString, updatedItem.get().getText()),
                        () -> assertEquals(1L, updatedItem.get().getId()),
                        () -> assertNotEquals(oldUpdatedAt, updatedItem.get().getUpdatedAt())
                );
            }
        }
    }
    @Nested
    class DeleteTest{
        @Test
        @DisplayName("Creating new Item and then deleting it, then checking if the created id doesn't exist in DB...")
        //Should check cascade (if categories are deleted from todo_item_category table
        void deleteItemWithoutCategories() {
            logger.debug("Creating new Item and then deleting it, then checking if the created id doesn't exist in DB...");
            TodoItem newItem = new TodoItem();
            Long id;

            LocalDateTime deadline = LocalDateTime.parse("2020-10-31T21:15");

            newItem.setText("Nový zkušební úkol!");
            newItem.setAuthor("Ivo");
            newItem.setDeadline(deadline);

            id = dao.create(newItem);

            Optional<TodoItem> deletak = dao.readById(id);

            if (deletak.isPresent()) {
                logger.debug("Item with id = " + id + " created and will be deleted...");
                dao.delete(deletak.get());
                logger.debug("Deletion completed!");
            }
            assertAll(
                    () -> assertNotEquals(0L, id),
                    () -> assertTrue(dao.readById(id).isEmpty())
            );

        }
    }
}