package cz.kksffn.dao;

import cz.kksffn.model.Category;
import cz.kksffn.model.TodoItem;
import cz.kksffn.service.ADaoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TodoItemDaoTest {

    private ITodoItemDao dao;

    @BeforeEach
    void init(){
        dao = ADaoFactory.getInstance().getTodoItemDao();;
    }

    @Nested
    class readTest{
        @Test
        @DisplayName("Get all fulfilled items from DB and check if the count is correct.")
        void readAllFulfilled() {
            Boolean fulfilled = true;
            List<TodoItem> fulfilledItems = dao.readAll(fulfilled);
            System.out.println("\n\nGetting all " + ((fulfilled) ? "fulfilled" : "not fulfilled") + " items............." );
            fulfilledItems.forEach(item-> {
                System.out.println(item);
            });
            long expectedSize = fulfilledItems.size();
            long providedSize =  fulfilledItems.stream()
                    .filter(item -> item.getFulfilled()==fulfilled).count();
            assertEquals(expectedSize, providedSize);
        }
        @Test
        @DisplayName("Get all unfulfilled items from DB and check if the count is correct.")
        void readAllUnfulfilled(){
            Boolean fulfilled = false;
            List<TodoItem> fulfilledItems = dao.readAll(fulfilled);
            System.out.println("\n\nGetting all " + ((fulfilled) ? "fulfilled" : "not fulfilled") + " items............." );
            fulfilledItems.forEach(item-> {
                System.out.println(item);
            });
            long expectedSize = fulfilledItems.size();
            long providedSize =  fulfilledItems.stream()
                    .filter(item -> item.getFulfilled()==fulfilled).count();
            assertEquals(expectedSize, providedSize);
        }
        @Test
        @DisplayName("Get all items from DB and check if the count is not 0.")
        void testReadAll() {
            List<TodoItem> items = dao.readAll();
            items.forEach(item -> {
                System.out.println(item);
            });
            int size = items.size();
            assertNotEquals(0, size);
        }
        @Test
        @DisplayName("Get existing item from DB")
        void readItemWithId1() {
            Optional<TodoItem>  item = dao.readById(1L);
            System.out.println(item);
            assertTrue(item.isPresent());
        }
        @Test
        @DisplayName("Get non-existing item from DB")
        void readItemWithId1000000() {
            Optional<TodoItem>  nonItem = dao.readById(1_000_000L);
            System.out.println(nonItem);
            assertTrue(nonItem.isEmpty());
        }

    }

    @Nested
    class CreateTest{
        @Test
        @DisplayName("Creating new item in DB and checking if it's there...")
        void create() {
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
            System.out.println("New item ukol created!..." + id);
            Optional<TodoItem> zkouska = dao.readById(id);

            assertTrue(zkouska.isPresent());
        }
    }

    @Nested
    class UpdateTest{
        @Test
        @DisplayName("Updating non-existing item in DB throws NoResultException")
        void updateItemWithId1000000(){
            TodoItem itemToUpdate = new TodoItem();
            itemToUpdate.setDeadline(LocalDateTime.of(2021,12,24,12,0));
            itemToUpdate.setAuthor("Pepa");
            itemToUpdate.setText("This text will not occur in DB!");
            itemToUpdate.setId(1_000_000L);
            assertThrows(NoResultException.class, ()-> dao.update(itemToUpdate));
        }
        @Test
        @DisplayName("Updating item with id = 1 setting text to 'Testing update' and then checking the text, id and " +
                "the Timestamp of update...")
        void updateItemWithId1(){
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
            TodoItem newItem = new TodoItem();
            Long id;

            LocalDateTime deadline = LocalDateTime.parse("2020-10-31T21:15");

            newItem.setText("Nový zkušební úkol!");
            newItem.setAuthor("Ivo");
            newItem.setDeadline(deadline);

            id = dao.create(newItem);

            Optional<TodoItem> deletak = dao.readById(id);

            if (deletak.isPresent()) {
                System.out.println("Item with id = " + id + " created and will be deleted...");
                dao.delete(deletak.get());
                System.out.println("Deletion completed!");
            }
            assertAll(
                    () -> assertNotEquals(0L, id),
                    () -> assertTrue(dao.readById(id).isEmpty())
            );

        }
    }
}