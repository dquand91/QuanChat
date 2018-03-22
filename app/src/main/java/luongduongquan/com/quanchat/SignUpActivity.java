package luongduongquan.com.quanchat;

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

public class SignUpActivity extends AppCompatActivity {

	private android.support.v7.widget.Toolbar mToolbar;

	EditText edtName, edtEmail, edtPassword;
	Button btnCreate;

	private FirebaseAuth mAuth;
	private ProgressDialog loadingBar;

	private DatabaseReference storeUserDefaultDataReference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		mAuth = FirebaseAuth.getInstance();

		mToolbar = findViewById(R.id.appbar_signup);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setTitle("Sign Up");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		edtName = findViewById(R.id.edtName_signup);
		edtEmail = findViewById(R.id.edtEmail_signup);
		edtPassword = findViewById(R.id.edtPassword_signup);
		btnCreate = findViewById(R.id.btnCreate_signup);

		loadingBar = new ProgressDialog(this);

		btnCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = edtName.getText().toString();
				String email = edtEmail.getText().toString();
				String password = edtPassword.getText().toString();
				
				registerAccount(name, email, password);
			}
		});

	}

	private void registerAccount(final String name, final String email, final String password) {

		if (name.isEmpty()){
			Toast.makeText(SignUpActivity.this, "Please input name", Toast.LENGTH_SHORT).show();
		}
		if (email.isEmpty()){
			Toast.makeText(SignUpActivity.this, "Please input email", Toast.LENGTH_SHORT).show();
		}
		if (password.isEmpty()){
			Toast.makeText(SignUpActivity.this, "Please input password", Toast.LENGTH_SHORT).show();
		} else {
			loadingBar.setTitle("Creating new account");
			loadingBar.setMessage("Please wait...");
			loadingBar.show();
			mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if(task.isSuccessful()){

						storeUserDefault_StartMainActivity(name,  email,  password);

					} else {
						loadingBar.dismiss();
						Toast.makeText(SignUpActivity.this, "Error, try again", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}


	}

	private void storeUserDefault_StartMainActivity(String name, String email, String password) {
		String currentUserID = mAuth.getCurrentUser().getUid();
		storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
		storeUserDefaultDataReference.child("user_name").setValue(name);
		storeUserDefaultDataReference.child("user_status").setValue("This is the status. Have a nice day.");
		storeUserDefaultDataReference.child("user_image").setValue("default_profile");
		storeUserDefaultDataReference.child("user_thumb_image").setValue("default_image").addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()){
					loadingBar.dismiss();
					Intent intentMain = new Intent(SignUpActivity.this, MainActivity.class);
					intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intentMain);
					finish();
				} else {
					loadingBar.dismiss();
					Toast.makeText(SignUpActivity.this, "Error, try again", Toast.LENGTH_SHORT).show();
				}
			}
		});



	}
}
