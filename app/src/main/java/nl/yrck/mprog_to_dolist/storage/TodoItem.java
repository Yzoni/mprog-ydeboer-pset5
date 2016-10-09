package nl.yrck.mprog_to_dolist.storage;

public class TodoItem {

    public final static int STATUS_UNCHECKED = 0;
    public final static int STATUS_CHECKED = 1;

    private long id;
    private long listId;
    private String name;
    private int status;
    private String created_at;

    public TodoItem(long listId, String name) {
        this.listId = listId;
        this.name = name;
    }

    TodoItem(long id, long listId, String name, int status, String created_at) {
        this.id = id;
        this.listId = listId;
        this.name = name;
        this.status = status;
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public long getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TodoItem && this.id == ((TodoItem) obj).getId();
    }
}
