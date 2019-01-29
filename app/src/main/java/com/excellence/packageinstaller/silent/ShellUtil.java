package com.excellence.packageinstaller.silent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Process;

/***
 * Created by ZhangWei on 2016/9/13.
 */
public class ShellUtil
{
	private static final String TAG = ShellUtil.class.getSimpleName();

	public static CommandResult execRuntimeCommand(String param)
	{
		Process process = null;
		StringBuilder msg = new StringBuilder();
		int result = -1;
		try
		{
			process = Runtime.getRuntime().exec(param);
			StringBuilder successMsg = getInputSteam(process.getInputStream());
			StringBuilder errorMsg = getInputSteam(process.getErrorStream());
			msg.append(successMsg).append(errorMsg);
			result = process.waitFor();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msg = null;
		}
		if (process != null)
			process.destroy();
		return new CommandResult(result, msg == null ? null : msg.toString());
	}

	public static CommandResult execProceeBuilderCommand(String... param)
	{
		if (param == null || param.length == 0)
			return new CommandResult();

		Process process = null;
		StringBuilder msg = null;
		int result = -1;
		try
		{
			// redirectErrorStream(true)将错误输出流转移到标准输出流中
			process = new ProcessBuilder(param).redirectErrorStream(true).start();
			msg = getInputSteam(process.getInputStream());
			result = process.waitFor();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (process != null)
			process.destroy();
		return new CommandResult(result, msg == null ? null : msg.toString());
	}

	private static StringBuilder getInputSteam(InputStream inputStream) throws Exception
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder result = new StringBuilder();
		String line = null;
		while ((line = stdin.readLine()) != null)
		{
			result.append(line);
		}
		stdin.close();
		return result;
	}

	public static class CommandResult
	{

		public int result = -1;
		public String msg = null;

		public CommandResult()
		{

		}

		public CommandResult(int result)
		{
			this.result = result;
		}

		public CommandResult(int result, String msg)
		{
			this.result = result;
			this.msg = msg;
		}

	}
}
