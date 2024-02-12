package com.partseazy.android.Logger;


import android.text.TextUtils;
import android.util.Log;

import com.partseazy.android.BuildConfig;

/**
 * Created by Pumpkin Guy on 20/11/16.
 */

public class CustomLogger {


    private static final boolean ENABLE_LOGS = true;
    private static final int VERBOSE = Log.VERBOSE;
    private static final int DEBUG = Log.DEBUG;
    private static final int INFO = Log.INFO;
    private static final int WARN = Log.WARN;
    private static final int ERROR = Log.ERROR;
    private static final int WTF = Log.ASSERT;

    //Default TAG
    private static final String DEFAULT_TAG = "PARTSEAZY";

    private static String tag = DEFAULT_TAG;

    private static int LEVEL = (BuildConfig.DEBUG ? VERBOSE : WARN);

    /**
     * To set new Tag
     *
     * @param newTag
     * @return
     */
    public static String setTag(String newTag) {
        String oldTag = tag;
        if (newTag != null && !newTag.equals(oldTag)) {
            tag = newTag;
            android.util.Log.i(DEFAULT_TAG, "Tag: " + newTag);
        }
        return oldTag;
    }

    public static String getTag() {
        return tag;
    }


    public static boolean isDefaultTag() {
        return DEFAULT_TAG.equals(tag);
    }

    private static void log(final int level, final String msg) {

        if (!ENABLE_LOGS)
            return;

        if (level < LEVEL) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        // Uses StackTrace to build the log tag ( class name, method name and
        // Line Number)
        final StackTraceElement[] elements = new Throwable().getStackTrace();
        String callerClassName = "?";
        String callerMethodName = "?";
        int lineNumber = 0;
        if (elements.length >= 3) {
            callerClassName = elements[2].getClassName();
            callerClassName = callerClassName.substring(callerClassName
                    .lastIndexOf('.') + 1);
            if (callerClassName.indexOf("$") > 0) {
                callerClassName = callerClassName.substring(0,
                        callerClassName.indexOf("$"));
            }
            callerMethodName = elements[2].getMethodName();
            lineNumber = elements[2].getLineNumber();
            callerMethodName = callerMethodName.substring(callerMethodName
                    .lastIndexOf('_') + 1);
            if (callerMethodName.equals("<init>")) {
                callerMethodName = callerClassName;
            }
        }

        final String stack = new StringBuffer(callerClassName).append(" ")
                .append(callerMethodName).append("()").append(":")
                .append(lineNumber).toString();

        switch (level) {
            case VERBOSE:
                Log.v(tag, "[" + stack + "] " + msg);
                break;
            case DEBUG:
                Log.d(tag, "[" + stack + "] " + msg);
                break;
            case INFO:
                Log.i(tag, "[" + stack + "] " + msg);
                break;
            case WARN:
                Log.w(tag, "[" + stack + "] " + msg);
                break;
            case ERROR:
                Log.e(tag, "[" + stack + "] " + msg);
                break;
            case WTF:
                Log.wtf(tag, "[" + stack + "] " + msg);
                break;
            default:
                break;
        }
    }

    /**
     * Send a @DEBUG log message.
     *
     * @param msg The message you would like logged
     */
    public static void d(final String msg) {
        CustomLogger.log(DEBUG, msg);
    }

    /**
     * Send a @VERBOSE log message.
     *
     * @param msg The message you would like logged
     */
    public static void v(final String msg) {
        CustomLogger.log(VERBOSE, msg);
    }

    /**
     * Send an @INFO log message.
     *
     * @param msg The message you would like logged
     */
    public static void i(final String msg) {
        CustomLogger.log(INFO, msg);
    }

    /**
     * Send a @WARN log message.
     *
     * @param msg The message you would like logged
     */
    public static void w(final String msg) {
        CustomLogger.log(WARN, msg);
    }

    /**
     * Send an @ERROR log message.
     *
     * @param msg The message you would like logged
     */
    public static void e(final String msg) {
        CustomLogger.log(ERROR, msg);
    }

    /**
     * Send a @WTF log message.
     *
     * @param msg The message you would like logged
     */
    public static void wtf(final String msg) {
        CustomLogger.log(WTF, msg);
    }

    /**
     * Send a @DEBUG log message and log the exception.
     *
     * @param msg       The message you would like logged
     * @param throwable An exception to log
     */
    public static void d(final String msg, final Throwable throwable) {
        CustomLogger.log(DEBUG, msg + '\n' + Log.getStackTraceString(throwable));
    }

    /**
     * Send a @VERBOSE log message and log the exception.
     *
     * @param msg       The message you would like logged
     * @param throwable An exception to log
     */
    public static void v(final String msg, final Throwable throwable) {
        CustomLogger.log(VERBOSE, msg + '\n' + Log.getStackTraceString(throwable));
    }

    /**
     * Send an @INFO log message and log the exception.
     *
     * @param msg       The message you would like logged
     * @param throwable An exception to log
     */
    public static void i(final String msg, final Throwable throwable) {
        CustomLogger.log(INFO, msg + '\n' + Log.getStackTraceString(throwable));
    }

    /**
     * Send a @WARN log message and log the exception.
     *
     * @param msg       The message you would like logged
     * @param throwable An exception to log
     */
    public static void w(final String msg, final Throwable throwable) {
        CustomLogger.log(WARN, msg + '\n' + Log.getStackTraceString(throwable));
    }

    /**
     * Send an @ERROR log message and log the exception.
     *
     * @param msg       The message you would like logged
     * @param throwable An exception to log
     */
    public static void e(final String msg, final Throwable throwable) {
        CustomLogger.log(ERROR, msg + '\n' + Log.getStackTraceString(throwable));
    }

    /**
     * Send a @WTF log message and log the exception.
     *
     * @param msg       The message you would like logged
     * @param throwable An exception to log
     */
    public static void wtf(final String msg, final Throwable throwable) {
        CustomLogger.log(WTF, msg + '\n' + Log.getStackTraceString(throwable));
    }

}
