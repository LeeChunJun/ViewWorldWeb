package com.licj.viewworldweb.utils;

/**
 * Created by licj on 2017/7/3.
 */
public class L {
	
	private static final String TAG = "Recommender";
	private static boolean debug = true;
	// private static boolean debug = false;

	public static boolean getDebug() {
		return debug;
	}

	public static void i(String msg) {
		if (debug) {
			System.out.println(TAG + ":" + msg);
		}
	}

	public static void e(String msg) {
		if (debug) {
			System.err.println(TAG + ":" + msg);
		}
	}

}
