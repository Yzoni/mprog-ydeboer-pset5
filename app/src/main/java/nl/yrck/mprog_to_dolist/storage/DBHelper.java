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

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TODO";

    private static final String TABLE_TODOITEM = "table_todoitem";
    private static final String COLUMN_TODOITEM_ID = "id";
    private static final String COLUMN_TODOITEM_LISTID = "todolist_id";
    private static final String COLUMN_TODOITEM_NAME = "name";
    private static final String COLUMN_TODOITEM_STATUS = "status";
    private static final String COLUMN_TODOITEM_CREATEDAT = "created_at";

    private static final String TABLE_TODOLIST = "table_todolist";
    private static final String COLUMN_TODOLIST_ID = "id";
    private static final String COLUMN_TODOLIST_NAME = "name";
    private static final String COLUMN_TODOLIST_CREATEDAT = "created_at";

//    private static final String TABLE_TODOLIST_TODOITEM = "table_todolist_todoitem";
//    private static final String COLUMN_TODOLIST_TODOITEM_ID = "id";
//    private static final String COLUMN_TODOLIST_TODOITEM_TODOLISTID = "todolist_id";
//    private static final String COLUMN_TODOLIST_TODOITEM_TODOITEMID = "todoitem_id";

    private static final String CREATE_TABLE_TODOLIST = "CREATE TABLE "
            + TABLE_TODOLIST + "(" + COLUMN_TODOLIST_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TODOLIST_NAME + " TEXT," + COLUMN_TODOLIST_CREATEDAT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TODOITEM = "CREATE TABLE "
            + TABLE_TODOITEM + "(" + COLUMN_TODOITEM_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TODOITEM_NAME + " TEXT," + COLUMN_TODOITEM_STATUS + " INTEGER,"
            + COLUMN_TODOITEM_CREATEDAT + " DATETIME" + ")";

//    private static final String CREATE_TABLE_TODOLIST_TODOITEM = "CREATE TABLE "
//            + TABLE_TODOLIST_TODOITEM + "(" + COLUMN_TODOLIST_TODOITEM_ID + " INTEGER PRIMARY KEY,"
//            + COLUMN_TODOLIST_TODOITEM_TODOITEMID + " INTEGER,"
//            + COLUMN_TODOLIST_TODOITEM_TODOLISTID + " INTEGER" + ")";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TODOLIST);
        sqLiteDatabase.execSQL(CREATE_TABLE_TODOITEM);
//        sqLiteDatabase.execSQL(CREATE_TABLE_TODOLIST_TODOITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOLIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOITEM);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOLIST_TODOITEM);
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
            } while (c.moveToNext());
        }
        c.close();

        return todoLists;
    }

    public long createTodoList(TodoList todoList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TODOLIST_NAME, todoList.getName());
        values.put(COLUMN_TODOLIST_CREATEDAT, getDateTime());

        long id = db.insert(TABLE_TODOITEM, null, values);
        this.getWritableDatabase().close();

        return id;
    }

    public long createTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TODOITEM_NAME, todoItem.getName());
        values.put(COLUMN_TODOITEM_STATUS, todoItem.getStatus());
        values.put(COLUMN_TODOITEM_CREATEDAT, getDateTime());

        long id = db.insert(TABLE_TODOITEM, null, values);
        this.getWritableDatabase().close();

        return id;
    }

    public TodoItem getTodo(long todoId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TODOITEM + " WHERE "
                + COLUMN_TODOITEM_ID + " = " + todoId;

        Cursor c = db.rawQuery(query, null);

        if (c != null)
            c.moveToFirst();

        TodoItem todoItem = new TodoItem(
                c.getInt(c.getColumnIndex(COLUMN_TODOITEM_ID)),
                c.getInt(c.getColumnIndex(COLUMN_TODOITEM_LISTID)),
                c.getString(c.getColumnIndex(COLUMN_TODOITEM_NAME)),
                c.getInt(c.getColumnIndex(COLUMN_TODOITEM_STATUS)),
                c.getString(c.getColumnIndex(COLUMN_TODOITEM_CREATEDAT))
        );
        c.close();
        return todoItem;
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

    public void deleteTodo(long todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOITEM, COLUMN_TODOITEM_ID + " = ?",
                new String[]{String.valueOf(todoId)});
    }
}
