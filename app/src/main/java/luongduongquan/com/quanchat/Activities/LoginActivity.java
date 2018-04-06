package luongduongquan.com.quanchat.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import luongduongquan.com.quanchat.R;
import luongduongquan.com.quanchat.Utils.Common;

public class LoginActivity extends AppCompatActivity {

	private android.support.v7.widget.Toolbar mToolbar;

	private Button btnLogin;
	private EditText edtEmail, edtPassword;

	private FirebaseAuth mAuth;
	private ProgressDialog loadingBar;

	private DatabaseReference userDataReference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		mToolbar = findViewById(R.id.appbar_login);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Login");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		btnLogin = findViewById(R.id.btnSignIn_login);
		edtEmail = findViewById(R.id.edtEmail_login);
		edtPassword = findViewById(R.id.edtPassword_login);

		mAuth = FirebaseAuth.getInstance();

		userDataReference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG);

		loadingBar = new ProgressDialog(this);

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				loadingBar.setTitle("Logging account");
				loadingBar.setMessage("Loading...");
				loadingBar.show();

				String email = edtEmail.getText().toString();
				String password = edtPassword.getText().toString();
				
				LoginUserAccount(email, password);

			}
		});

	}

	private void LoginUserAccount(String email, String password) {

		if(email.isEmpty()){
			Toast.makeText(LoginActivity.this, "Please input Email!", Toast.LENGTH_SHORT).show();
		}
		if (password.isEmpty()){
			Toast.makeText(LoginActivity.this, "Please input Password!", Toast.LENGTH_SHORT).show();
		} else {
			mAuth.signInWithEmailAndPassword(email, password)
					.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if(task.isSuccessful()){
						String currenUserID = mAuth.getCurrentUser().getUid();
						String device_token = FirebaseInstanceId.getInstance().getToken();

						userDataReference.child(currenUserID).child(Common.DEVICE_TOKEN_TAG).setValue(device_token)
								.addOnCompleteListener(new OnCompleteListener<Void>() {
									@Override
									public void onComplete(@NonNull Task<Void> task) {
										if(task.isSuccessful()){
											loadingBar.dismiss();
											Intent intentToMain = new Intent(LoginActivity.this, MainActivity.class);
											intentToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
											startActivity(intentToMain);
											finish();
										} else {
											loadingBar.dismiss();
											Toast.makeText(LoginActivity.this, "Error,...", Toast.LENGTH_SHORT).show();
										}
									}
								});


					}	else {
						loadingBar.dismiss();
						Toast.makeText(LoginActivity.this, "Error, Login Fail, please check email and password.", Toast.LENGTH_SHORT).show();
					}

				}
			});
		}

	}
}
