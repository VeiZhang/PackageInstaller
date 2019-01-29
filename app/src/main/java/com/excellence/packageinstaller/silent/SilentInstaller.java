package com.excellence.packageinstaller.silent;

import android.content.Context;
import android.util.Log;

import com.excellence.packageinstaller.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SilentInstaller
{
	public static int install(String apkPath, Context context)
	{
		return silentInstall(apkPath.replace(" ", "\" \""));
	}

	public static int uninstall(String packageName, Context context)
	{
		return silentUninstall(packageName);
	}

	/**
	 * 静默安装
	 */
	private static int silentInstall(String apkPath)
	{
		Process process = null;
		String result = "";
		String line = null;
		try
		{
			String[] command = { "chmod", "777", apkPath };
			ProcessBuilder filebBuilder = new ProcessBuilder(command);
			filebBuilder.start();

			process = Runtime.getRuntime().exec("pm install -r " + apkPath);
			BufferedReader errIs = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((line = errIs.readLine()) != null)
			{
				result += line;
			}
			BufferedReader inIs = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = inIs.readLine()) != null)
			{
				result += line;
			}
			inIs.close();
			errIs.close();
			Log.e(apkPath, result);
			return typeOfInstallResult(result.toUpperCase());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (process != null)
			{
				process.destroy();
			}
		}
		return typeOfInstallResult(null);
	}

	private static int typeOfInstallResult(String result)
	{
		if (result == null)
		{
			return R.string.INSTALL_FAILED_UNKNOWN_ERROR;
		}
		else
		{
			if (result.contains("SUCCESS"))
			{
				return R.string.INSTALL_SUCCESS;
			}
			if (result.contains("INSTALL_FAILED_ALREADY_EXISTS"))
			{
				return R.string.INSTALL_FAILED_ALREADY_EXISTS;
			}
			if (result.contains("INSTALL_FAILED_INVALID_APK"))
			{
				return R.string.INSTALL_FAILED_INVALID_APK;
			}
			if (result.contains("INSTALL_FAILED_INVALID_URI"))
			{
				return R.string.INSTALL_FAILED_INVALID_URI;
			}
			if (result.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE"))
			{
				return R.string.INSTALL_FAILED_INSUFFICIENT_STORAGE;
			}
			if (result.contains("INSTALL_FAILED_DUPLICATE_PACKAGE"))
			{
				return R.string.INSTALL_FAILED_DUPLICATE_PACKAGE;
			}
			if (result.contains("INSTALL_FAILED_NO_SHARED_USER"))
			{
				return R.string.INSTALL_FAILED_NO_SHARED_USER;
			}
			if (result.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE"))
			{
				return R.string.INSTALL_FAILED_UPDATE_INCOMPATIBLE;
			}
			if (result.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE"))
			{
				return R.string.INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
			}
			if (result.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY"))
			{
				return R.string.INSTALL_FAILED_MISSING_SHARED_LIBRARY;
			}
			if (result.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE"))
			{
				return R.string.INSTALL_FAILED_REPLACE_COULDNT_DELETE;
			}
			if (result.contains("INSTALL_FAILED_DEXOPT"))
			{
				return R.string.INSTALL_FAILED_DEXOPT;
			}
			if (result.contains("INSTALL_FAILED_OLDER_SDK"))
			{
				return R.string.INSTALL_FAILED_OLDER_SDK;
			}
			if (result.contains("INSTALL_FAILED_CONFLICTING_PROVIDER"))
			{
				return R.string.INSTALL_FAILED_CONFLICTING_PROVIDER;
			}
			if (result.contains("INSTALL_FAILED_NEWER_SDK"))
			{
				return R.string.INSTALL_FAILED_NEWER_SDK;
			}
			if (result.contains("INSTALL_FAILED_TEST_ONLY"))
			{
				return R.string.INSTALL_FAILED_TEST_ONLY;
			}
			if (result.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE"))
			{
				return R.string.INSTALL_FAILED_CPU_ABI_INCOMPATIBLE;
			}
			if (result.contains("INSTALL_FAILED_MISSING_FEATURE"))
			{
				return R.string.INSTALL_FAILED_MISSING_FEATURE;
			}
			if (result.contains("INSTALL_FAILED_CONTAINER_ERROR"))
			{
				return R.string.INSTALL_FAILED_CONTAINER_ERROR;
			}
			if (result.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION"))
			{
				return R.string.INSTALL_FAILED_INVALID_INSTALL_LOCATION;
			}
			if (result.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE"))
			{
				return R.string.INSTALL_FAILED_MEDIA_UNAVAILABLE;
			}
			if (result.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT"))
			{
				return R.string.INSTALL_FAILED_VERIFICATION_TIMEOUT;
			}
			if (result.contains("INSTALL_FAILED_VERIFICATION_FAILURE"))
			{
				return R.string.INSTALL_FAILED_VERIFICATION_FAILURE;
			}
			if (result.contains("INSTALL_FAILED_PACKAGE_CHANGED"))
			{
				return R.string.INSTALL_FAILED_PACKAGE_CHANGED;
			}
			if (result.contains("INSTALL_FAILED_UID_CHANGED"))
			{
				return R.string.INSTALL_FAILED_UID_CHANGED;
			}
			if (result.contains("INSTALL_PARSE_FAILED_NOT_APK"))
			{
				return R.string.INSTALL_PARSE_FAILED_NOT_APK;
			}
			if (result.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST"))
			{
				return R.string.INSTALL_PARSE_FAILED_BAD_MANIFEST;
			}
			if (result.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION"))
			{
				return R.string.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION;
			}
			if (result.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES"))
			{
				return R.string.INSTALL_PARSE_FAILED_NO_CERTIFICATES;
			}
			if (result.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES"))
			{
				return R.string.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES;
			}
			if (result.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING"))
			{
				return R.string.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING;
			}
			if (result.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME"))
			{
				return R.string.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
			}
			if (result.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID"))
			{
				return R.string.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
			}
			if (result.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED"))
			{
				return R.string.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
			}
			if (result.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY"))
			{
				return R.string.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
			}
			if (result.contains("INSTALL_FAILED_INTERNAL_ERROR"))
			{
				return R.string.INSTALL_FAILED_INTERNAL_ERROR;
			}
			return R.string.INSTALL_FAILED_UNKNOWN_ERROR;
		}
	}

	/**
	 * 静默卸载
	 */
	private static int silentUninstall(String packageName)
	{
		Process process = null;
		String result = "";
		String line = null;
		try
		{
			process = Runtime.getRuntime().exec("gsu shell pm uninstall " + packageName);
			BufferedReader errIs = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((line = errIs.readLine()) != null)
			{
				result += line;
			}
			BufferedReader inIs = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = inIs.readLine()) != null)
			{
				result += line;
			}
			inIs.close();
			errIs.close();
			Log.e(packageName, result);
			return typeOfUninstallResult(result.toUpperCase());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (process != null)
			{
				process.destroy();
			}
		}
		return typeOfUninstallResult(null);
	}

	private static int typeOfUninstallResult(String result)
	{
		if (result == null)
		{
			return R.string.uninstall_fail;
		}
		else
		{
			if (result.contains("SUCCESS"))
				return R.string.uninstall_success;
			else
				return R.string.uninstall_fail;
		}
	}

	/**
	 * 启动app com.exmaple.client/.MainActivity
	 * com.exmaple.client/com.exmaple.client.MainActivity
	 */
	public static boolean startApp(String packageName, String activityName)
	{
		boolean isSuccess = false;
		String cmd = "am start -n " + packageName + "/" + activityName + " \n";
		Process process = null;
		try
		{
			process = Runtime.getRuntime().exec(cmd);
			int value = process.waitFor();
			return returnResult(value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (process != null)
			{
				process.destroy();
			}
		}
		return isSuccess;
	}

	private static boolean returnResult(int value)
	{
		// 代表成功
		if (value == 0)
		{
			return true;
		}
		else if (value == 1)
		{ // 失败
			return false;
		}
		else
		{ // 未知情况
			return false;
		}
	}
}
