package jp.co.thcomp.activity;

import android.content.Intent;

public class HandleResultActivity extends AbsResultTransferActivity {
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		returnResult(data);
		finish();
	}

	@Override
	protected boolean launchOther() {
		boolean ret = true;
		Intent intent = getIntent();
		Intent forOtherIntent = intent.getParcelableExtra(INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT);

		if(forOtherIntent == null){
			ret = false;
		}else{
			startActivityForResult(forOtherIntent, 0);
		}

		return ret;
	}
}
