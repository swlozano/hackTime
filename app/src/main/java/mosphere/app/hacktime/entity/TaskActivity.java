package mosphere.app.hacktime.entity;

/**
 * Created by alejandrolozanomedina on 24/10/16.
 */
public class TaskActivity {

    public String activity = "activity";
    public String task = "task";
    public String dateTimeTask = "dateTimeTask";
    public String dateTask = "dateTask";


    public TaskActivity() {

    }

    public TaskActivity(String activity, String task, String dateTimeTask, String dateTask, String timeTask) {
        this.activity = activity;
        this.task = task;
        this.dateTimeTask = dateTimeTask;
        this.dateTask = dateTask;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDateTimeTask() {
        return dateTimeTask;
    }

    public void setDateTimeTask(String dateTimeTask) {
        this.dateTimeTask = dateTimeTask;
    }

    public String getDateTask() {
        return dateTask;
    }

    public void setDateTask(String dateTask) {
        this.dateTask = dateTask;
    }

}