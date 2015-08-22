package nguyenvanquan7826.com.demojson;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class AddLoverActivity extends AppCompatActivity implements View.OnClickListener, LoadJson.OnFinishLoadJSonListener, DatePickerDialog.OnDateSetListener {

    private EditText editName, editPhone;
    private Button btnBeginDate, btnEndDate;

    private LoadJson loadJson;

    private Context context;

    private ProgressDialog progressDialog;

    private String nick;

    // for begin date or end date
    private int typeDate;
    private String beginDate = "", endDate = "";

    public static final int BEGIN_DATE = 0;
    public static final int END_DATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lover);

        context = this;

        setupToolbar();

        connectView();

        loadJson = new LoadJson();
        loadJson.setOnFinishLoadJSonListener(this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));

        nick = getIntent().getStringExtra(Var.KEY_NICK);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.title_activity_add_lover);
            }
        }
    }

    private void connectView() {
        editName = (EditText) findViewById(R.id.edit_name);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        btnBeginDate = (Button) findViewById(R.id.btn_begin_date);
        btnEndDate = (Button) findViewById(R.id.btn_end_date);

        btnBeginDate.setOnClickListener(this);
        btnEndDate.setOnClickListener(this);

        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                reset();
                break;
            case R.id.btn_add:
                add();
                break;
            case R.id.btn_begin_date:
                typeDate = BEGIN_DATE;
                showDatePicker();
                break;
            case R.id.btn_end_date:
                typeDate = END_DATE;
                showDatePicker();
                break;
            default:
                break;
        }
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        String title;
        if (typeDate == BEGIN_DATE) {
            title = context.getResources().getString(R.string.begin_date);
        } else {
            title = context.getResources().getString(R.string.end_date);
        }
        dpd.show(getFragmentManager(), title);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + monthOfYear + "/" + year;
        if (typeDate == BEGIN_DATE) {
            beginDate = date;
            btnBeginDate.setText(date);
        } else {
            endDate = date;
            btnEndDate.setText(date);
        }
    }

    private void reset() {
        editName.setText("");
        editPhone.setText("");
        editName.requestFocus();
    }


    private void add() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        // not enter nick name
        if (name.length() == 0) {
            editName.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_nick));
            return;
        }

        // not enter pass
        if (phone.length() == 0) {
            editPhone.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_pass));
            return;
        }


        HashMap<String, String> map = new HashMap<>();
        map.put(Var.KEY_NICK, nick);
        map.put(Var.KEY_NAME, name);
        map.put(Var.KEY_PHONE, phone);
        map.put(Var.KEY_BEGIN_DATE, beginDate);
        map.put(Var.KEY_END_DATE, endDate);

        loadJson.sendDataToServer(Var.METHOD_ADD_OLD_LOVER, map);
        progressDialog.show();
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getBoolean(Var.KEY_ADD)) {
                    Var.showToast(context, context.getResources().getString(R.string.add_success));
                    finish();
                } else {
                    Var.showToast(context, context.getResources().getString(R.string.add_fail));
                }
            } else {
                Var.showToast(context, error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
