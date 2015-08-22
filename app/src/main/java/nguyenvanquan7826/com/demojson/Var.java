package nguyenvanquan7826.com.demojson;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Var {


    // const for load and put data with server
    public static final String KEY_METHOD = "method";

    public static final String KEY_NICK = "nick";
    public static final String KEY_PASS = "pass";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_BEGIN_DATE = "begin_date";
    public static final String KEY_END_DATE = "end_date";

    public static final String KEY_LOGIN = "login";
    public static final String KEY_REGISTER = "register";
    public static final String KEY_ADD = "add";

    public static final int METHOD_LOGIN = 1;
    public static final int METHOD_REGISTER = 2;
    public static final int METHOD_GET_OLD_LOVER = 3;
    public static final int METHOD_ADD_OLD_LOVER = 4;


    public static void showToast(Context context, String sms) {
        Toast.makeText(context, sms, Toast.LENGTH_SHORT).show();
    }

    // method for save and get nick and pass user

    public static void save(Context context, String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext())
                .edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String get(Context context, String key) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return settings.getString(key, null);
    }
}
