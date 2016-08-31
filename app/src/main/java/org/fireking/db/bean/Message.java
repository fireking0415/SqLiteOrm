package org.fireking.db.bean;


import com.eku.library.SQLiteColumn;

/**
 * Created by kima.wang on 2016/8/30.
 */
public class Message {

    public static final String COL_TITLE = "title";
    public static final String COL_MESSAGE = "message";
    public static final String COL_ID = "_id";

    @SQLiteColumn(ColumnName = COL_ID, ColumnType = "INTEGER", PrimaryKey = true)
    private long id;

    @SQLiteColumn(ColumnName = COL_TITLE, ColumnType = "VARCHAR ", Nullable = false)
    private String title;

    @SQLiteColumn(ColumnName = COL_MESSAGE, ColumnType = "VARCHAR ", Nullable = false)
    private String message;

    private int type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Message(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public Message() {
    }
}
