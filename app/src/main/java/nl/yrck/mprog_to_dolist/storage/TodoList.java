package nl.yrck.mprog_to_dolist.storage;

import java.util.ArrayList;
import java.util.List;

public class TodoList {

    private long id;
    private String name;
    private String created_at;
    private List<TodoItem> todoItems;

    public TodoList(String name) {
        this.name = name;
    }

    public TodoList(long id, String name, String created_at) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
        this.todoItems = new ArrayList<>();
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
        this.todoItems.addAll(todoItems);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TodoList && this.id == ((TodoList) obj).getId();
    }
}
