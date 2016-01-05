package br.com.pelisoli.realm.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.com.pelisoli.realm.BuildConfig;
import br.com.pelisoli.realm.R;
import br.com.pelisoli.realm.adapter.SearchAdapter;
import br.com.pelisoli.realm.model.Person;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by pelisoli on 19/11/15.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSearch;

    private Button btnSearch;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private SearchAdapter searchAdapter;

    private ProgressDialog alertDialog;

    private RealmResults<Person> personResult;

    private Realm realm;

    private static boolean SHOW_PROGRESS_DIALOG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        //UI components
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        //Button listener
        btnSearch.setOnClickListener(this);

        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recycler view with all addresses found in search
        searchAdapter = new SearchAdapter(getApplicationContext(), personResult);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setVisibility(View.VISIBLE);

        alertDialog = new ProgressDialog(this);
        alertDialog.setMessage(getString(R.string.loading));
        alertDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSearch:
                recyclerView.setVisibility(View.GONE);

                //It's not allowed an empty string
                if(edtSearch.getText().toString().equals("")){

                    //Show warning message
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.empty_search));
                    builder.setPositiveButton(R.string.ok_label, null);
                    builder.show();

                }else {

                    if(personResult != null){
                        personResult.removeChangeListeners();
                    }

                    alertDialog.show();

                    //Asynchronous query
                    personResult = realm.where(Person.class).contains("name",
                            edtSearch.getText().toString()).findAllAsync();

                    personResult.addChangeListener(queryCallback);
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(personResult != null){
            personResult.removeChangeListeners();
        }

        if(realm != null){
            realm.close();
        }
    }

    private RealmChangeListener queryCallback = new RealmChangeListener() {
        @Override
        public void onChange() {
            // called once the query complete and on every update

            //Hide progress bar
            alertDialog.cancel();

            //Add address list to recycler view
            searchAdapter.addPersonList(personResult);

            //show recycler view
            recyclerView.setVisibility(View.VISIBLE);
        }
    };
}
