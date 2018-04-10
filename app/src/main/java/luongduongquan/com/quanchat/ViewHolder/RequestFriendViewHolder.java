package luongduongquan.com.quanchat.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import luongduongquan.com.quanchat.R;

public class RequestFriendViewHolder extends RecyclerView.ViewHolder {


	private CircleImageView imgUser_holder;
	private TextView tvUserName, tvStatus;
	private Button btnAccept, btnCancel;
	private RequestFriendViewHolder.CustomOnClick mCustomOnClick;
	public View mView;

	public interface CustomOnClick{
		public void OnClickListenerCustom(View view, int position);
	}

	public RequestFriendViewHolder(View itemView) {
		super(itemView);
		mView = itemView;
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCustomOnClick.OnClickListenerCustom(v, getAdapterPosition());
			}
		});
	}

	public void initView(){
		tvUserName = mView.findViewById(R.id.userName_itemRequestFriend);
		imgUser_holder = mView.findViewById(R.id.userImage_itemRequestFriend);
		tvStatus = mView.findViewById(R.id.userStatus_itemRequestFriend);
		btnAccept = mView.findViewById(R.id.btnAccept_itemRequestFriend);
		btnCancel = mView.findViewById(R.id.btnCancel_itemRequestFriend);
	}

	public void setUserName(String userName){
		tvUserName.setText(userName);
	}

	public void setAvatar(Context context, String urlImage){
		Picasso.with(context).load(urlImage).into(imgUser_holder);
	}

	public void setTvStatus(String status){
		tvStatus.setText(status);
	}

	public void setButtonAccept(int visibility, View.OnClickListener clickListener){
		btnAccept.setVisibility(visibility);
		if(visibility == View.VISIBLE){
			btnAccept.setOnClickListener(clickListener);
		}
	}

	public void setButtonCancel(int visibility, View.OnClickListener clickListener){
		btnAccept.setVisibility(visibility);
		btnCancel.setOnClickListener(clickListener);

	}

	public void setCustomOnClick(RequestFriendViewHolder.CustomOnClick customOnClick){
		mCustomOnClick = customOnClick;
	}
}
