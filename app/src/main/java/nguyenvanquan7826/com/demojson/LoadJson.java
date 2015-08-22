package nguyenvanquan7826.com.demojson;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadJson {

    public static final String LINK = "http://nguyenvanquan7826.com/mobile/demo-json/lover.php";

    public void sendDataToServer(int method, HashMap<String, String> map) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        // put data to server
        params.put(Var.KEY_METHOD, method);

        if (map != null) {
            for (String key : map.keySet()) {
                params.put(key, map.get(key));
            }
        }

        System.out.println("Post...");

        client.post(LINK, params, new AsyncHttpResponseHandler() {

            @SuppressWarnings("deprecation")
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                System.out.println("onSuccess:" + json);
                onFinishLoadJSonListener.finishLoadJSon(null, json);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("onFailure:" + statusCode);

                String e;

                if (statusCode == 404) {
                    e = "Requested resource not found";
                } else if (statusCode == 500) {
                    e = "Something went wrong at server end";
                } else {
                    e = "Device might not be connected to Internet";
                }
                onFinishLoadJSonListener.finishLoadJSon(e, null);
            }
        });
    }

    public static Lover jsonToLover(JSONObject jsonObject) {
        try {
            int id = jsonObject.getInt(Var.KEY_ID);
            String name = jsonObject.getString(Var.KEY_NAME);
            String phone = jsonObject.getString(Var.KEY_PHONE);
            String beginDate = jsonObject.getString(Var.KEY_BEGIN_DATE);
            String endDate = jsonObject.getString(Var.KEY_END_DATE);
            return new Lover(id, name, phone, beginDate, endDate);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Lover> jsonToListLover(String json) {
        ArrayList<Lover> list = new ArrayList<>();

        try {
            JSONArray arraySMSJson = new JSONArray(json);
            for (int i = 0; i < arraySMSJson.length(); i++) {
                JSONObject jsonObject = arraySMSJson.getJSONObject(i);
                Lover lover = jsonToLover(jsonObject);
                if (lover != null) {
                    list.add(lover);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public interface OnFinishLoadJSonListener {
        void finishLoadJSon(String error, String json);
    }

    public OnFinishLoadJSonListener onFinishLoadJSonListener;

    public void setOnFinishLoadJSonListener(OnFinishLoadJSonListener onFinishLoadJSonListener) {
        this.onFinishLoadJSonListener = onFinishLoadJSonListener;
    }
}
