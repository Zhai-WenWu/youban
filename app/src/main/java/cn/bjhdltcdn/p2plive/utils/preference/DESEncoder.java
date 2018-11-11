package cn.bjhdltcdn.p2plive.utils.preference;

import android.util.Base64;

import com.orhanobut.logger.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * des加密类
 * @author pwe
 *
 */
public class DESEncoder {

	private static String DES = "DES";

	// private static byte[] ivBytes;

	/**
	 * 加密
	 * 
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {

		// SecureRandom sr = new SecureRandom();
		// byte[] ivbytes = getIv(key);
		// IvParameterSpec iv = new IvParameterSpec(ivBytes);
		// AlgorithmParameterSpec paramSpec = iv;
		//
		// // DES算法要求有一个可信任的随机数源
		// SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		// Cipher cipher = Cipher.getInstance(DES + "/CBC/NoPadding");

		Cipher cipher = Cipher.getInstance(DES + "/CBC/PKCS5Padding");

		// 用密匙初始化Cipher对象
		IvParameterSpec param = new IvParameterSpec(getIv(key));
		cipher.init(Cipher.ENCRYPT_MODE, securekey, param);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		//SecureRandom sr = new SecureRandom();

		// byte[] ivbytes = getIv(key);
		// IvParameterSpec iv = new IvParameterSpec(ivBytes);
		// AlgorithmParameterSpec paramSpec = iv;

		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		// Cipher cipher = Cipher.getInstance(DES + "/CBC/NoPadding");

		Cipher cipher = Cipher.getInstance(DES + "/CBC/PKCS5Padding");
		// 用密匙初始化Cipher对象
		IvParameterSpec param = new IvParameterSpec(getIv(key));

		cipher.init(Cipher.DECRYPT_MODE, securekey, param);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 密码解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String decrypt(String data, String key) {
		try {
			byte[] keyBytes = MD5Encoder.encodeTobytes(key);
			// byte[] keyBytes = key.getBytes("utf-8");
			byte[] eData = Base64.decode(data, Base64.DEFAULT);
			byte[] dData = decrypt(eData, keyBytes);
			String result = new String(dData, "utf-8");
			return result;

		} catch (Exception e) {
			Logger.e(e.getMessage());
		}
		return null;
	}

	/**
	 * 密码加密
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public final static String encrypt(String data, String password) {
		try {
			byte[] keyBytes = MD5Encoder.encodeTobytes(password);
			// byte[] keyBytes = password.getBytes("utf-8");
			byte[] dData = data.getBytes("utf-8");
			byte[] eData = encrypt(dData, keyBytes);
			String result = Base64.encodeToString(eData, Base64.DEFAULT);
			return result;
		} catch (Exception e) {
			Logger.e(e.getMessage());
		}
		return null;
	}

	protected static byte[] getIv(byte[] key) {
		// ivBytes = new byte[8];
		// ivBytes = key.getBytes();
		// byte[] keyendode = MD5Encoder.encodeTobytes(key);
		//
		// String strKey = Base64.encodeToString(keyendode, Base64.DEFAULT);
		//
		// if (strKey == null) {
		// strKey = "";
		// }
		//

		// byte[] keyendode = MD5Encoder.encode(key);
		byte[] keyendode = key;
		byte[] ivBytes = new byte[8];
		if (keyendode == null || keyendode.length == 0) {
			return ivBytes;
		} else {
			for (int i = 0; i < 8 && i < keyendode.length; i++) {
				ivBytes[i] = keyendode[i];
			}
		}
		return ivBytes;
	}
}
