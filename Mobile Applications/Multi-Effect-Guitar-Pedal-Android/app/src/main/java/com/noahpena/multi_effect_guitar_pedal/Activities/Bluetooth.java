package com.noahpena.multi_effect_guitar_pedal.Activities;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.noahpena.multi_effect_guitar_pedal.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Bluetooth
{

    private static BluetoothAdapter mBluetoothAdapter = null;
    private static BluetoothLeScanner mLEScanner = null;
    private static int REQUEST_ENABLE_BT = 1337;
    private static final long SCAN_PERIOD = 10000;
    private static Handler mHandler;
    private static boolean mScanning;
    private static BLEListAdapater adapter;
    private static BluetoothDevice device;

    private static BluetoothGattCharacteristic characteristic;
    private static BluetoothGatt gattServer;


    public static void init(Activity activity)
    {

        final BluetoothManager bluetoothManager = (BluetoothManager)activity.getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter();

        mHandler = new Handler();


        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
    }

    public static boolean isEnabled()
    {
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled());
    }

    public static void close()
    {
        if(gattServer == null)
        {
            return;
        }

        gattServer.disconnect();

        gattServer.close();
        gattServer = null;
    }

    public static void scanForBLEDevice(final boolean enable, final Activity activity)
    {
        mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();

        if(enable && gattServer == null && mBluetoothAdapter != null)
        {
            mScanning = false;
            mLEScanner.stopScan(mScanCallback);

            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.myDialog));
            builder.setTitle("Select BLE Device");
            builder.setCancelable(true);

            adapter = new BLEListAdapater(activity);

            builder.setAdapter(adapter, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();


                    device = (BluetoothDevice) adapter.getItem(i);
                    pairDevice(device);
                    device.createBond();
                    gattServer = device.connectGatt(activity.getApplicationContext(), false, mGattCallback);

                }
            });


            mScanning = true;
            mLEScanner.startScan(mScanCallback);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        else
        {
            mScanning = false;
            mLEScanner.stopScan(mScanCallback);
        }
    }

    private static void pairDevice(BluetoothDevice device) {
        try {
            Log.d("pairDevice()", "Start Pairing...");
            Method m = device.getClass()
                    .getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Log.d("pairDevice()", "Pairing finished.");
        } catch (Exception e) {
            Log.e("pairDevice()", e.getMessage());
        }
    }

    public static final String baseBluetoothUuidPostfix = "0000-1000-8000-00805F9B34FB";

    public static UUID uuidFromShortCode16(String shortCode16)
    {
        return UUID.fromString("0000" + shortCode16 + "-" + baseBluetoothUuidPostfix);
    }

    private static ScanCallback mScanCallback = new ScanCallback()
    {
        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            super.onScanResult(callbackType, result);

            adapter.addDevice(result.getDevice());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results)
        {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode)
        {
            super.onScanFailed(errorCode);
        }
    };

    private static BluetoothAdapter.LeScanCallback mLEScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord)
        {
            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    adapter.addDevice(device);
                }
            });

