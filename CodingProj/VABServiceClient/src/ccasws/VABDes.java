package ccasws;

import com.sun.crypto.provider.SunJCE;
import java.io.PrintStream;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class VABDes
{

	public VABDes()
	{
		deskey = null;
		c = null;
		cipherByte = null;
		mydeskey = null;
		if("0123456789ABCDEF".length() == 16 || "0123456789ABCDEF".length() == 32 || "0123456789ABCDEF".length() == 48)
			mydeskey = hex2byte("0123456789ABCDEF");
		else
			mydeskey = stdcommkey;
		init(mydeskey);
	}

	public VABDes(byte mykey[])
	{
		deskey = null;
		c = null;
		cipherByte = null;
		mydeskey = null;
		init(mykey);
	}

	public VABDes(String typekey)
	{
		deskey = null;
		c = null;
		cipherByte = null;
		mydeskey = null;
		mydeskey = stdcommkey;
		if(typekey.equals("deskey"))
			mydeskey = hex2byte("0123456789ABCDEF");
		if(typekey.equals("pinkey"))
			mydeskey = hex2byte("0123456789ABCDEF");
		if(typekey.equals("stdkey"))
			mydeskey = stdcommkey;
		if(mydeskey == null || typekey.length() == 16 || typekey.length() == 32 || typekey.length() == 48)
			mydeskey = hex2byte(typekey);
		init(mydeskey);
	}

	public void init()
	{
		init(stdcommkey);
	}

	public void init(String strkey)
	{
		mydeskey = stdcommkey;
		if(strkey.length() == 16 || strkey.length() == 32 || strkey.length() == 48)
			mydeskey = hex2byte(strkey);
		if(strkey.equals("deskey"))
			mydeskey = hex2byte("0123456789ABCDEF");
		if(strkey.equals("pinkey"))
			mydeskey = hex2byte("0123456789ABCDEF");
		init(mydeskey);
	}

	public void init(byte initkey[])
	{
		try
		{
			if(initkey.length == 16)
			{
				String myinitkey = byte2hex(initkey);
				myinitkey = (new StringBuilder(String.valueOf(myinitkey))).append(myinitkey.substring(0, 16)).toString();
				initkey = hex2byte(myinitkey);
				myinitkey = null;
			}
			if(initkey.length == 24)
			{
				deskey = new SecretKeySpec(initkey, "DESede");
				c = Cipher.getInstance("DESede/ECB/Nopadding");
			} else
			{
				deskey = new SecretKeySpec(initkey, "DES");
				c = Cipher.getInstance("DES/ECB/Nopadding");
			}
		}
		catch(NoSuchAlgorithmException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
		catch(NoSuchPaddingException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
	}

	public byte[] createEncryptor(String str)
	{
		int len = str.length();
		int maxPage = len / 8;
		maxPage += len % 8 != 0 ? 1 : 0;
		try
		{
			c.init(1, deskey);
			byte aa[] = new byte[8 * maxPage];
			for(int i = 0; i < aa.length; i++)
				aa[i] = 32;

			System.arraycopy(str.getBytes(), 0, aa, 0, str.getBytes().length);
			cipherByte = c.doFinal(aa);
		}
		catch(InvalidKeyException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
		catch(BadPaddingException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
		catch(IllegalBlockSizeException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder("Exception0=")).append(ex.toString()).toString());
		}
		return cipherByte;
	}

	public byte[] createEncryptor(byte bt[])
	{
		int len = bt.length;
		int maxPage = len / 8;
		maxPage += len % 8 != 0 ? 1 : 0;
		byte aa[] = new byte[8 * maxPage];
		for(int i = 0; i < aa.length; i++)
			aa[i] = 0;

		System.arraycopy(bt, 0, aa, 0, bt.length);
		try
		{
			c.init(1, deskey);
			cipherByte = c.doFinal(aa);
		}
		catch(InvalidKeyException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
		catch(BadPaddingException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
		catch(IllegalBlockSizeException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
		return cipherByte;
	}

	public String createDecryptor(byte buff[])
	{
		return byte2hex(createDecryptorB(buff));
	}

	public byte[] createDecryptorB(byte buff[])
	{
		cipherByte = null;
		try
		{
			c.init(2, deskey);
			cipherByte = c.doFinal(buff);
		}
		catch(InvalidKeyException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
		catch(BadPaddingException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder(String.valueOf((new Exception()).getStackTrace()[0].getClassName()))).append(":").append((new Exception()).getStackTrace()[0].getMethodName()).append(",Exception=").append(ex.toString()).toString());
		}
		catch(IllegalBlockSizeException ex)
		{
			System.out.println((new StringBuilder("getMessage=")).append(ex.getMessage()).toString());
			System.out.println((new StringBuilder("Exception2=")).append(ex.toString()).toString());
			System.out.println((new StringBuilder("buff len=[")).append(buff.length).append("]...").toString());
		}
		return cipherByte;
	}

	public String base64encode(byte b[])
	{
		BASE64Encoder enCoder = new BASE64Encoder();
		return enCoder.encode(b);
	}

	public String byte2hex(byte b[])
	{
		String hs = "";
		String stmp = "";
		for(int n = 0; n < b.length; n++)
		{
			stmp = Integer.toHexString(b[n] & 0xff);
			if(stmp.length() == 1)
				hs = (new StringBuilder(String.valueOf(hs))).append("0").append(stmp).toString();
			else
				hs = (new StringBuilder(String.valueOf(hs))).append(stmp).toString();
		}

		return hs.toUpperCase();
	}

	public byte[] hex2byte(String str)
	{
		int len = str.length();
		String stmp = null;
		byte bt[] = new byte[len / 2];
		for(int n = 0; n < len / 2; n++)
		{
			stmp = str.substring(n * 2, n * 2 + 2);
			bt[n] = (byte)Integer.parseInt(stmp, 16);
		}

		return bt;
	}

	public String dec(String str)
	{
		return createDecryptor(hex2byte(str));
	}

	public byte[] decB(String str)
	{
		return createDecryptorB(hex2byte(str));
	}

	public String enc(String str)
	{
		return byte2hex(createEncryptor(str));
	}

	public String base64enc(String str)
	{
		return base64encode(createEncryptor(str));
	}

	public String base64encH(String str)
	{
		return base64encode(createEncryptor(hex2byte(str)));
	}

	public static String hexStringToString(String hexString, int encodeType)
	{
		String result = "";
		int max = hexString.length() / encodeType;
		for(int i = 0; i < max; i++)
		{
			char c = (char)hexStringToAlgorism(hexString.substring(i * encodeType, (i + 1) * encodeType));
			result = (new StringBuilder(String.valueOf(result))).append(c).toString();
		}

		return result;
	}

	public static int hexStringToAlgorism(String hex)
	{
		hex = hex.toUpperCase();
		int max = hex.length();
		int result = 0;
		for(int i = max; i > 0; i--)
		{
			char c = hex.charAt(i - 1);
			int algorism = 0;
			if(c >= '0' && c <= '9')
				algorism = c - 48;
			else
				algorism = c - 55;
			result = (int)((double)result + Math.pow(16D, max - i) * (double)algorism);
		}

		return result;
	}

	public static void main(String args[])
	{
		Des odes_1 = new Des("31323334353637383132333435363738");
		String password = odes_1.enc("feel");
		System.out.println((new StringBuilder(" password ")).append(password).toString());
		System.out.println((new StringBuilder(" dec ")).append(hexStringToString(odes_1.dec(password), 2)).toString());
	}

	private static final String Algorithm = "DES";
	private static final byte stdcommkey[] = {25, 112, -119, 32, 3, 2, 32, -119};
	static final String strcommkey = "0123456789ABCDEF";
	static final String strpinkey = "0123456789ABCDEF";
	private SecretKey deskey;
	private Cipher c;
	private byte cipherByte[];
	private byte mydeskey[];

	static 
	{
		Security.addProvider(new SunJCE());
	}

}
