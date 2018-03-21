package luongduongquan.com.quanchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

	private android.support.v7.widget.Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		mToolbar = findViewById(R.id.appbar_login);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Login");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
