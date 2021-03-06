package luongduongquan.com.quanchat.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import luongduongquan.com.quanchat.R;

/**
 * Created by luong.duong.quan on 4/5/2018.
 */

public class FriendsViewHolder extends RecyclerView.ViewHolder{

	private CircleImageView imgUser_holder;
	private TextView tvUserName, tvDate;
	private CustomOnClick mCustomOnClick;
	private ImageView imgStatus;
	public View mView;

	public interface CustomOnClick{
		public void OnClickListenerCustom(View view, int position);
	}

	public FriendsViewHolder(View itemView) {
		super(itemView);
		mView = itemView;
		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCustomOnClick.OnClickListenerCustom(v, getAdapterPosition());
			}
		});
	}

	public void setUserName(String userName){
		tvUserName = mView.findViewById(R.id.userName_item);
		tvUserName.setText(userName);
	}

	public void setAvatar(Context context, String urlImage){
		imgUser_holder = mView.findViewById(R.id.userImage_item);
		Picasso.with(context).load(urlImage).into(imgUser_holder);
	}

	public void setTvDate(String date){
		tvDate = mView.findViewById(R.id.userStatus_item);
		tvDate.setText(date);
	}

	public void setStatus(boolean isOnline){
		imgStatus = mView.findViewById(R.id.imgOnlineStatus_item);
		if(isOnline){
			imgStatus.setVisibility(View.VISIBLE);
		} else {
			imgStatus.setVisibility(View.INVISIBLE);
		}
	}

	public void setCustomOnClick(CustomOnClick customOnClick){
		mCustomOnClick = customOnClick;
	}

}