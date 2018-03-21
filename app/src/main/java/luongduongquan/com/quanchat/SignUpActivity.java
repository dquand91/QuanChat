package luongduongquan.com.quanchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

	private android.support.v7.widget.Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		mToolbar = findViewById(R.id.appbar_signup);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Sign Up");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}
}
