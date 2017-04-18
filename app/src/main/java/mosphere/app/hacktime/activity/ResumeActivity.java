package mosphere.app.hacktime.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import mosphere.app.hacktime.R;
import mosphere.app.hacktime.entity.TaskActivity;
import mosphere.app.hacktime.persistence.sqlite.DBHelper;
import mosphere.app.hacktime.utils.DateUtils;

public class ResumeActivity extends AppCompatActivity {

    DBHelper dbHelper;
    private final  String LOG_TAG = this.getClass().getName();
    String[] arrActivities;
    TextView txtResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_resume);
        txtResume = (TextView) findViewById(R.id.txtResume);
        arrActivities = getArrActivities();
        resume();

    }

    private String[] getArrActivities(){
        String[] arrActivities = new String[]{getString(R.string.txt_wealth_generator), getString(R.string.txt_study), getString(R.string.txt_exercise), getString(R.string.txt_take_care_Of_mySelf), getString(R.string.txt_responsibilities), getString(R.string.txt_culture) ,getString(R.string.txt_time_to_relax)};
        return arrActivities;
    }

    private void resume(){
        try {


         dbHelper = new DBHelper(getApplicationContext());
        HashMap<String, Long> hashMapActivities = initializeHashMapActivities();
        ArrayList<TaskActivity> arrTaskActivities = dbHelper.getActivitiesByDay(new Date());
        if(arrTaskActivities!=null && !arrTaskActivities.isEmpty()) {
            TaskActivity taskActivity;
            long minutes=0;
            Date dateStart;
            Date dateEnd;
            for (int i = 0; i < arrTaskActivities.size(); i++) {
                taskActivity = arrTaskActivities.get(i);
                dateStart = new Date(Long.parseLong(taskActivity.getDateTimeTask()));
                if(i+1<arrTaskActivities.size()) {
                    dateEnd = new Date(Long.parseLong(arrTaskActivities.get(i + 1).getDateTimeTask()));
                }else{
                    dateEnd = new Date();
                }
                minutes = DateUtils.getDateDifference(dateStart,dateEnd);
                hashMapActivities.put(taskActivity.getActivity(), hashMapActivities.get(taskActivity.getActivity())+minutes);
            }
        }
        printHashMapActivities(hashMapActivities);
        }catch (Exception  e){
            e.printStackTrace();
        }

    }

    private HashMap<String, Long> initializeHashMapActivities(){
        HashMap<String, Long> hashMapActivities = new HashMap<String, Long>();
        for (int index =0; index < arrActivities.length; index++){
            hashMapActivities.put(arrActivities[index],0L);
        }
        return hashMapActivities;
    }

    private void printHashMapActivities(HashMap<String, Long> hashMapActivities){
        int hr =0;
        int minutes=0;
        String str = "For today\n\n";
        for (int i = 0; i < hashMapActivities.size(); i++) {
            hr = (int) (hashMapActivities.get(arrActivities[i])/60);
            minutes = (int) (hashMapActivities.get(arrActivities[i])%60);
            //str += arrActivities[i]+"|"+hr+" hrs "+minutes+" mins\n";
            str += arrActivities[i]+" | "+DateUtils.formatDateDiff(hashMapActivities.get(arrActivities[i]))+"\n";
        }
        setTextToTextViewResume(str);
    }

    private void setTextToTextViewResume(String text){
        txtResume.setText(text);

    }
}
