package com.example.changereaction.changereaction;

/**
 * Created by ishiimao on 15/07/12.
 */
public class UtilityCommons {

	public static String IntToHex2(int i) {
		char hex_2[] = {Character.forDigit((i>>4) & 0x0f,16),Character.forDigit(i&0x0f, 16)};
		String hex_2_str = new String(hex_2);
		return hex_2_str.toUpperCase();
	}

}
