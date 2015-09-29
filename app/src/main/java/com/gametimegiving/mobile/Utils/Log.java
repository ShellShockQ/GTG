package com.gametimegiving.mobile.Utils;

public class Log {
    private static String TAG = "qeala";
    private static boolean ENABLED = true;

    public static void i(final String category, final String message) {
        if (!ENABLED) return;

        if (android.util.Log.isLoggable(TAG, android.util.Log.INFO)) {

            int iMax = 3000;
            String value = message;

            while (value != null) {
                if (value.length() > iMax) {
                    android.util.Log.i(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value.substring(0, iMax)));
                    value = value.substring(iMax);
                } else {
                    android.util.Log.i(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value));
                    value = null;
                }
            }
        } else {
            Log.i(category, message);
        }
    }

    public static void v(final String category, final String message) {
        if (!ENABLED) return;

        if (android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE)) {

            int iMax = 3000;
            String value = message;

            while (value != null) {
                if (value.length() > iMax) {
                    android.util.Log.v(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value.substring(0, iMax)));
                    value = value.substring(iMax);
                } else {
                    android.util.Log.v(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value));
                    value = null;
                }
            }
        } else {
            Log.i(category, message);
        }
    }

    public static void d(final String category, final String message) {
        if (!ENABLED) return;

        if (android.util.Log.isLoggable(TAG, android.util.Log.DEBUG)) {

            int iMax = 3000;
            String value = message;

            while (value != null) {
                if (value.length() > iMax) {
                    android.util.Log.d(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value.substring(0, iMax)));
                    value = value.substring(iMax);
                } else {
                    android.util.Log.d(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value));
                    value = null;
                }
            }
        } else {
            Log.i(category, message);
        }
    }

    public static void e(final String category, final Exception exc) {
        if (!ENABLED) return;

        String message = exc.toString();

        if (android.util.Log.isLoggable(TAG, android.util.Log.ERROR)) {

            int iMax = 3000;
            String value = message;

            while (value != null) {
                if (value.length() > iMax) {
                    android.util.Log.e(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value.substring(0, iMax)));
                    value = value.substring(iMax);
                } else {
                    android.util.Log.e(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value), exc);
                    value = null;
                }
            }
        } else {
            Log.i(category, message);
        }
    }

    public static void e(final String category, final String message) {
        if (!ENABLED) return;

        if (android.util.Log.isLoggable(TAG, android.util.Log.ERROR)) {

            int iMax = 3000;
            String value = message;

            while (value != null) {
                if (value.length() > iMax) {
                    android.util.Log.e(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value.substring(0, iMax)));
                    value = value.substring(iMax);
                } else {
                    android.util.Log.e(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value));
                    value = null;
                }
            }
        } else {
            Log.i(category, message);
        }
    }

    public static void w(final String category, final Exception exc) {
        if (!ENABLED) return;
        Log.w(category, exc.getMessage());
    }

    public static void w(final String category, final Throwable throwable) {
        if (!ENABLED) return;
        Log.w(category, throwable.getMessage());
    }

    public static void w(final String category, final String message) {
        if (!ENABLED) return;

        if (android.util.Log.isLoggable(TAG, android.util.Log.WARN)) {

            int iMax = 3000;
            String value = message;

            while (value != null) {
                if (value.length() > iMax) {
                    android.util.Log.w(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value.substring(0, iMax)));
                    value = value.substring(iMax);
                } else {
                    android.util.Log.w(TAG, String.format(java.util.Locale.ENGLISH, "%s:%s", category, value));
                    value = null;
                }
            }
        } else {
            Log.i(category, message);
        }
    }
}

