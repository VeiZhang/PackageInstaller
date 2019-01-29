package com.excellence.packageinstaller.silent;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;

/**
 * Created by ZhangWei on 2016/9/30.
 */

public class ApkSigner
{
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String getAPKFileSignature(String apkPath)
	{
		String signatureMD5 = null;
		try
		{
			// apk包的文件路径
			// 这是一个Package 解释器, 是隐藏的
			// 构造函数的参数只有一个, apk文件的路径
			// PackageParser packageParser = new PackageParser(apkPath);
			String PATH_PackageParser = "android.content.pm.PackageParser";
			Class pkgParserCls = Class.forName(PATH_PackageParser);
			Class[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			// 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			// PackageParser.Package mPkgInfo = packageParser.parsePackage(new
			// File(apkPath), apkPath, metrics, 0);
			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
			valueArgs = new Object[4];
			valueArgs[0] = new File(apkPath);
			valueArgs[1] = apkPath;
			valueArgs[2] = metrics;
			valueArgs[3] = PackageManager.GET_SIGNATURES;
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);

			typeArgs = new Class[2];
			typeArgs[0] = pkgParserPkg.getClass();
			typeArgs[1] = Integer.TYPE;
			Method pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates", typeArgs);
			valueArgs = new Object[2];
			valueArgs[0] = pkgParserPkg;
			valueArgs[1] = PackageManager.GET_SIGNATURES;
			pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
			// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
			Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
			Signature[] signatures = (Signature[]) packageInfoFld.get(pkgParserPkg);
			if (signatures.length > 0)
				signatureMD5 = getSignatureMD5(signatures[0]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return signatureMD5;
	}

	public static String getPackageSignature(Context context, String packageName)
	{
		String signatureMD5 = null;
		try
		{
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			if (packageInfo.signatures.length > 0)
			{
				signatureMD5 = getSignatureMD5(packageInfo.signatures[0]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return signatureMD5;
	}

	private static String getSignatureMD5(Signature signature) throws Exception
	{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] digest = md5.digest(signature.toByteArray());
		return toHexString(digest);
	}

	/** 将字节数组转化为对应的十六进制字符串 */
	private static String toHexString(byte[] rawByteArray)
	{
		char[] chars = new char[rawByteArray.length * 2];
		for (int i = 0; i < rawByteArray.length; i++)
		{
			byte b = rawByteArray[i];
			chars[i * 2] = HEX_CHAR[(b >>> 4 & 0x0F)];
			chars[i * 2 + 1] = HEX_CHAR[(b & 0x0F)];
		}
		return new String(chars);
	}
}
