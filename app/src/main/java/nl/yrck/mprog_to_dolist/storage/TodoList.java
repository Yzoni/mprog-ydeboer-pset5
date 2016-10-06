package nl.yrck.mprog_to_dolist.storage;

import java.util.List;

public class TodoList {

    private long id;
    private String name;
    private String created_at;
    private List<TodoItem> todoItems;

    public TodoList(long id, String name, String created_at) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
    }

    public void addTodoItem(TodoItem todoItem) {
        todoItems.add(todoItem);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }
}
