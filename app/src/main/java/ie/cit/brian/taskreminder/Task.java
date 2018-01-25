package ie.cit.brian.taskreminder;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by briancoveney on 11/25/15.
 */
public class Task implements Serializable{

    private String taskName;
    private String taskDescription;
    private String taskTime;
    private Date taskDate;

    private Task(String taskName, String taskDescription , String taskTime , Date taskDate)
    {
        this.taskTime = taskTime;
        this.taskDate = taskDate;
        Validate(taskName);
        Validate(taskDescription);
        this.taskDescription = taskDescription;
        this.taskName = taskName;
    }

    public static Task createTask(String taskName, String taskDescription , String taskTime , Date taskDate)
    {
        return new Task(taskName, taskDescription, taskTime, taskDate);
    }




    public String Validate(String input)
    {
        if(input.matches("^[0-9]*$")){
            throw new IllegalArgumentException("The task name or description cannot contain number.");
        }
        if(input.isEmpty()){
            throw new NullPointerException("The task name or description cannot be null.");
        }
        return input;
    }


    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
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
        return this.taskName;
    }


}

