package ie.cit.brian.taskreminder.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ie.cit.brian.taskreminder.Task;
import ie.cit.brian.taskreminder.activities.TaskActivity;
import ie.cit.brian.taskreminder.TaskController;

/**
 * Created by briancoveney on 11/29/15.
 */
public class SecondFragment extends ListFragment {


    private ArrayAdapter<Task> adapter;

    //The interface which this fragment uses to communicate up to its Activity
    public interface TaskSearcher {
        void refreshTaskList();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        ArrayList<Task> tasks =  TaskController.getInstance().getTask();

        this.adapter
                = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tasks);

        setListAdapter(this.adapter);
        super.onActivityCreated(savedInstanceState);
    }


    public void refreshList()
    {
        this.adapter.notifyDataSetChanged();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //Create a new instance of Intent - our context is our Activity - then our new TaskActivity class
        Intent intent = new Intent(getActivity(), TaskActivity.class);

        //Call the controller, we get the task list and within that list we get the item at that position
        //dictated what was selected in the list fragment
        Task selectedTask = TaskController.getInstance().getTask().get(position);

        //put our task object into the bundle
        intent.putExtra("selectedTask", selectedTask);

        //extra check to be sure there is something out there that is capable of dealing with the implicit intent
        if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
