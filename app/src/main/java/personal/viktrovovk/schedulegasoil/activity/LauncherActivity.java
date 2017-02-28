package personal.viktrovovk.schedulegasoil.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import personal.viktrovovk.schedulegasoil.R;
import personal.viktrovovk.schedulegasoil.model.SelectorItem;

/**
 * Created by volkeee on 27.02.17.
 */
public class LauncherActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.application_preference_key), MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();

        if (sharedPref.getString(getResources().getString(R.string.preference_key_users_faculty), "empty").equals("empty")) {
            Log.d("WARNING", "First launch, no saved group, starting group picker...");
            Intent groupPickerIntent = new Intent(this, WelcomeActivity.class);
            startActivity(groupPickerIntent);
            this.finish();
        } else {
            Gson gson = new Gson();
            String jsonFaculty = sharedPref.getString(getString(R.string.preference_key_users_faculty), null);
            String jsonGroup = sharedPref.getString(getString(R.string.preference_key_users_group), null);
            SelectorItem faculty = gson.fromJson(jsonFaculty, SelectorItem.class);
            SelectorItem group = gson.fromJson(jsonGroup, SelectorItem.class);

            Log.d("Saved info loaded", "Saved faculty is \"" + faculty.getName() + "with id " + faculty.getId().toString()
                    + "\" and saved group is \"" + group.getName() + "\" wtih id " + group.getId().toString());

            Intent intent = new Intent(this, ScheduleActivity.class);
            intent.putExtra("faculty", faculty);
            intent.putExtra("group", group);

            startActivity(intent);
            this.finish();
        }
    }
}
