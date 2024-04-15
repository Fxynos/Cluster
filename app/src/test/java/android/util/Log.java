package android.util;

public class Log {
    public static int w(String tag, String msg, Throwable throwable) {
        System.out.println("WARN: " + tag + ": " + msg);
        return 0;
    }}
