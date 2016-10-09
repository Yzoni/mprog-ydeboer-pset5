package nl.yrck.mprog_to_dolist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "TODOV2";

    private static final String TABLE_TODOLIST = "table_todolist";
    private static final String COLUMN_TODOLIST_ID = "id";
    private static final String COLUMN_TODOLIST_NAME = "name";
    private static final String COLUMN_TODOLIST_CREATEDAT = "created_at";

    private static final String TABLE_TODOITEM = "table_todoitem";
    private static final String COLUMN_TODOITEM_ID = "id";
    private static final String COLUMN_TODOITEM_LISTID = "todolist_id";
    private static final String COLUMN_TODOITEM_NAME = "name";
    private static final String COLUMN_TODOITEM_STATUS = "status";
    private static final String COLUMN_TODOITEM_CREATEDAT = "created_at";

    private static final String CREATE_TABLE_TODOLIST = "CREATE TABLE "
            + TABLE_TODOLIST + "(" + COLUMN_TODOLIST_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TODOLIST_NAME + " TEXT," + COLUMN_TODOLIST_CREATEDAT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TODOITEM = "CREATE TABLE "
            + TABLE_TODOITEM + "(" + COLUMN_TODOITEM_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TODOITEM_LISTID + " INTEGER," + COLUMN_TODOITEM_NAME + " TEXT,"
            + COLUMN_TODOITEM_STATUS + " INTEGER," + COLUMN_TODOITEM_CREATEDAT + " DATETIME" + ")";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TODOLIST);
        sqLiteDatabase.execSQL(CREATE_TABLE_TODOITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOLIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOITEM);
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TODOLIST);
        db.execSQL("DELETE FROM " + TABLE_TODOITEM);
        db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public List<TodoList> getAllTodoLists() {
        List<TodoList> todoLists = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_TODOLIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                todoLists.add(new TodoList(
                        c.getInt(c.getColumnIndex(COLUMN_TODOLIST_ID)),
                        c.getString(c.getColumnIndex(COLUMN_TODOLIST_NAME)),
                        c.getString(c.getColumnIndex(COLUMN_TODOLIST_CREATEDAT))
                ));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return todoLists;
    }

    public List<TodoList> getAllTodoListsPopulated() {
        List<TodoList> todoLists = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_TODOLIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                TodoList todoList = new TodoList(
                        c.getInt(c.getColumnIndex(COLUMN_TODOLIST_ID)),
                        c.getString(c.getColumnIndex(COLUMN_TODOLIST_NAME)),
                        c.getString(c.getColumnIndex(COLUMN_TODOLIST_CREATEDAT))
                );
                todoList.setTodoItems(getAllTodosFromTodoList(todoList.getId()));
                todoLists.add(todoList);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return todoLists;
    }

    public long createTodoList(TodoList todoList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TODOLIST_NAME, todoList.getName());
        values.put(COLUMN_TODOLIST_CREATEDAT, getDateTime());

        long id = db.insert(TABLE_TODOLIST, null, values);
        db.close();

        return id;
    }

    public long createTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TODOITEM_NAME, todoItem.getName());
        values.put(COLUMN_TODOITEM_LISTID, todoItem.getListId());
        values.put(COLUMN_TODOITEM_STATUS, todoItem.getStatus());
        values.put(COLUMN_TODOITEM_CREATEDAT, getDateTime());

        long id = db.insert(TABLE_TODOITEM, null, values);
        db.close();

        return id;
    }

    public TodoItem getTodo(long todoId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TODOITEM + " WHERE "
                + COLUMN_TODOITEM_ID + " = " + todoId;

        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();

            TodoItem todoItem = new TodoItem(
                    c.getInt(c.getColumnIndex(COLUMN_TODOITEM_ID)),
                    c.getInt(c.getColumnIndex(COLUMN_TODOITEM_LISTID)),
                    c.getString(c.getColumnIndex(COLUMN_TODOITEM_NAME)),
                    c.getInt(c.getColumnIndex(COLUMN_TODOITEM_STATUS)),
                    c.getString(c.getColumnIndex(COLUMN_TODOITEM_CREATEDAT))
            );
            c.close();

            db.close();
            return todoItem;
        }
        return null;
    }

    public TodoList getTodoList(long todoListId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TODOLIST + " WHERE "
                + COLUMN_TODOLIST_ID + " = " + todoListId;

        Cursor c = db.rawQuery(query, null);

        if (c != null) {
            c.moveToFirst();

            TodoList todoList = new TodoList(
                    c.getInt(c.getColumnIndex(COLUMN_TODOLIST_ID)),
                    c.getString(c.getColumnIndex(COLUMN_TODOLIST_NAME)),
                    c.getString(c.getColumnIndex(COLUMN_TODOLIST_CREATEDAT))
            );
            c.close();
            db.close();
            return todoList;
        }
        return null;
    }

    public List<TodoItem> getAllTodosFromTodoList(long todoListId) {
        List<TodoItem> todoItems = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_TODOITEM + " WHERE "
                + COLUMN_TODOITEM_LISTID + " = " + todoListId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                todoItems.add(new TodoItem(
                        c.getInt(c.getColumnIndex(COLUMN_TODOITEM_ID)),
                        c.getInt(c.getColumnIndex(COLUMN_TODOITEM_LISTID)),
                        c.getString(c.getColumnIndex(COLUMN_TODOITEM_NAME)),
                        c.getInt(c.getColumnIndex(COLUMN_TODOITEM_STATUS)),
                        c.getString(c.getColumnIndex(COLUMN_TODOITEM_CREATEDAT))
                ));

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return todoItems;
    }

    public List<TodoItem> getAllTodos() {
        List<TodoItem> todoItems = new ArrayList<>();
        String query = "SELECT  * FROM " + TABLE_TODOITEM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                todoItems.add(new TodoItem(
                        c.getInt(c.getColumnIndex(COLUMN_TODOITEM_ID)),
                        c.getInt(c.getColumnIndex(COLUMN_TODOITEM_LISTID)),
                        c.getString(c.getColumnIndex(COLUMN_TODOITEM_NAME)),
                        c.getInt(c.getColumnIndex(COLUMN_TODOITEM_STATUS)),
                        c.getString(c.getColumnIndex(COLUMN_TODOITEM_CREATEDAT))
                ));

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return todoItems;
    }

    public int updateTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TODOITEM_NAME, todoItem.getName());
        values.put(COLUMN_TODOITEM_STATUS, todoItem.getStatus());

        return db.update(TABLE_TODOITEM, values, COLUMN_TODOITEM_ID + " = ?",
                new String[]{String.valueOf(todoItem.getId())});
    }

    public void deleteTodoItem(long todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOITEM, COLUMN_TODOITEM_ID + " = ?",
                new String[]{String.valueOf(todoId)});
        db.close();
    }

    public void deleteTodoList(long todoListId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOLIST, COLUMN_TODOLIST_ID + " = ?",
                new String[]{String.valueOf(todoListId)});
        db.close();
    }
}
