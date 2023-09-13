package com.example.test_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothStatusReceiver extends BroadcastReceiver {

    private BluetoothStatusListener listener;

    public void setListener(BluetoothStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_CONNECTED:
                    if (listener != null) {
                        listener.onConnected();
                    }
                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:
                    if (listener != null) {
                        listener.onDisconnected();
                    }
                    break;
            }
        }
    }

    public interface BluetoothStatusListener {
        void onConnected();
        void onDisconnected();
    }
}
