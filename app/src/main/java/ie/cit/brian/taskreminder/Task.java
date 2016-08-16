package ie.cit.brian.taskreminder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.xml.validation.Validator;

/**
 * Created by briancoveney on 11/25/15.
 */
public class Task implements Serializable{

    private String taskName;
    private String taskDescription;
    private String taskTime;
    private Date taskDate;

    public Task(String taskName, String taskDescription , String taskTime , Date taskDate)
    {
        this.taskTime = taskTime;
        this.taskDate = taskDate;

//        Validate(taskName);
//        Validate(taskDescription);
        this.taskDescription = taskDescription;
        this.taskName = taskName;

    }



    public Task(String taskName, String taskDescription)
    {
        this.taskDescription = taskDescription;
        this.taskName = taskName;
    }



    public String Validate(String input)
    {
        if(!input.matches("^[a-zA-Z]*$")){
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

