package jp.co.thcomp.test_baseactivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import jp.co.thcomp.activity.HandleResultActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LaunchBluetooth = "LaunchBluetooth";
    private static final int LaunchBluetoothInt = LaunchBluetooth.hashCode() & 0x0000FFFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnEnableBluetooth).setOnClickListener(mBtnClickListener);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver, new IntentFilter(LaunchBluetooth));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
    }

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.btnEnableBluetooth:
                    enableBluetoothWithConfirmation();
                    break;
            }
        }
    };

    private void enableBluetoothWithConfirmation() {
        Intent launchIntent = new Intent();
        launchIntent.setClass(this, HandleResultActivity.class);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent transferIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        launchIntent.putExtra(HandleResultActivity.INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT, transferIntent);
        launchIntent.putExtra(HandleResultActivity.INTENT_INT_EXTRA_HOWTO_CALLBACK, HandleResultActivity.CALLBACK_BY_LOCAL_BROADCAST);
        launchIntent.putExtra(HandleResultActivity.INTENT_STRING_EXTRA_CALLBACK_ACTION, LaunchBluetooth);
        launchIntent.putExtra(HandleResultActivity.INTENT_INT_EXTRA_REQUEST_CODE, LaunchBluetoothInt);

        startActivity(launchIntent);
    }

    private final BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (LaunchBluetooth.equals(action)) {
                Intent retIntent = intent.getParcelableExtra(HandleResultActivity.INTENT_PARCELABLE_EXTRA_TRANSFER_INTENT);
                Toast.makeText(MainActivity.this,
                        "Result: " + intent.getIntExtra(HandleResultActivity.INTENT_INT_EXTRA_RESULT_CODE_FROM_TARGET, Activity.RESULT_CANCELED) + "\n" +
                                "Ret: " + retIntent,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}
