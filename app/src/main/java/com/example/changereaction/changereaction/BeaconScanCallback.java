package com.example.changereaction.changereaction;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;

/**
 * Beacoスキャナー
 * Created by ishiimao on 15/07/23.
 */
public class BeaconScanCallback implements LeScanCallback {

	private String uuid;

	@Override
	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

		if (scanRecord.length > 30) {
				if ((scanRecord[5] == (byte) 0x4c) && (scanRecord[6] == (byte) 0x00) && (scanRecord[7] == (byte) 0x02) && (scanRecord[8] == (byte) 0x15)) {
					uuid = UtilityCommons.IntToHex2(scanRecord[9] & 0xff)
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
				}
			}
	}

	public String getUuid() {
		return uuid;
	}

}
