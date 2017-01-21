package jp.co.thcomp.activity;

import android.content.Intent;
import android.util.Log;

public class HandleResultActivity extends AbsResultTransferActivity {
    private static final String TAG = HandleResultActivity.class.getSimpleName();
    private static final int REQUEST_CODE = "HandleResultActivity".hashCode() & 0x0000FFFF;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            if (mEnableDebugLog)
                Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode + ", data=" + data);

            returnResult(resultCode, data);
            finish();
        }
    }

    @Override
    protected boolean launchOther() {
        if (mEnableDebugLog) Log.d(TAG, "launchOther");

        boolean ret = true;
        Intent intent = getIntent();
        Intent forOtherIntent = intent.getParcelableExtra(INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT);

        if (forOtherIntent == null) {
            ret = false;
        } else {
            startActivityForResult(forOtherIntent, REQUEST_CODE);
        }

        return ret;
    }
}
