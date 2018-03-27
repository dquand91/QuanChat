package luongduongquan.com.quanchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import luongduongquan.com.quanchat.Utils.Common;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

	private CircleImageView imgUser;
	private TextView tvUserName;
	private TextView tvUserStatus;
	private Button btnChangeImage, btnChangeStatus;

	private DatabaseReference userDataPreference;
	private FirebaseAuth mAuth;

	private StorageReference storageReference;

	private static final int GALLERY_PICK = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		imgUser = findViewById(R.id.imgUser_Settings);
		tvUserName = findViewById(R.id.tvUserName_Settings);
		tvUserStatus = findViewById(R.id.tvUserStatus_Settings);
		btnChangeImage = findViewById(R.id.btnChangeImage_Settings);
		btnChangeStatus = findViewById(R.id.btnChangeStatus_Settings);

		btnChangeStatus.setOnClickListener(this);
		btnChangeImage.setOnClickListener(this);


		mAuth = FirebaseAuth.getInstance();
		String current_user_id = mAuth.getCurrentUser().getUid();
		userDataPreference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG).child(current_user_id);
		userDataPreference.keepSynced(true);

		storageReference = FirebaseStorage.getInstance().getReference().child("Profile_Image"); // Profile_Image là tên do mình đặt để tạo thư mục tên "Profile_Image" trên FireBase storage.



		userDataPreference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null){
					String name = dataSnapshot.child(Common.USER_NAME_TAG).getValue().toString();
					String status = dataSnapshot.child(Common.USER_STATUS_TAG).getValue().toString();
					final String userImage = dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString();
					String userThumb = dataSnapshot.child(Common.USER_THUMB_IMAGE_TAG).getValue().toString();

					tvUserName.setText(name);
					tvUserStatus.setText(status);
//					Picasso.with(SettingsActivity.this).load(userImage).placeholder(R.drawable.user).into(imgUser);
					Picasso.with(SettingsActivity.this).load(userImage)
							.networkPolicy(NetworkPolicy.OFFLINE)
							.placeholder(R.drawable.user)
							.into(imgUser, new Callback() {
								@Override
								public void onSuccess() {

								}

								@Override
								public void onError() {
									Picasso.with(SettingsActivity.this).load(userImage).placeholder(R.drawable.user).into(imgUser);
								}
							});

				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null){
			Uri imageURI = data.getData();

			// Tới đây sẽ gọi tới Activity để Crop Image.
			// Crop xong image thì nó sẽ quay lại app và sẽ quay lại callBack onActivityResult() này tiếp.
			// Nó sẽ xuống chỗ "CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE"
			CropImage.activity()
					.setGuidelines(CropImageView.Guidelines.ON)
					.setAspectRatio(1,1)
					.start(this);
		}

		// Cái này là sau khi đã Crop Image bằng Activity CropImage xong sẽ trả ra result CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
		// Rồi sau đó app của mình sẽ xử lý
		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK) {
				Uri resultUri = result.getUri();

				String currentUserID = mAuth.getCurrentUser().getUid();
				StorageReference filePath = storageReference.child(currentUserID + ".jpg");
				filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
						if(task.isSuccessful()){
							Toast.makeText(SettingsActivity.this, "Saving your image to FireBase Storage...", Toast.LENGTH_LONG).show();

							String downloadURI = task.getResult().getDownloadUrl().toString();
							userDataPreference.child(Common.USER_IMAGE_TAG).setValue(downloadURI).addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									if(task.isSuccessful()){
										Toast.makeText(SettingsActivity.this, "Image update complete...", Toast.LENGTH_LONG).show();

									}
								}
							});

						} else {
							Toast.makeText(SettingsActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
						}
					}
				});

			} else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
				Exception error = result.getError();
			}
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btnChangeImage_Settings:
//				Intent intentToGallery = new Intent();
//				intentToGallery.setAction(Intent.ACTION_GET_CONTENT);
//				intentToGallery.setType("image/*");
//				startActivityForResult(intentToGallery, GALLERY_PICK);
				CropImage.activity()
						.setGuidelines(CropImageView.Guidelines.ON)
						.setAspectRatio(1,1)
						.start(this);
				break;
			case R.id.btnChangeStatus_Settings:

				String current_status = tvUserStatus.getText().toString();

				Intent intentToStatusActivity = new Intent(SettingsActivity.this, StatusActivity.class);
				if (!current_status.isEmpty()){
					intentToStatusActivity.putExtra(Common.USER_STATUS_TAG, current_status);
				}

				startActivity(intentToStatusActivity);

				break;
		}
	}
}
