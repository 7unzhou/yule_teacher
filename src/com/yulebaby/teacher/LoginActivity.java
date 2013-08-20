package com.yulebaby.teacher;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.fragment.TeachingFragment;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.Login;
import com.yulebaby.teacher.model.Version;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.LoginService;
import com.yulebaby.teacher.server.UpdateService;

public class LoginActivity extends FragmentActivity implements OnClickListener {

	private static final String TAG = App.TAG;
	private EditText mPassportText;
	private EditText mPasswordText;
	private Button mLoginBtn;
	private LoginTask mLoginTask;
	private WeakReference<Activity> mWeakActivity;
	private SharedPreferences preferences;

	private class LoginTask extends AsyncTask<String, Void, ModelData<Login>> {
		private Context context;
		private String passport, password;

		public LoginTask(Context context) {
			this.context = context;
		}

		@Override
		protected ModelData<Login> doInBackground(String... params) {
			this.passport = params[0];
			this.password = params[1];
			String chineseTime = preferences.getString(Commons.chineseTime, "");
			String englishTime = preferences.getString(Commons.englishTime, "");
			String waterTime = preferences.getString(Commons.waterTime, "");
			ModelData<Login> login = LoginService.login(context, passport,
					password,chineseTime,englishTime,waterTime);
			if (login != null) {
				SharedPreferences share = context.getSharedPreferences(
						App.Name, 0);
				boolean isFirst = share.getBoolean("is_first", true);
				login.setTag(isFirst);
				share.edit().putBoolean("is_first", false).commit();
				if (login != null) {
					if (login.getResultCode() == Response.OK) {
						
						//System.out.println("App.getInstance().setLogin(login.getInfo())"+login.getInfo().getImgUrl());
						App.getInstance().setLogin(login.getInfo());
					}
				}
			}
			return login;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mLoginBtn.setText(getResources().getString(R.string.login_ing));
			mLoginBtn.setEnabled(false);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(ModelData<Login> result) {
			if (mWeakActivity.get() == null
					|| mWeakActivity.get().isFinishing()) {
				return;
			}
			mLoginBtn.setText(getResources().getString(R.string.login));
			mLoginBtn.setEnabled(true);
			if (result == null) {
				Toast.makeText(context, App.mNetWorkIssue, Toast.LENGTH_SHORT)
						.show();
				Log.d(TAG, "onPostExecute result == null");
				return;
			}
			switch (result.getResultCode()) {
			case Response.NotLogin:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.OK:
				App.getInstance().getUserInfo().setLoginName(passport);
				App.getInstance().getUserInfo().setPassword(password);
				App.getInstance().getUserInfo().save(LoginActivity.this);
				boolean isFirst = (Boolean) result.getTag();
				if (isFirst) {
					Intent intent = new Intent(context, YuLeGuideActivity.class);
					context.startActivity(intent);
					((Activity) context).finish();
				} else {
//					Intent intent = new Intent(context, MainActivity.class);
//					context.startActivity(intent);
					
					Intent intent = new Intent(context, YuLeActivity.class);
					intent.putExtra("home", TeachingFragment.INDEX);
					context.startActivity(intent);
					((Activity) context).finish();
					// Toast.makeText(context, result.getMessage(),
					// Toast.LENGTH_SHORT).show();
				}
				break;
			case Response.ServerError:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG)
						.show();
				break;
			case Response.LogicalErrors:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG)
						.show();
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		if (mWeakActivity == null) {
			mWeakActivity = new WeakReference<Activity>(this);
		}
		
		preferences = this.getSharedPreferences(
				Commons.TEACHER_PREFERE, Context.MODE_PRIVATE);
		initView();
	}

	void showDialog(Version version) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = newInstance(version.getUrl(),
				version.getVersionCode(), version.isForce());
		newFragment.show(ft, "dialog");
	}

	private UpdateDialogFragment newInstance(String url, String v,
			boolean isForce) {
		UpdateDialogFragment f = new UpdateDialogFragment();
		Bundle args = new Bundle();
		args.putString("url", url);
		args.putString("v", v);
		args.putBoolean("force", isForce);
		f.setArguments(args);
		return f;
	}

