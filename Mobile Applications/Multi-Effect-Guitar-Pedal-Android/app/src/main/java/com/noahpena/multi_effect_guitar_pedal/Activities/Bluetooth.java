package com.noahpena.multi_effect_guitar_pedal.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by noah-pena on 11/9/16.
 */

public class Bluetooth
{

    private static OutputStream outputStream = null;
    private static InputStream inputStream = null;

    private static void openBluetoothSettingsPage(Context context)
    {
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.android.settings",
                "com.android.settings.bluetooth.BluetoothSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity( intent);
    }

    public static void init(Context context)
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter != null)
        {
            if(bluetoothAdapter.isEnabled())
            {
                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

                if(bondedDevices.size() > 0)
                {
                    Object[] devices = bondedDevices.toArray();

                    for (int i = 0; i < devices.length; i++)
                    {
                        BluetoothDevice device = (BluetoothDevice) devices[i];
                        ParcelUuid[] uuids = device.getUuids();

                        try
                        {
                            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());

                            socket.connect();

                            outputStream = socket.getOutputStream();
                            inputStream = socket.getInputStream();
                            return;
                        }
                        catch(Exception e)
                        {

                        }
                    }
                }

                Toast.makeText(context, "Please Connect to Guitar-Pedal", Toast.LENGTH_SHORT).show();
                openBluetoothSettingsPage(context);
            }
            else
            {
                Toast.makeText(context, "Please turn on your Bluetooth", Toast.LENGTH_SHORT).show();
                openBluetoothSettingsPage(context);
            }
        }
    }


    public static void sendString(String s)
    {
        try
        {
            outputStream.write(s.getBytes());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String readString()
    {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        try
        {
            bytes = inputStream.read(buffer);
            return buffer.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
