package personal.viktrovovk.schedulegasoil.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import personal.viktrovovk.schedulegasoil.model.ScheduleItem;
import personal.viktrovovk.schedulegasoil.model.SelectorItem;

/**
 * Created by Viktor on 23/02/2017.
 */

public class ConnectionManager extends IntentService {
    public static String ACTION_RETURN_FACULTIES = "facultiesReturned",
            ACTION_RETURN_GROUPS = "groupsReturned",
            ACTION_RETURN_SCHEDULE = "scheduleReturned";
    public static String HOME_LINK = "http://rozklad.nung.edu.ua/index.php",
            TAG_LOG = "QueryService";
    public static final int SELECTOR_TYPE_FACULTY = 1,
            SELECTOR_TYPE_GROUP = 2;
    public RequestQueue requestQueue;
    public Context mContext;

    public ConnectionManager() {
        super("QueryService");
    }

    public ConnectionManager(Context context) {
        super("QueryService");
        mContext = context;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public void requestFacultiesSelector() {
        //Blue gay BoyCo.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_LINK.trim(), response -> {
            Intent serviceIntent = new Intent(mContext, this.getClass());
            String decodedResponse = null;
            try {
                decodedResponse = new String(response.getBytes("iso-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            serviceIntent.setData(Uri.parse(decodedResponse)).putExtra("type", "faculties");
//            Log.d(TAG_LOG, decodedResponse);

            mContext.startService(serviceIntent);
        }, error -> {
            Log.e("Connection error", error.toString());
        });
        requestQueue.add(stringRequest);
    }

    public void requestGroupsSelector(SelectorItem faculty) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_LINK.trim(), response -> {
            Intent serviceIntent = new Intent(mContext, this.getClass());
            String decodedResponse = null;
            try {
                decodedResponse = new String(response.getBytes("iso-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            serviceIntent.setData(Uri.parse(decodedResponse)).putExtra("type", "groups");
//            Log.d(TAG_LOG, decodedResponse);

            mContext.startService(serviceIntent);
        }, error -> {
            Log.e("Connection error", error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("faculty_id", faculty.getId().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void requestSchedulePage(SelectorItem faculty, SelectorItem group) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_LINK.trim(), response -> {
            Intent serviceIntent = new Intent(mContext, this.getClass());
            String decodedResponse = null;
            try {
                decodedResponse = new String(response.getBytes("iso-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            serviceIntent.setData(Uri.parse(decodedResponse)).putExtra("type", "schedule");
            mContext.startService(serviceIntent);
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("faculty_id", faculty.getId().toString().trim());
                params.put("group_id", group.getId().toString().trim());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String type = intent.getStringExtra("type");
        Intent response = new Intent();
        String dataString = intent.getDataString();

        if (type.equals("faculties")) {
            ArrayList<SelectorItem> selectorItems = Parser.parseSelector(dataString,
                    "<select name =\"faculty_id\" onchange=\"this\\.form\\.submit\\(\\);\">([\\s\\S]*?)<\\/select>");
            response.setAction(ACTION_RETURN_FACULTIES);
            response.putExtra("faculties", selectorItems);
            response.addCategory((Intent.CATEGORY_DEFAULT));

            sendBroadcast(response);
        } else if (type.equals("groups")) {
            ArrayList<SelectorItem> selectorItems = Parser.parseSelector(dataString,
                    "<option value=\"0\">Група<\\/option>([\\s\\S]*?)<\\/select>");
            response.setAction(ACTION_RETURN_GROUPS);
            response.putExtra("groups", selectorItems);
            response.addCategory((Intent.CATEGORY_DEFAULT));

            sendBroadcast(response);
        } else if (type.equals("schedule")) {
            ArrayList<ScheduleItem> scheduleItems = Parser.parseSchedule(dataString);
            response.setAction(ACTION_RETURN_SCHEDULE);
            response.putExtra("schedule", scheduleItems);
            response.addCategory((Intent.CATEGORY_DEFAULT));

            sendBroadcast(response);
        }
    }
}
