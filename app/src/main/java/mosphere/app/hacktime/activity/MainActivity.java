package mosphere.app.hacktime.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Date;


import mosphere.app.hacktime.R;
import mosphere.app.hacktime.entity.TaskActivity;
import mosphere.app.hacktime.persistence.preferences.PersisPreferences;
import mosphere.app.hacktime.persistence.sqlite.DBHelper;
import mosphere.app.hacktime.utils.DateUtils;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroupActivities;
    EditText editTextTask;
    DBHelper dbHelper;
    Activity currentAndroidActivity;
    final String KEY_PREFERENCES = "key";
    private Handler handler;
    int radioBtnIdCurrentSelect=0;
    int radioBtnIdLastSelect=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_activity_main);
        currentAndroidActivity = this;
        dbHelper = new DBHelper(getApplicationContext());
        radioGroupActivities = (RadioGroup) findViewById(R.id.radioGroupActivities);
        editTextTask = (EditText) findViewById(R.id.editTextTask);
        handler = new Handler();
        readPersistence();
        setupRadioBtns();
        new Thread(new Task()).start();
    }

    private View.OnClickListener onClickBtnResume= new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ResumeActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener onClickRdoBtn = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            int selectedId = radioGroupActivities.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            String activityText =  getTextByIdRadioBtn(selectedId);
            Date dateTime = new Date();
            Date date = new Date();
            date.setTime(dateTime.getTime());
            date = DateUtils.resetTimeOnDate(date);
            dbHelper.insertActivity(activityText,dateTime.getTime() + "",date.getTime() + "");
            writePersistence(selectedId);
        }
    };

    private String getTextByIdRadioBtn(int idRadioBtn){
        String text="";
        switch (idRadioBtn){
            case  R.id.radioBtnCulture:
                text = getString(R.string.txt_culture);
                break;
            case  R.id.radioBtnExercise:
                text = getString(R.string.txt_exercise);
                break;
            case  R.id.radioBtnRelax:
                text = getString(R.string.txt_time_to_relax);
                break;
            case  R.id.radioBtnResp:
                text = getString(R.string.txt_responsibilities);
                break;
            case  R.id.radioBtnStudy:
                text = getString(R.string.txt_study);
                break;
            case  R.id.radioBtnTakeCare:
                text = getString(R.string.txt_take_care_Of_mySelf);
                break;
            case  R.id.radioBtnWealth:
                text = getString(R.string.txt_wealth_generator);
                break;

        }
        return text;
    }

    private void setupRadioBtns(){
        Button btnResume = (Button) findViewById(R.id.btnResume);
        btnResume.setOnClickListener(onClickBtnResume);

        RadioButton radioBtnCulture = (RadioButton) findViewById(R.id.radioBtnCulture);
        radioBtnCulture.setOnClickListener(onClickRdoBtn);

        RadioButton radioBtnExercise = (RadioButton) findViewById(R.id.radioBtnExercise);
        radioBtnExercise.setOnClickListener(onClickRdoBtn);

        RadioButton radioBtnRelax = (RadioButton) findViewById(R.id.radioBtnRelax);
        radioBtnRelax.setOnClickListener(onClickRdoBtn);

        RadioButton radioBtnResp = (RadioButton) findViewById(R.id.radioBtnResp);
        radioBtnResp.setOnClickListener(onClickRdoBtn);

        RadioButton radioBtnStudy = (RadioButton) findViewById(R.id.radioBtnStudy);
        radioBtnStudy.setOnClickListener(onClickRdoBtn);

        RadioButton radioBtnTakeCare = (RadioButton) findViewById(R.id.radioBtnTakeCare);
        radioBtnTakeCare.setOnClickListener(onClickRdoBtn);

        RadioButton radioBtnWealth = (RadioButton) findViewById(R.id.radioBtnWealth);
        radioBtnWealth.setOnClickListener(onClickRdoBtn);
    }

    private void writePersistence(int idRadioBtn){
        clearRadioButtonText(idRadioBtn);
        PersisPreferences preferences = new PersisPreferences();
        preferences.writeIntPreferences(currentAndroidActivity,KEY_PREFERENCES,idRadioBtn);
    }

    private void clearRadioButtonText(int idRadioBtn){
        if(radioBtnIdCurrentSelect!=0){
            radioBtnIdLastSelect = radioBtnIdCurrentSelect;
        }
        if(radioBtnIdLastSelect!=0){
            setTextOnRadioBtn(radioBtnIdLastSelect,getTextByIdRadioBtn(radioBtnIdLastSelect));
        }
        if(idRadioBtn!=0) {
            setRadioBtnIdCurrentSelect(idRadioBtn);
        }
    }

    private void readPersistence(){
        int idRadioBtn =getIdRadioBtnSelected();
        if(idRadioBtn!=0) {
            RadioButton radioButton = (RadioButton) findViewById(idRadioBtn);
            radioButton.toggle();
        }
        clearRadioButtonText(idRadioBtn);
    }

    private void setRadioBtnIdCurrentSelect(int idRadioBtn){
        radioBtnIdCurrentSelect = idRadioBtn;
    }

    private int getIdRadioBtnSelected(){
        PersisPreferences preferences = new PersisPreferences();
        int idRadioBtn = preferences.readIntPreferences(currentAndroidActivity,KEY_PREFERENCES,0);
        return idRadioBtn;
    }

    private void setTextOnRadioBtn(int idRadioBtn, String text){
        if(idRadioBtn!=0) {
            RadioButton radioButton = (RadioButton) findViewById(idRadioBtn);
            radioButton.setText(text);
        }
    }

    private void setTextAndTimeOnRadioBtn(){
        TaskActivity taskActivity  = dbHelper.getLastRecord();
        if(taskActivity!=null) {
            long lgMinutes = DateUtils.getDateDifference(DateUtils.convertStringToDate(taskActivity.getDateTimeTask()),new Date());
            int idRadioBtn = getIdRadioBtnSelected();
            setTextOnRadioBtn(idRadioBtn,getTextByIdRadioBtn(idRadioBtn) + " | "+DateUtils.formatDateDiff(lgMinutes));
        }
    }

    class Task implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextAndTimeOnRadioBtn();
                    }
                });
            }
        }
    }


}
