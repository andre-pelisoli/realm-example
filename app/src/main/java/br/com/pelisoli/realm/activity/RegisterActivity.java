package br.com.pelisoli.realm.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import br.com.pelisoli.realm.BuildConfig;
import br.com.pelisoli.realm.R;
import br.com.pelisoli.realm.model.City;
import br.com.pelisoli.realm.model.Person;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by pelisoli on 09/12/15.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

	private EditText edtDoc;

	private EditText edtName;

	private EditText edtCity;

	private Button btnSave;

	private ProgressDialog alertDialog;

	private RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

		edtDoc = (EditText) findViewById(R.id.edtDoc);

		edtName = (EditText) findViewById(R.id.edtName);

		edtCity = (EditText) findViewById(R.id.edtCity);

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		getSupportActionBar().setTitle(R.string.app_name);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		alertDialog = new ProgressDialog(this);
		alertDialog.setMessage(getString(R.string.saving));
		alertDialog.setCancelable(false);

	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btnSave:

				//Close soft keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

				Person person = new Person(edtDoc.getText().toString(),
						edtName.getText().toString(), new City(edtCity.getText().toString().toUpperCase()));


				alertDialog.show();

				new RegisterAsyncTask().execute(person);
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class RegisterAsyncTask extends AsyncTask<Person, Void, Boolean>{

		@Override
		protected Boolean doInBackground(final Person... persons) {
			boolean status = false;
			Realm realm = Realm.getDefaultInstance();

			try{
				realm.beginTransaction();

				for(Person person : persons) {
					RealmResults<City> cityResults = realm.where(City.class).equalTo("name",
							person.getCity().getName()).findAll();

					if(cityResults != null && cityResults.size() > 0) {

						for(City city: cityResults){
							person.setCity(city);
						}
					} else {
						person.getCity().setId(java.util.UUID.randomUUID().toString());
						realm.copyToRealm(person.getCity());
					}

					realm.copyToRealmOrUpdate(person);
				}

				realm.commitTransaction();
				status = true;

			}catch (Exception e){
				if(e != null && e.getMessage() != null) {
					Log.e(BuildConfig.LOG_TAG, e.getMessage());
				}

				if(realm != null){
					realm.cancelTransaction();
				}
			}

			return status;
		}

		@Override
		protected void onPostExecute(Boolean status) {
			super.onPostExecute(status);

			alertDialog.cancel();

			if(status){
				Snackbar.make(relativeLayout, R.string.success_on_saving, Snackbar.LENGTH_SHORT).show();
			}else{
				Snackbar.make(relativeLayout, R.string.error_on_saving, Snackbar.LENGTH_SHORT).show();
			}

		}
	}
}
