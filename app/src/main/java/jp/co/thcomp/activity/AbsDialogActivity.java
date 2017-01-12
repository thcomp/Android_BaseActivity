package jp.co.thcomp.activity;

import android.app.Dialog;
import android.content.Intent;

abstract public class AbsDialogActivity extends AbsResultTransferActivity {
	abstract protected Dialog createDialog(Intent intent);

	@Override
	protected boolean launchOther() {
		boolean ret = true;
		Dialog dialog = createDialog(getIntent());

		if(dialog == null){
			ret = false;
		}

		return ret;
	}

}
