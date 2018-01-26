# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk1/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keep class org.apache.** { *; }
-dontwarn org.apache.**

-keep class com.jakewharton.** { *; }
-dontwarn com.jakewharton.**

-keep class org.mortbay.** { *; }
-dontwarn org.mortbay.**

-keep class javax.** { *; }
-dontwarn javax.**

-keep class at.technikum.** { *; }
-dontwarn at.technikum.**

-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**

-keep class java.lang.management.** { *; }
-dontwarn java.lang.management.**


-keep class twitter4j.** { *; }
-dontwarn twitter4j.**

-dontnote **ILicensingService