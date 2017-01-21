package jp.co.thcomp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public abstract class AbsResultTransferActivity extends Activity {
    private static final String TAG = AbsResultTransferActivity.class.getSimpleName();
    public static final String INTENT_BOOLEAN_EXTRA_ENABLE_DEBUG_LOG = "INTENT_BOOLEAN_EXTRA_ENABLE_DEBUG_LOG";
    public static final String INTENT_INT_EXTRA_HOWTO_CALLBACK = "INTENT_INT_EXTRA_HOWTO_CALLBACK";
    public static final String INTENT_INT_EXTRA_REQUEST_CODE = "INTENT_INT_EXTRA_REQUEST_CODE";
    public static final String INTENT_INT_EXTRA_RESULT_CODE_FROM_TARGET = "INTENT_INT_EXTRA_RESULT_CODE_FROM_TARGET";
    public static final String INTENT_STRING_EXTRA_CALLBACK_ACTION = "INTENT_STRING_EXTRA_CALLBACK_ACTION";
    public static final String INTENT_STRING_EXTRA_CALLBACK_TO_PACKAGE = "INTENT_STRING_EXTRA_CALLBACK_TO_PACKAGE";
    public static final String INTENT_STRING_EXTRA_CALLBACK_TO_CLASS = "INTENT_STRING_EXTRA_CALLBACK_TO_CLASS";
    public static final String INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT = "INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT";

    public static final int CALLBACK_BY_LOCAL_BROADCAST = 0;
    public static final int CALLBACK_BY_BROADCAST = 1;
    public static final int CALLBACK_BY_SERVICE = 2;
    public static final int CALLBACK_BY_ACTIVITY = 3;

    abstract protected boolean launchOther();

    protected boolean mEnableDebugLog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEnableDebugLog = getIntent().getBooleanExtra(INTENT_BOOLEAN_EXTRA_ENABLE_DEBUG_LOG, false);
        if (!launchOther()) {
            finish();
        }
    }

    protected void returnResult(int resultCode, Intent retIntent) {
        if (mEnableDebugLog) Log.d(TAG, "returnResult");

        Intent backToIntent = new Intent();
        Intent intent = getIntent();
        String retAction = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_ACTION);
        String packageName = null;
        String className = null;
        int callbackType = intent.getIntExtra(INTENT_INT_EXTRA_HOWTO_CALLBACK, -1);

        if (retAction != null) {
            backToIntent.setAction(retAction);
        }

        backToIntent.putExtra(INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT, retIntent != null ? new Intent(retIntent) : null);
        backToIntent.putExtra(INTENT_INT_EXTRA_REQUEST_CODE, intent.getIntExtra(INTENT_INT_EXTRA_REQUEST_CODE, 0));
        backToIntent.putExtra(INTENT_INT_EXTRA_RESULT_CODE_FROM_TARGET, resultCode);

        switch (callbackType) {
            case CALLBACK_BY_LOCAL_BROADCAST:
                if (mEnableDebugLog)
                    Log.d(TAG, "returnResult(CALLBACK_BY_LOCAL_BROADCAST): backToIntent=" + backToIntent);
                LocalBroadcastManager.getInstance(this).sendBroadcast(backToIntent);
                break;
            case CALLBACK_BY_BROADCAST:
                if (mEnableDebugLog)
                    Log.d(TAG, "returnResult(CALLBACK_BY_BROADCAST): backToIntent=" + backToIntent);
                sendBroadcast(backToIntent);
                break;
            case CALLBACK_BY_SERVICE:
                if (mEnableDebugLog)
                    Log.d(TAG, "returnResult(CALLBACK_BY_SERVICE): backToIntent=" + backToIntent);
                packageName = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_TO_PACKAGE);
                className = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_TO_CLASS);
                backToIntent.setClassName(packageName, className);
                startService(backToIntent);
                break;
            case CALLBACK_BY_ACTIVITY:
                if (mEnableDebugLog)
                    Log.d(TAG, "returnResult(CALLBACK_BY_ACTIVITY): backToIntent=" + backToIntent);
                packageName = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_TO_PACKAGE);
                className = intent.getStringExtra(INTENT_STRING_EXTRA_CALLBACK_TO_CLASS);
                backToIntent.setClassName(packageName, className);
                startActivity(backToIntent);
                break;
        }
    }
}
