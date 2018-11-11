package cn.bjhdltcdn.p2plive.utils.preference;

import android.util.Base64;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 *  MD5 加密类
 * @author pwe
 *
 */
public class MD5Encoder {

	public MD5Encoder() {
	}

	public static String encode(String sourceString) {
		String resultString = null;
		try {
			resultString = new String(sourceString);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = encodeBase16(md.digest(resultString.getBytes()));
		} catch (Exception ex) {

		}
		return resultString;
	}

	public static byte[] encodeTobytes(String sourceString) {
		byte[] result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = md.digest(sourceString.getBytes("utf-8"));
			return result;
		} catch (Exception ex) {
			Logger.e(ex.getMessage());
		}
		return result;
	}

	public static String encodeBase16(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			// top 4 bits
			char c = (char) ((b >> 4) & 0xf);
			if (c > 9) {
				c = (char) ((c - 10) + 'a');
			} else {
				c = (char) (c + '0');
			}
			sb.append(c);
			// bottom 4 bits
			c = (char) (b & 0xf);
			if (c > 9) {
				c = (char) ((c - 10) + 'a');
			} else {
				c = (char) (c + '0');
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public static String encodeFile(String filePath) {
		String resultString = null;
		FileInputStream fis = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				return "";
			}
			fis = new FileInputStream(file);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[2048];
			int lenght = -1;
			while ((lenght = fis.read(buffer)) != -1) {
				md.update(buffer, 0, lenght);
			}
			byte[] bout = md.digest();
			// md.digest(input);
			resultString = encodeBase16(bout);
		} catch (Exception ex) {
			Logger.e(ex.getMessage());
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return resultString;
	}

	public static byte[] encode(byte[] sourceString) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(sourceString);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public static String encodeBase64(byte[] sourceString) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(sourceString);
			return Base64.encodeToString(result, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		}
	}

}
