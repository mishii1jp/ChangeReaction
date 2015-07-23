package com.example.changereaction.changereaction;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private List<String> uuidArray = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(MainActivity.this, BeaconService.class);
        startService(intent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getBeaconBtn = (Button) this.findViewById(R.id.getBeaconBtn);
        uuidArray = new ArrayList<String>();
        final ListView uuidList = (ListView) this.findViewById(R.id.uuidListView);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        getBeaconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
                BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                        if (scanRecord.length > 30) {
                            if ((scanRecord[5] == (byte) 0x4c) && (scanRecord[6] == (byte) 0x00) && (scanRecord[7] == (byte) 0x02) && (scanRecord[8] == (byte) 0x15)) {
                                String uuid = UtilityCommons.IntToHex2(scanRecord[9] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[10] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[11] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[12] & 0xff)
                                        + "-"
                                        + UtilityCommons.IntToHex2(scanRecord[13] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[14] & 0xff)
                                        + "-"
                                        + UtilityCommons.IntToHex2(scanRecord[15] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[16] & 0xff)
                                        + "-"
                                        + UtilityCommons.IntToHex2(scanRecord[17] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[18] & 0xff)
                                        + "-"
                                        + UtilityCommons.IntToHex2(scanRecord[19] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[20] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[21] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[22] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[23] & 0xff)
                                        + UtilityCommons.IntToHex2(scanRecord[24] & 0xff);

//                                String major = IntToHex2(scanRecord[25] & 0xff) + IntToHex2(scanRecord[26] & 0xff);
//                                String minor = IntToHex2(scanRecord[27] & 0xff) + IntToHex2(scanRecord[28] & 0xff);
                                uuidArray.add(uuid);
                            }
                        }
                    }
                };
                mBluetoothAdapter.startLeScan(mLeScanCallback);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                if (uuidArray.size() != 0) {
                    for (String s : uuidArray) {
                        adapter.add(s);
                    }
                }
                uuidList.setAdapter(adapter);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
