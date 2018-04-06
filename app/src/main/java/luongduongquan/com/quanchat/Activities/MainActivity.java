package luongduongquan.com.quanchat.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import luongduongquan.com.quanchat.Adapter.TabsPagerAdapter;
import luongduongquan.com.quanchat.R;
import luongduongquan.com.quanchat.Utils.Common;

public class MainActivity extends AppCompatActivity {

	private FirebaseAuth mAuth;

	private Toolbar mToolbar;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private TabsPagerAdapter mTabsAdapter;
	private DatabaseReference userReference;

	private FirebaseUser currentUser;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAuth = FirebaseAuth.getInstance();

		//Tabs in MainActivity
		mViewPager = findViewById(R.id.tabPager_view_main);
		mTabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mTabsAdapter);
		mTabLayout = findViewById(R.id.tabLayout_main);
		mTabLayout.setTabTextColors(Color.WHITE, Color.BLACK);
		mTabLayout.setupWithViewPager(mViewPager);

		mToolbar = findViewById(R.id.appbar_main);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("myChatApp");

		currentUser = mAuth.getCurrentUser();

		if(currentUser != null){
			String current_user_id = mAuth.getCurrentUser().getUid();
			userReference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(current_user_id);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(currentUser == null){
			LogoutUser();
		} else if(currentUser != null){
			userReference.child(Common.ONLINE_TAG).setValue(true);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		if(currentUser != null){
			userReference.child(Common.ONLINE_TAG).setValue(System.currentTimeMillis());
		}
	}

	private void LogoutUser() {
		if(currentUser != null){
			userReference.child(Common.ONLINE_TAG).setValue(System.currentTimeMillis());
		}
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

		if(item.getItemId() == R.id.menu_logout_main){
			mAuth.signOut();
			LogoutUser();
		} else if (item.getItemId() == R.id.menu_account_setting_main){
			Intent intentToSettings = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intentToSettings);
		} else if (item.getItemId() == R.id.menu_userlist_main){
			Intent intentToUserList = new Intent(MainActivity.this, AllUserActivity.class);
			startActivity(intentToUserList);
		}
		return true;
	}
}
