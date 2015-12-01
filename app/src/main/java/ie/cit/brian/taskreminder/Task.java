package ie.cit.brian.taskreminder;

import java.io.Serializable;

/**
 * Created by briancoveney on 11/25/15.
 */
public class Task implements Serializable{

    private String taskName;
//    private String taskDescription;
//    private int taskDueDate;

    public Task(String taskName)  //String taskDescription, int taskDueDate
    {
        this.taskName = taskName;
//        this.taskDescription = taskDescription;
//        this.taskDueDate = taskDueDate;
    }

//    public int getTaskDueDate() {
//        return taskDueDate;
//    }
//
//    public void setTaskDueDate(int taskDueDate) {
//        this.taskDueDate = taskDueDate;
//    }
//
//    public String getTaskDescription() {
//        return taskDescription;
//    }
//
//    public void setTaskDescription(String taskDescription) {
//        this.taskDescription = taskDescription;
//    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String toString()
    {
        return this.taskName;
    }
}
