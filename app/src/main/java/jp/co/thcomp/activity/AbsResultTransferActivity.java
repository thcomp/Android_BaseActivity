package jp.co.thcomp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public abstract class AbsResultTransferActivity extends Activity {
	public static final String INTENT_INT_EXTRA_HOWTO_CALLBACK = "INTENT_INT_EXTRA_HOWTO_CALLBACK";
	public static final String INTENT_INT_EXTRA_REQUEST_CODE = "INTENT_INT_EXTRA_REQUEST_CODE";
	public static final String INTENT_INT_EXTRA_RESULT_CODE = "INTENT_INT_EXTRA_RESULT_CODE";
	public static final String INTENT_STRING_EXTRA_CALLBACK_ACTION = "INTENT_STRING_EXTRA_CALLBACK_ACTION";
	public static final String INTENT_STRING_EXTRA_CALLBACK_TO_PACKAGE = "INTENT_STRING_EXTRA_CALLBACK_TO_PACKAGE";
	public static final String INTENT_STRING_EXTRA_CALLBACK_TO_CLASS = "INTENT_STRING_EXTRA_CALLBACK_TO_CLASS";
	public static final String INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT = "INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT";

	public static final int CALLBACK_BY_LOCAL_BROADCAST = 0;
	public static final int CALLBACK_BY_BROADCAST = 1;
	public static final int CALLBACK_BY_SERVICE = 2;
	public static final int CALLBACK_BY_ACTIVITY = 3;

	abstract protected boolean launchOther();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//moveTaskToBack(true);
		if(!launchOther()){
			finish();
		}
	}

	protected void returnResult(Intent retIntent){
		if(retIntent != null){
			Intent backToIntent = new Intent();
			Intent intent = getIntent();
			String retAction = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_ACTION);
			String packageName = null;
			String className = null;
			int callbackType = intent.getIntExtra(INTENT_INT_EXTRA_HOWTO_CALLBACK, -1);
	
			if(retAction != null){
				backToIntent.setAction(retAction);
			}

			backToIntent.putExtra(INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT, new Intent(retIntent));
			backToIntent.putExtra(INTENT_INT_EXTRA_REQUEST_CODE, intent.getIntExtra(INTENT_INT_EXTRA_REQUEST_CODE, 0));

			switch(callbackType){
			case CALLBACK_BY_LOCAL_BROADCAST:
				LocalBroadcastManager.getInstance(this).sendBroadcast(backToIntent);
				break;
			case CALLBACK_BY_BROADCAST:
				sendBroadcast(backToIntent);
				break;
			case CALLBACK_BY_SERVICE:
				packageName = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_TO_PACKAGE);
				className = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_TO_CLASS);
				backToIntent.setClassName(packageName, className);
				startService(backToIntent);
				break;
			case CALLBACK_BY_ACTIVITY:
				packageName = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_TO_PACKAGE);
				className = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_TO_CLASS);
				backToIntent.setClassName(packageName, className);
				startActivity(backToIntent);
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			data.putExtra(INTENT_INT_EXTRA_RESULT_CODE, resultCode);
		}
	}
}
