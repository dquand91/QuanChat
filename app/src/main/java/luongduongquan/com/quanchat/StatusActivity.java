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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import luongduongquan.com.quanchat.Utils.Common;

public class StatusActivity extends AppCompatActivity {

	private android.support.v7.widget.Toolbar mToolBar;
	private Button btnSaveChange;
	private EditText edtInputChange;

	private FirebaseAuth mAuth;
	private DatabaseReference databaseReference;
	private ProgressDialog loadingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

		mAuth = FirebaseAuth.getInstance();
		String currentUserID = mAuth.getCurrentUser().getUid();
		databaseReference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(currentUserID);

		loadingBar = new ProgressDialog(this);

		mToolBar = findViewById(R.id.appBar_status);
		setSupportActionBar(mToolBar);
		getSupportActionBar().setTitle("Change Status");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		btnSaveChange = findViewById(R.id.btnSaveChange_Status);
		edtInputChange = findViewById(R.id.edtChangeStatus_Status);

		String old_status = "";
		if(!getIntent().getExtras().getString(Common.USER_STATUS_TAG).isEmpty()){
				old_status = getIntent().getExtras().getString(Common.USER_STATUS_TAG);
		}
		edtInputChange.setText(old_status);

		btnSaveChange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String statusText = edtInputChange.getText().toString();

				changeProfileStatus(statusText);

			}
		});

	}

	private void changeProfileStatus(String statusText) {

		if (statusText.isEmpty()){
			Toast.makeText(StatusActivity.this, "Please input status...", Toast.LENGTH_SHORT).show();
		} else {
			loadingBar.setTitle("Changing status");
			loadingBar.setMessage("Please wait...");
			databaseReference.child(Common.USER_STATUS_TAG).setValue(statusText).addOnCompleteListener(new OnCompleteListener<Void>() {
				@Override
				public void onComplete(@NonNull Task<Void> task) {
					if(task.isSuccessful()){
						loadingBar.dismiss();
						Intent intentToSetting = new Intent(StatusActivity.this, SettingsActivity.class);
						startActivity(intentToSetting);

						Toast.makeText(StatusActivity.this, "Update status complete...", Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(StatusActivity.this, "Update status error...", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

	}

}