//            runOnUiThread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    adapter.addDevice(device);
//                }
//            });
        }
    };


    private static BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status)
        {
            super.onMtuChanged(gatt, mtu, status);

        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            super.onConnectionStateChange(gatt, status, newState);

            if(newState == BluetoothProfile.STATE_CONNECTED)
            {
                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            super.onServicesDiscovered(gatt, status);

            if(status == BluetoothGatt.GATT_SUCCESS)
            {
                ParcelUuid[] parcelUuids = device.getUuids();

                for(int i = 0; i < parcelUuids.length; i++)
                {
                    Log.d("DEBUG", "UUID" + i + ": " + parcelUuids[i].getUuid().toString());
                }
                //Log.d("DEBUG", "UUID: " + eh[0].getUuid().toString());
                characteristic = gattServer.getService(uuidFromShortCode16("1337")).getCharacteristic(uuidFromShortCode16("6969"));

                Log.d("DEBUG", "Character UUID: " + characteristic.getUuid());
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            super.onCharacteristicWrite(gatt, characteristic, status);

            isWriting = false;

            Log.d("DEBUG", "Write equals: " + status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }
    };

    private static boolean isWriting = false;

    public static void writeString(String s)
    {
        if(gattServer == null || isWriting)
        {
            return;
        }

       // if(gattServer.requestMtu(128))
        //{
            isWriting = true;
            Log.d("DEBUG", "Wrote: " + s);
            characteristic.setValue(s.getBytes());
            boolean result = gattServer.writeCharacteristic(characteristic);

            if(result)
            {
                Log.d("DEBUG", "it actually wrote");
            }
        //}

    }



    public static class BLEListAdapater extends BaseAdapter
    {

        private ArrayList<BluetoothDevice> devices;
        private LayoutInflater inflater;

        public BLEListAdapater(Activity activity)
        {
            devices = new ArrayList<>();

            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addDevice(BluetoothDevice device)
        {
            devices.add(device);
            notifyDataSetChanged();
        }


        @Override
        public int getCount()
        {
            return devices.size();
        }

        @Override
        public Object getItem(int i)
        {
            return devices.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View vi = inflater.inflate(android.R.layout.simple_list_item_1, null);

            TextView textView = (TextView)vi.findViewById(android.R.id.text1);

            textView.setText(devices.get(i).getName());

            return vi;

        }
    }
}



















//
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.io.OutputStream;
//import java.util.Set;
//import java.util.UUID;
//
///**
// * Created by noah-pena on 11/9/16.
// */
//
//public class Bluetooth
//{
//
//    private static BluetoothDevice guitarPedalDevice = null;
//    private static BluetoothSocket socket = null;
//
//    private static OutputStream outputStream;
//    private static boolean weConnected = false;
//
//
//    public static boolean isConnected()
//    {
//        return weConnected;
//    }
//
//    public static void init(Activity activity)
//    {
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        if(bluetoothAdapter == null)
//        {
//            Toast.makeText(activity.getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
//        }
//
//        if(!bluetoothAdapter.isEnabled())
//        {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            activity.startActivityForResult(enableBtIntent, 1);
//        }
//
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//
//        if(pairedDevices.size() > 0)
//        {
//            for(BluetoothDevice device : pairedDevices)
//            {
//                Log.d("DEBUG", device.getName());
//
//                if(device.getName().equalsIgnoreCase("Multi-Effect Guitar Pedal"))
//                {
//                    guitarPedalDevice = device;
//                    Log.d("DEBUG", "FOUND DEVICE");
//                    return;
//                }
//            }
//
//
//
//            Log.d("DEBUG", "DEVICE NOT PAIRED");
//        }
//        else
//        {
//            Log.d("DEBUG", "Device not found");
//        }
//    }
//
//
//    public static void connectToDevice()
//    {
//        if(guitarPedalDevice == null)
//        {
//            weConnected = false;
//            return;
//        }
//
//        try
//        {
//            //Serial uuid
//            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//            socket = guitarPedalDevice.createRfcommSocketToServiceRecord(uuid);
//
//            socket.connect();
//
//            outputStream = socket.getOutputStream();
//
//            if(outputStream == null)
//            {
//                Log.d("DEBUG", "Stream is null");
//            }
//            else
//            {
//                Log.d("DEBUG", "Stream is gucci");
//            }
//
//            weConnected = true;
//        }
//        catch(Exception e)
//        {
//            weConnected = false;
//        }
//    }
//
//    public static void write(String message)
//    {
//
//        if(weConnected)
//        {
//            try
//            {
//                byte[] bytes = message.getBytes();
//
//                outputStream.write(bytes);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//
//                Log.d("DEBUG", e.getMessage());
//            }
//        }
//    }
//
//    public static void close()
//    {
//        try
//        {
//            socket.close();
//        }
//        catch(Exception e)
//        {
//
//        }
//
//        weConnected = false;
//    }
//}
