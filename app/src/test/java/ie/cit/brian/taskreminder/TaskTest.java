package ie.cit.brian.taskreminder;

import org.joda.time.DateTime;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by brian on 25/07/16.
 */
public class TaskTest {

    private Task mTask;
    private Date mDate;

    @Before
    public void setUp() throws Exception {

        mDate = new Date();
        mTask = new Task("Running", "in park", "at 3pm",
                mDate);
    }

    @Test
    public void testGetTaskDate() throws Exception {
        assertEquals(mDate, mTask.getTaskDate());
    }

    @Test
    public void testSetTaskDate() throws Exception {

    }

    @Test
    public void testGetTaskDescription() throws Exception {
        assertEquals("in park", mTask.getTaskDescription());
    }

    @Test
    public void testSetTaskDescription() throws Exception {

    }

    @Test
    public void testGetTaskTime() throws Exception {
        assertEquals("at 3pm", mTask.getTaskTime());
    }

    @Test
    public void testSetTaskTime() throws Exception {

    }

    @Test
    public void testGetTaskName() throws Exception {
        assertEquals("Running", mTask.getTaskName());
    }

    @Test
    public void testSetTaskName() throws Exception {
    }

    @Test
    public void testToString() throws Exception {

    }
}