package com.noahpena.multi_effect_guitar_pedal.Activities;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by noah-pena on 11/9/16.
 */

public class Bluetooth
{

    private static BluetoothDevice guitarPedalDevice = null;
    private static BluetoothSocket socket = null;

    private static OutputStream outputStream = null;


    public static void init(Activity activity)
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null)
        {
            Toast.makeText(activity.getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName() == "Multi-Effect Guitar Pedal")
                {
                    guitarPedalDevice = device;
                    break;
                }
            }


        }
        else
        {

        }
    }


    public static void connectToDevice()
    {
        if(guitarPedalDevice == null)
        {

            return;
        }

        try
        {
            //Serial uuid
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            socket = guitarPedalDevice.createRfcommSocketToServiceRecord(uuid);

            outputStream = socket.getOutputStream();
        }
        catch(Exception e)
        {

        }
    }

    public static void write(byte[] bytes)
    {
        try
        {
            outputStream.write(bytes);
        }
        catch(Exception e)
        {

        }
    }

    public static void close()
    {
        try
        {
            socket.close();
        }
        catch(Exception e)
        {

        }
    }
}
