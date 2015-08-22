package nguyenvanquan7826.com.demojson;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, LoadJson.OnFinishLoadJSonListener {

    private EditText editNick, editPass, editRePass;

    private LoadJson loadJson;

    private Context context;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        setupToolbar();

        connectView();

        loadJson = new LoadJson();
        loadJson.setOnFinishLoadJSonListener(this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.register);
            }
        }
    }

    private void connectView() {
        editNick = (EditText) findViewById(R.id.edit_nick);
        editPass = (EditText) findViewById(R.id.edit_pass);
        editRePass = (EditText) findViewById(R.id.edit_re_pass);

        findViewById(R.id.btn_reset).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                reset();
                break;
            case R.id.btn_register:
                register();
                break;
            default:
                break;
        }
    }

    private void reset() {
        editNick.setText("");
        editPass.setText("");
        editRePass.setText("");
        editNick.requestFocus();
    }

    private void register() {
        String nick = editNick.getText().toString().trim();
        String pass = editPass.getText().toString().trim();
        String repass = editRePass.getText().toString().trim();

        // not enter nick name
        if (nick.length() == 0) {
            editNick.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_nick));
            return;
        }

        // not enter pass
        if (pass.length() == 0) {
            editPass.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_pass));
            return;
        }

        // not enter pass
        if (repass.length() == 0) {
            editRePass.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_repass));
            return;
        }

        if (!repass.equals(pass)) {
            editPass.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.pass_not_match));
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Var.KEY_NICK, nick);
        map.put(Var.KEY_PASS, pass);

        loadJson.sendDataToServer(Var.METHOD_REGISTER, map);
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
                if (jsonObject.getBoolean(Var.KEY_REGISTER)) {
                    Var.showToast(context, context.getResources().getString(R.string.register_success));
                    finish();
                } else {
                    Var.showToast(context, context.getResources().getString(R.string.register_fail));
                }
            } else {
                Var.showToast(context, error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
