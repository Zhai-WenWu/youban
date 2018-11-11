/**
 * 
 */
package cn.bjhdltcdn.p2plive.utils.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import cn.bjhdltcdn.p2plive.app.App;


/**
 * 数据存储工具类
 * @author pwe
 *
 */
public class SafeSharePreferenceUtils {
	private static ArrayList<Integer> lsitId;

	public static String getString(String key, String defValue) {
		try {
			if (TextUtils.isEmpty(key)) {
				return "" ;
			}
			String strKey = MD5Encoder.encode(key);
			String dsKey = MD5Encoder.encode(strKey);
			String strDef = DESEncoder.encrypt(defValue, dsKey);
			String vl = PreferenceManager.getDefaultSharedPreferences(App.getInstance()).getString(strKey, strDef);
			String str = DESEncoder.decrypt(vl, dsKey);
			return str;
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return defValue;
		}
	}

	public static boolean saveString(String key, String vl) {
		try {
			if (TextUtils.isEmpty(key)) {
				return false ;
			}
			String strKey = MD5Encoder.encode(key);
			String dsKey = MD5Encoder.encode(strKey);
			String strDef = DESEncoder.encrypt(vl, dsKey);
			return PreferenceManager.getDefaultSharedPreferences(App.getInstance()).edit().putString(strKey, strDef).commit();
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return false;
		}
	}

	public static int getInt(String key, int defValue) {
		try {
			String str=getString(key, String.valueOf(defValue));
			int result = Integer.parseInt(str);
			return result;
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return defValue;
		}
	}

	public static boolean saveInt(String key, int vl) {
		try {
			return saveString(key, String.valueOf(vl));
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return false;
		}
	}

	public static boolean getBoolean(String key, boolean defValue) {
		try {
			String str=getString(key, String.valueOf(defValue));
			boolean result = Boolean.parseBoolean(str);
			return result;
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return defValue;
		}
	}

	public static boolean saveBoolean(String key, boolean vl) {
		try {
			return saveString(key, String.valueOf(vl));
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return false;
		}
	}

	public static float getFloat(String key, float defValue) {
		try {
			String str=getString(key, String.valueOf(defValue));
			float result = Float.parseFloat(str);
			return result;
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return defValue;
		}
	}

	public static boolean saveFloat(String key, boolean vl) {
		try {
			return saveString(key, String.valueOf(vl));
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return false;
		}
	}

	public static long getLong(String key, long defValue) {
		try {
			String str=getString(key, String.valueOf(defValue));
			long result = Long.parseLong(str);
			return result;
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return defValue;
		}
	}

	public static boolean saveLong(String key, long vl) {
		try {
			return saveString(key, String.valueOf(vl));
		} catch (Exception e) {
			Logger.e(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 清楚数据
	 * @param context
	 * @param keys
	 */
	public static void clearDataByKey(final Context context, String... keys) {
		if (keys == null || keys.length == 0) {
			return;
		}
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getInstance()).edit();
		for(String key:keys){
			if (!TextUtils.isEmpty(key)) {
				String encoderKey = MD5Encoder.encode(key);
				editor.remove(encoderKey);
			}
		}
	    editor.commit();
	}

}
