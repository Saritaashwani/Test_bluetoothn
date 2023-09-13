package com.example.test_bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.UUID;

public class BluetoothService extends Service {

    private final IBinder binder = new LocalBinder();

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    public BluetoothStatusReceiver bluetoothStatusReceiver;

    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SerialPortService ID

    public class LocalBinder extends Binder {
        BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void connectToDevice(String deviceAddress) {
        // Get the Bluetooth device
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);

        // Establish a connection
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start listening for connection status changes
        bluetoothStatusReceiver = new BluetoothStatusReceiver();
        bluetoothStatusReceiver.setListener(new BluetoothStatusReceiver.BluetoothStatusListener() {
            @Override
            public void onConnected() {
                // Handle connected event (optional)
                Toast.makeText(BluetoothService.this, "connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDisconnected() {
                // Handle disconnected event
                // Notify your application that the Bluetooth connection is lost
            }
        });

        Intent filterIntent = new Intent(BluetoothAdapter.ACTION_STATE_CHANGED);
        sendBroadcast(filterIntent);
    }

    public void disconnect() {
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bluetoothStatusReceiver != null) {
            unregisterReceiver(bluetoothStatusReceiver);
            bluetoothStatusReceiver = null;
        }
    }

    private void unregisterReceiver(BluetoothStatusReceiver bluetoothStatusReceiver) {
    }

    private class BluetoothStatusListener {
    }
}
