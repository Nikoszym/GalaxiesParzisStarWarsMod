package com.parzivail.util.common;

import com.parzivail.swg.Resources;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Created by colby on 12/18/2016.
 */
public class Lumberjack
{
	private static Logger logger;

	/**
	 * Prints a message to log only in Debug mode
	 *
	 * @param message The message to print
	 */
	public static void debug(Object message, Object... params)
	{
		if (!Resources.IS_DEBUG)
			return;
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		if (trace.length >= 3)
		{
			StackTraceElement stack = trace[2];
			log("<%s::%s> %s", stack.getClassName(), stack.getMethodName(), String.format(String.valueOf(message), params));
		}
		else
		{
			log("<Unknown Stack> %s", String.format(String.valueOf(message), params));
		}
	}

	/**
	 * Prints a message to log
	 *
	 * @param message The message to print
	 */
	public static void info(Object message)
	{
		log(Level.INFO, String.valueOf(message));
	}

	private static void log(Level level, String message)
	{
		logger.log(level, message);
	}

	/**
	 * Prints a message to log
	 *
	 * @param message The message to print
	 */
	public static void log(Object message)
	{
		info(String.valueOf(message));
	}

	/**
	 * Prints a message to log
	 *
	 * @param message The message to print
	 */
	public static void log(Object message, Object... params)
	{
		info(String.format(String.valueOf(message), params));
	}

	/**
	 * Prints a message to log only
	 *
	 * @param message The message to print
	 */
	public static void warn(Object message, Object... params)
	{
		log(Level.WARN, String.format(String.valueOf(message), params));
	}

	/**
	 * Prints a message to log only
	 *
	 * @param message The message to print
	 */
	public static void err(Object message, Object... params)
	{
		log(Level.ERROR, String.format(String.valueOf(message), params));
	}

	/**
	 * Prints a message to log only
	 *
	 * @param message The message to print
	 */
	public static void trace(Object message, Object... params)
	{
		log(Level.TRACE, String.format(String.valueOf(message), params));
	}

	public static void init(Logger modLog)
	{
		Lumberjack.logger = modLog;
	}
}
