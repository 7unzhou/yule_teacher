package com.yulebaby.teacher;




import com.yulebaby.teacher.fragment.GuideFragment;
import com.yulebaby.teacher.fragment.SalaryFragment;
import com.yulebaby.teacher.fragment.TeachingFragment;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
    	View icon = this.findViewById(R.id.home_yulebaby);
    	icon.setOnClickListener(this);
    	View msg = this.findViewById(R.id.bt_msg);
    	msg.setOnClickListener(this);
    	
    	
	}
    
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initView();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent(this, YuLeActivity.class);
		switch(id){
		case R.id.home_yulebaby:
//			intent = new Intent(this, YuLeAboutActivity.class);
//			this.startActivity(intent);
			
			
			//育婴指南
			intent.putExtra("home", GuideFragment.INDEX);
			this.startActivity(intent);
			break;
		case R.id.bt_msg:
			intent.putExtra("home", TeachingFragment.INDEX);
			this.startActivity(intent);
			break;
		}
		
	}
	
	
	private long mFirstTime = 0;
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - mFirstTime > 2000) {
				Toast.makeText(this, R.string.backkey_again, Toast.LENGTH_SHORT).show();
				mFirstTime = secondTime;
				return true;
			} else {
				this.finish();
			}
		}
		return super.onKeyUp(keyCode, event);
	}

    
}
