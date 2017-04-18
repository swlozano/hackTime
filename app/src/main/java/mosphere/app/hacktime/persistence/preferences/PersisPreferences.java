package mosphere.app.hacktime.persistence.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alejandrolozanomedina on 26/10/16.
 */
public class PersisPreferences {

    public void writeIntPreferences(Activity context, String key, int value){
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt( key, value);
        editor.commit();
    }

    public int readIntPreferences(Activity context, String key, int defaultValue){
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultValue);
    }
}
