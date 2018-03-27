package luongduongquan.com.quanchat.Utils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by luong.duong.quan on 3/27/2018.
 */

public class OfflineData extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		// Picasso load picture offline
		Picasso.Builder builder = new Picasso.Builder(this);
		builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
		Picasso imageOfflineBuilt = builder.build();

		imageOfflineBuilt.setIndicatorsEnabled(true);
		imageOfflineBuilt.setLoggingEnabled(true);
		Picasso.setSingletonInstance(imageOfflineBuilt);

	}
}