	public class UpdateDialogFragment extends DialogFragment {
		String mUrl;
		String mVersion;
		boolean mIsForce;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mUrl = getArguments().getString("url");
			mVersion = getArguments().getString("v");
			mIsForce = getArguments().getBoolean("force");
		}

		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
			alert.setCanceledOnTouchOutside(true);
			alert.setTitle(getString(R.string.dialog_update));
			alert.setMessage(getString(R.string.dialog_update_v));
			alert.setButton(DialogInterface.BUTTON_POSITIVE,
					getString(R.string.dialog_confirm),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UpdateDialogFragment.this.dismiss();
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri
									.parse(mUrl));
							startActivity(intent);
							if (mIsForce) {
								LoginActivity.this.finish();
							} else {
								login();
							}
						}
					});
			alert.setButton(DialogInterface.BUTTON_NEGATIVE,
					getString(R.string.dialog_cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							UpdateDialogFragment.this.dismiss();
							if (mIsForce) {
								LoginActivity.this.finish();
							} else {
								login();
							}
						}
					});
			return alert;
		}
	}

	private void initView() {
		mPassportText = (EditText) this.findViewById(R.id.login_name);
		mPasswordText = (EditText) this.findViewById(R.id.login_password);
		mLoginBtn = (Button) this.findViewById(R.id.login_confirm);
		SharedPreferences share = this.getSharedPreferences(App.Name, 0);
		String name = share.getString("name", "");
		String pwd = share.getString("password", "");
		String cookie = share.getString("cookie", "");
		mPassportText.setText(name);
		mPasswordText.setText(pwd);
		mLoginBtn.setOnClickListener(this);
		mLoginTask = new LoginTask(this);
		mLoginBtn.setText(getResources().getString(R.string.login));
		boolean autoLogin = getIntent().getBooleanExtra("auto", true);
		if (autoLogin && !TextUtils.isEmpty(cookie)) {
			//checkVersion();
			login();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login_confirm:
			//checkVersion();
			login();
			break;
		}
	}

	private void checkVersion() {
		new AsyncTask<Void, Void, ModelData<Version>>() {
			@Override
			protected ModelData<Version> doInBackground(Void... params) {
				PackageManager pManager = getPackageManager();
				int v = 1;
				try {
					v = pManager.getPackageInfo("com.yulebaby.teacher", 0).versionCode;
				} catch (NameNotFoundException e) {
				}
				ModelData<Version> version = UpdateService.checkVersion(LoginActivity.this);
				if (version != null) {
					if (Integer.valueOf(version.getInfo().getVersion()) > v) {
						version.setTag(true);
					} else {
						version.setTag(false);
					}
				}
				return version;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mLoginBtn.setText(getResources().getString(R.string.login_ing));
				mLoginBtn.setEnabled(false);
			}

			@Override
			protected void onPostExecute(ModelData<Version> result) {
				if (mWeakActivity.get() == null
						|| mWeakActivity.get().isFinishing()) {
					return;
				}
				mLoginBtn.setText(getResources().getString(R.string.login));
				mLoginBtn.setEnabled(true);
				if (result == null) {
					Toast.makeText(mWeakActivity.get(), App.mNetWorkIssue,
							Toast.LENGTH_SHORT).show();
					Log.d(TAG, "onPostExecute result == null");
					return;
				}
				switch (result.getResultCode()) {
				case Response.NotLogin:
					Toast.makeText(mWeakActivity.get(), result.getMessage(),
							Toast.LENGTH_SHORT).show();
					break;
				case Response.OK:
					boolean update = (Boolean) result.getTag();
					if (update) {
						showDialog(result.getInfo());
					} else {
						login();
					}
					break;
				case Response.ServerError:
					Toast.makeText(mWeakActivity.get(), result.getMessage(),
							Toast.LENGTH_LONG).show();
					break;
				case Response.LogicalErrors:
					Toast.makeText(mWeakActivity.get(), result.getMessage(),
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		}.execute();
	}

	private void login() {
		String passport = mPassportText.getText().toString().trim();
		String password = mPasswordText.getText().toString().trim();

		if ("".equals(passport)) {
			Toast.makeText(this,
					getResources().getString(R.string.passport_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if ("".equals(password)) {
			Toast.makeText(this,
					getResources().getString(R.string.password_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		mLoginTask.cancel(true);
		mLoginTask = new LoginTask(this);
		mLoginTask.execute(passport, password);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLoginTask.cancel(true);
	}
}
