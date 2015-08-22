package nguyenvanquan7826.com.demojson;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoadJson.OnFinishLoadJSonListener {

    private EditText editNick, editPass;
    private CheckBox cbRememberPass;

    private LoadJson loadJson;

    private Context context;

    private ProgressDialog progressDialog;

    private String nick, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                getSupportActionBar().setTitle(R.string.login);
            }
        }
    }

    private void connectView() {
        editNick = (EditText) findViewById(R.id.edit_nick);
        editPass = (EditText) findViewById(R.id.edit_pass);
        cbRememberPass = (CheckBox) findViewById(R.id.cb_remember);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);

        // get nick and pass if it be remember
        nick = Var.get(context, Var.KEY_NICK);
        pass = Var.get(context, Var.KEY_PASS);

        if (nick != null && pass != null) {
            editNick.setText(nick);
            editPass.setText(pass);
            cbRememberPass.setChecked(true);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
            default:
                break;
        }
    }

    private void register() {
        Intent intent = new Intent(context, RegisterActivity.class);
        startActivity(intent);
    }

    private void login() {
        nick = editNick.getText().toString().trim();
        pass = editPass.getText().toString().trim();

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

        // save nick and pass
        if (cbRememberPass.isChecked()) {
            Var.save(context, Var.KEY_NICK, nick);
            Var.save(context, Var.KEY_PASS, pass);
        } else {
            Var.save(context, Var.KEY_NICK, null);
            Var.save(context, Var.KEY_PASS, null);
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Var.KEY_NICK, nick);
        map.put(Var.KEY_PASS, pass);

        loadJson.sendDataToServer(Var.METHOD_LOGIN, map);
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
                if (jsonObject.getBoolean(Var.KEY_LOGIN)) {
                    Var.showToast(context, context.getResources().getString(R.string.login_success));

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(Var.KEY_NICK, nick);
                    startActivity(intent);

                    finish();
                } else {
                    Var.showToast(context, context.getResources().getString(R.string.login_fail));
                }
            } else {
                Var.showToast(context, error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
