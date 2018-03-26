package luongduongquan.com.quanchat.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import luongduongquan.com.quanchat.Model.User;
import luongduongquan.com.quanchat.R;

/**
 * Created by luong.duong.quan on 3/23/2018.
 */

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.UserViewHolder> {

	Context mContext;

	public interface CustomOnClick{
		void OnClickListenerCustom(int position);
	}

	private List<User> mListUser;
	private CustomOnClick mCustomOnClick;

	public AllUserAdapter(Context context, List<User> listUser){
		mContext = context;
		mListUser = listUser;
	}

	public void setCustomOnClick(CustomOnClick customOnClick){
		mCustomOnClick = customOnClick;
	}

	public class UserViewHolder extends RecyclerView.ViewHolder{

		private CircleImageView imgUser_holder;
		private TextView tvUserName, tvUserStatus;


		public UserViewHolder(View itemView) {
			super(itemView);
			imgUser_holder = itemView.findViewById(R.id.userImage_item);
			tvUserName = itemView.findViewById(R.id.userName_item);
			tvUserStatus = itemView.findViewById(R.id.userStatus_item);

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mCustomOnClick.OnClickListenerCustom(getPosition());
				}
			});
		}
	}

	@NonNull
	@Override
	public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);

		return new UserViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

		User user = mListUser.get(position);
		holder.tvUserName.setText(user.getUserName());
		holder.tvUserStatus.setText(user.getUserStatus());
		Picasso.with(mContext).load(Uri.parse(user.getUserImage())).placeholder(R.drawable.user).into(holder.imgUser_holder);
//		Picasso.with(holder.imgUser_holder.getContext()).load(user.getUserImage());

	}

	@Override
	public int getItemCount() {
		return mListUser.size();
	}



}
