package jp.co.thcomp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;

abstract public class AbsDialogActivity extends AbsResultTransferActivity {
	private static final String TAG = AbsDialogActivity.class.getSimpleName();

	abstract protected Dialog createDialog(Intent intent);

	@Override
	protected boolean launchOther() {
		if (mEnableDebugLog) Log.d(TAG, "launchOther");

		boolean ret = true;
		Dialog dialog = createDialog(getIntent());

		if(dialog == null){
			ret = false;
		}

		return ret;
	}

}
