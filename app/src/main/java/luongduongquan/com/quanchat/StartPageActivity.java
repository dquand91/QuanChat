package luongduongquan.com.quanchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartPageActivity extends AppCompatActivity implements View.OnClickListener {

	Button btnHadAccount;
	Button btnNewAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_page);

		btnHadAccount = findViewById(R.id.btnHadAccount);
		btnNewAccount = findViewById(R.id.btnNeedNewAccount);

		btnHadAccount.setOnClickListener(this);
		btnNewAccount.setOnClickListener(this);



	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btnHadAccount:
				break;
			case R.id.btnNeedNewAccount:
				break;
		}

	}
}
