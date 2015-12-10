package ie.cit.brian.taskreminder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by briancoveney on 11/25/15.
 */
public class Task implements Serializable{

    private String taskName;
    private String taskDescription;
    private String taskTime;
    private String taskDate;

    public Task(String taskName, String taskDescription , String taskTime , String taskDate)
    {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskTime = taskTime;
        this.taskDate = taskDate;

    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {this.taskDescription = taskDescription;}

    public String getTaskTime() {return taskTime;}

    public void setTaskTime(String taskTime) {this.taskTime = taskTime;}

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String toString()
    {
        return this.taskName ;
    } //+ " - " + this.taskTime


}

