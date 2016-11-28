package com.noahpena.multi_effect_guitar_pedal.Activities;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

    private static OutputStream outputStream;
    private static boolean weConnected = false;


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
                Log.d("DEBUG", device.getName());

                if(device.getName().equalsIgnoreCase("Multi-Effect Guitar Pedal"))
                {
                    guitarPedalDevice = device;
                    Log.d("DEBUG", "FOUND DEVICE");
                    return;
                }
            }



            Log.d("DEBUG", "DEVICE NOT PAIRED");
        }
        else
        {
            Log.d("DEBUG", "Device not found");
        }
    }


    public static void connectToDevice()
    {
        if(guitarPedalDevice == null)
        {
            weConnected = false;
            return;
        }

        try
        {
            //Serial uuid
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            socket = guitarPedalDevice.createRfcommSocketToServiceRecord(uuid);

            socket.connect();

            outputStream = socket.getOutputStream();

            if(outputStream == null)
            {
                Log.d("DEBUG", "Stream is null");
            }
            else
            {
                Log.d("DEBUG", "Stream is gucci");
            }

            weConnected = true;
        }
        catch(Exception e)
        {
            weConnected = false;
        }
    }

    public static void write(String message)
    {

        if(weConnected)
        {
            try
            {
                byte[] bytes = message.getBytes();

                outputStream.write(bytes);
            }
            catch (Exception e)
            {
                e.printStackTrace();

                Log.d("DEBUG", e.getMessage());
            }
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

        weConnected = false;
    }
}
