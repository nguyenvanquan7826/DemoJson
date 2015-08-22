package nguyenvanquan7826.com.demojson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoadJson.OnFinishLoadJSonListener {

    private ItemAdapter adapter;
    private ArrayList<Lover> list;

    private LoadJson loadJson;

    private Context context;

    private ProgressBar progressBar;
    private ViewGroup layoutProgress;
    private TextView tvNotify;

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        loadJson = new LoadJson();
        loadJson.setOnFinishLoadJSonListener(this);

        setupToolbar();
        setupRecyclerView();

        findViewById(R.id.fab_add).setOnClickListener(this);
        layoutProgress = (ViewGroup) findViewById(R.id.layout_progress);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        tvNotify = (TextView) findViewById(R.id.tv_notify);

        nick = getIntent().getStringExtra(Var.KEY_NICK);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_lover);

        // If the size of views will not change as the data changes.
        recyclerView.setHasFixedSize(true);

        // Setting the LayoutManager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Setting the adapter.
        list = new ArrayList<>();
        adapter = new ItemAdapter(context, list);
        recyclerView.setAdapter(adapter);
    }

    public void onResume() {
        super.onResume();
        refresh();
    }

    /**
     * re load data from server
     */
    private void refresh() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Var.KEY_NICK, nick);
        loadJson.sendDataToServer(Var.METHOD_GET_OLD_LOVER, map);
        layoutProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        tvNotify.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
            case R.id.action_refresh:
                refresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                addOldLover();
                break;
        }
    }

    private void addOldLover() {
        Intent intent = new Intent(context, AddLoverActivity.class);
        intent.putExtra(Var.KEY_NICK, nick);
        startActivity(intent);
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (json != null) {
            layoutProgress.setVisibility(View.GONE);
            updateList(json);
        } else {
            showNotify(error);
        }
    }

    private void updateList(String json) {
        ArrayList<Lover> newList = LoadJson.jsonToListLover(json);
        list.clear();
        list.addAll(newList);
        adapter.notifyDataSetChanged();
        if (list.size() == 0) {
            showNotify(context.getResources().getString(R.string.no_lover));
        }
    }

    private void showNotify(String notify) {
        layoutProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        tvNotify.setVisibility(View.VISIBLE);
        tvNotify.setText(notify);
    }

    /**
     * double click back then exit app (2 click in 2s to exit)
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Var.showToast(context, context.getResources().getString(R.string.back_to_exit));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private boolean doubleBackToExitPressedOnce;
}
