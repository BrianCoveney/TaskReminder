package ie.cit.brian.taskreminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by briancoveney on 11/26/15.
 */
public class TaskController
{
    private static TaskController instance;


    private ArrayList<Task> taskList;

    private TaskController()
    {
        this.taskList = new ArrayList<Task>();
    }

    public static TaskController getInstance()
    {
        if(instance == null){
            instance = new TaskController();
        }
        return instance;
    }

    public void addTask(String taskName, String taskDesc, String taskTime, Date taskDate){
        Task task = new Task(taskName, taskDesc, taskTime, taskDate);
        this.taskList.add(task);
    }

    public ArrayList<Task> getTask()
    {
        return this.taskList;
    }

}
