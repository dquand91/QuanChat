package luongduongquan.com.quanchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAuth = FirebaseAuth.getInstance();
	}

	@Override
	protected void onStart() {
		super.onStart();

		FirebaseUser currentUser = mAuth.getCurrentUser();

		if(currentUser == null){
			Intent intentStartPage = new Intent(MainActivity.this, StartPageActivity.class);
			intentStartPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // After start other Activity Do not allow user press back to back this Main Activity
			startActivity(intentStartPage);
			finish();
		}
	}

}
