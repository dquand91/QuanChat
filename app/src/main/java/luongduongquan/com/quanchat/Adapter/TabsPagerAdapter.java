package luongduongquan.com.quanchat.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import luongduongquan.com.quanchat.Fragment.ChatFragment;
import luongduongquan.com.quanchat.Fragment.FriendsFragment;
import luongduongquan.com.quanchat.Fragment.RequestFragment;

/**
 * Created by luong.duong.quan on 3/22/2018.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {


	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position){
			case 0:
				return new RequestFragment();
			case 1:
				return new ChatFragment();
			case 2:
				return new FriendsFragment();
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position){
			case 0:
				return "Request";
			case 1:
				return "Chat";
			case 2:
				return "Friends";
			default:
				return null;
		}
	}

}
