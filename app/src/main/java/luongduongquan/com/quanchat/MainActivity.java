package luongduongquan.com.quanchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

	private FirebaseAuth mAuth;

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAuth = FirebaseAuth.getInstance();

		mToolbar = findViewById(R.id.appbar_main);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("myChatApp");
	}

	@Override
	protected void onStart() {
		super.onStart();

		FirebaseUser currentUser = mAuth.getCurrentUser();

		if(currentUser == null){
			LogoutUser();
		}
	}

	private void LogoutUser() {
		Intent intentStartPage = new Intent(MainActivity.this, StartPageActivity.class);
		intentStartPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // After start other Activity Do not allow user press back to back this Main Activity
		startActivity(intentStartPage);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		if(item.getItemId() == R.id.logout_menu_main){
			mAuth.signOut();
			LogoutUser();
		} else if (item.getItemId() == R.id.acc_settings_menu_main){

		}
		return true;
	}
}
