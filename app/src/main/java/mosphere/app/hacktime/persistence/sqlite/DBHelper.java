package mosphere.app.hacktime.persistence.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import mosphere.app.hacktime.entity.TaskActivity;
import mosphere.app.hacktime.utils.DateUtils;

/**
 * Created by alejandrolozanomedina on 24/10/16.
 */
public class DBHelper extends SQLiteOpenHelper{

    private final String LOG_TAG = this.getClass().getName();

    public static final String DATABASE_NAME = "hackTime.db";
    public static final String TABLE_ACTIVITIES = "activities";
    public static final String COLUMN_ACTIVITY = "activity";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_DATE_TIME_TASK = "dateTimeTask";
    public static final String COLUMN_DATE_TASK = "dateTask";

    private HashMap hp;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table activities " +
                        "(id integer primary key, activity text,task text,dateTimeTask text, dateTask text,timeTask text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS activities");
        onCreate(db);
    }

    public boolean insertActivity  (String activity, String dateTimeTask,String dateTask)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACTIVITY, activity);
        contentValues.put(COLUMN_DATE_TIME_TASK, dateTimeTask);
        contentValues.put(COLUMN_DATE_TASK, dateTask);
        db.insert(TABLE_ACTIVITIES, null, contentValues);
        return true;
    }

    public ArrayList<TaskActivity> getActivitiesByDay(Date date)
    {
        ArrayList<TaskActivity> array_list = new ArrayList<TaskActivity>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_ACTIVITIES +" where dateTask="+ DateUtils.resetTimeOnDate(date).getTime() , null );
        res.moveToFirst();
        TaskActivity activity=null;

        while(res.isAfterLast() == false){
            activity = new TaskActivity();
            activity.setActivity(res.getString(res.getColumnIndex(COLUMN_ACTIVITY)));
            activity.setDateTask(res.getString(res.getColumnIndex(COLUMN_DATE_TASK)));
            activity.setDateTimeTask(res.getString(res.getColumnIndex(COLUMN_DATE_TIME_TASK)));
            activity.setTask(res.getString(res.getColumnIndex(COLUMN_TASK)));
            array_list.add(activity);
            res.moveToNext();
        }
        return array_list;
    }

    public TaskActivity getLastRecord(){
         ArrayList<TaskActivity> arrayListTask = getActivitiesByDay(new Date());
         if(arrayListTask!=null && !arrayListTask.isEmpty()){
             return arrayListTask.get(arrayListTask.size()-1);
         }
        return null;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_ACTIVITIES);
        return numRows;
    }
}
