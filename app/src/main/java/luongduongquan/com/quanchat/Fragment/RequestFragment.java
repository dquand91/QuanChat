package luongduongquan.com.quanchat.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import luongduongquan.com.quanchat.Model.RequestFriend;
import luongduongquan.com.quanchat.R;
import luongduongquan.com.quanchat.Utils.Common;
import luongduongquan.com.quanchat.ViewHolder.RequestFriendViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

	private static final String TAG = "RequestFragment";

	private View mMainView;

	private FirebaseAuth mAuth;
	private DatabaseReference requestFriendDatabase;
	private DatabaseReference userListDatabaseReference;
	private DatabaseReference friendDatabaseReference;

	private RecyclerView listRequestFriend;

	private String localUserID = "";

	FirebaseRecyclerAdapter<RequestFriend, RequestFriendViewHolder> requestAdapter;


	public RequestFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		mMainView = inflater.inflate(R.layout.fragment_request, container, false);

		mAuth = FirebaseAuth.getInstance();
		localUserID = mAuth.getCurrentUser().getUid();

		requestFriendDatabase = FirebaseDatabase.getInstance().getReference().child(Common.FRIEND_REQUEST_TAG);
		requestFriendDatabase.keepSynced(true);

		// For Friend Data base
		friendDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Common.FRIENDS_TAG);
		friendDatabaseReference.keepSynced(true);

		userListDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Common.USERS_TAG);
		userListDatabaseReference.keepSynced(true);

		listRequestFriend = mMainView.findViewById(R.id.listRequest_RequestFragment);
		listRequestFriend.setHasFixedSize(true);
		listRequestFriend.setLayoutManager(new LinearLayoutManager(mMainView.getContext()));

		requestAdapter = new FirebaseRecyclerAdapter<RequestFriend, RequestFriendViewHolder>(
				RequestFriend.class,
				R.layout.item_request_friend,
				RequestFriendViewHolder.class,
				requestFriendDatabase.child(localUserID)
		) {
			@Override
			protected void populateViewHolder(final RequestFriendViewHolder viewHolder, final RequestFriend model, final int position) {
				viewHolder.initView();

				viewHolder.setCustomOnClick(new RequestFriendViewHolder.CustomOnClick() {
					@Override
					public void OnClickListenerCustom(View view, int position) {
						Toast.makeText(mMainView.getContext(), "position = " + position , Toast.LENGTH_SHORT).show();
					}
				});

				final String opposite_user_id = getRef(position).getKey();
				userListDatabaseReference.child(opposite_user_id).addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if(dataSnapshot != null){
							String userName = dataSnapshot.child(Common.USER_NAME_TAG).getValue().toString();
							String status = dataSnapshot.child(Common.USER_STATUS_TAG).getValue().toString();
							String imgURL = dataSnapshot.child(Common.USER_IMAGE_TAG).getValue().toString();

							viewHolder.setUserName(userName);
							viewHolder.setTvStatus(status);
							viewHolder.setAvatar(mMainView.getContext(), imgURL);
							if(model.getRequest_type().equals(Common.REQUEST_SEND_TAG)){
								viewHolder.setButtonAccept(View.GONE, null);

								viewHolder.setButtonCancel(View.VISIBLE, new View.OnClickListener() {
									@Override
									public void onClick(View v) {
//										Toast.makeText(mMainView.getContext(), "Cancel", Toast.LENGTH_SHORT).show();
										cancelRequest(localUserID, opposite_user_id);
									}
								});
							} else {
								viewHolder.setButtonAccept(View.VISIBLE, new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										// Accept friend
										acceptRequest(localUserID, opposite_user_id);

									}
								});
								viewHolder.setButtonCancel(View.VISIBLE, new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										// Cancel friend request
										cancelRequest(localUserID, opposite_user_id);

									}
								});
							}
						}

					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
			}
		};


		listRequestFriend.setAdapter(requestAdapter);
		requestAdapter.notifyDataSetChanged();

		return mMainView;
	}

	private void acceptRequest(final String localUserID, final String remoteUserID) {

		Calendar calendar = Calendar.getInstance();
		final SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm:ss");
		final String saveCurrentDate = currentDateFormat.format(calendar.getTime());

		friendDatabaseReference.child(localUserID).child(remoteUserID).child(Common.DATE_TAG).setValue(saveCurrentDate)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if(task.isSuccessful()){
							friendDatabaseReference.child(remoteUserID).child(localUserID).child(Common.DATE_TAG).setValue(saveCurrentDate)
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if(task.isSuccessful()){
												// After make friend success, remove Friend request.
												cancelRequest(localUserID, remoteUserID);
											}
										}
									});
						} else {
							Toast.makeText(mMainView.getContext(), "Error, ", Toast.LENGTH_SHORT).show();
						}
					}
				});


	}

	private void cancelRequest(final String localUserID, final String remoteUserID) {

		requestFriendDatabase.child(localUserID).child(remoteUserID).removeValue()
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()){
							requestFriendDatabase.child(remoteUserID).child(localUserID).removeValue()
									.addOnCompleteListener(new OnCompleteListener<Void>() {
										@Override
										public void onComplete(@NonNull Task<Void> task) {
											if (task.isSuccessful()){
												requestAdapter.notifyDataSetChanged();
											} else {
												Toast.makeText(mMainView.getContext(), "Error while remove request...", Toast.LENGTH_SHORT).show();
											}
										}
									});
						} else {
							Toast.makeText(mMainView.getContext(), "Error while remove request...", Toast.LENGTH_SHORT).show();
						}
					}
				});

	}

}
