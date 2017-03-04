package personal.viktrovovk.schedulegasoil.model;

import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by volkeee on 04.03.17.
 */

public class Task implements Serializable {
    private Integer taskID;
    private String lesson;
    private String title;
    private String body;
    private Integer priority;

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_ID = "taskid";
        public static final String COLUMN_TASK_LESSON = "lesson";
        public static final String COLUMN_TASK_TITLE = "tasktitle";
        public static final String COLUMN_TASK_BODY = "taskbody";
        public static final String COLUMN_TASK_PRIORITY = "taskpriority";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE" + TABLE_NAME + " (" +
                        COLUMN_TASK_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_TASK_LESSON + "TEXT," +
                        COLUMN_TASK_TITLE + "TEXT," +
                        COLUMN_TASK_BODY + "TEXT," +
                        COLUMN_TASK_PRIORITY + " )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
