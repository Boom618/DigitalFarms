# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\user\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontwarn
-dontskipnonpubliclibraryclassmembers
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


 -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# 泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.os.IInterface

-keep class com.ty.digitalfarms.ui.activity.** {*;}
-keep class com.ty.digitalfarms.constant.**{*;}
-keep class com.ty.digitalfarms.bean.** {*;}
-keep class com.ty.digitalfarms.util.**{*;}
-keep class com.ty.digitalfarms.ui.fragment.**{*;}

#保持第3方jar包不混淆
-keep class com.squareup.okhttp.** {*;}
-keep class com.squareup.okhttp3.** {*;}
-keep class com.squareup.retrofit2.** {*;}
-keep class com.jakewharton.** {*;}
-keep class io.reactivex.** {*;}
-keep class rx.** {*;}
-keep class com.jakewharton.rxbinding.** {*;}
-keep class com.butterknife.** {*;}
-keep class com.google.code.gson.** {*;}

-dontwarn android.support.**
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment

-dontwarn android.support.**
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class !android.support.v7.internal.view.menu.**,android.support.** {*;}

#####butterknife#######
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-dontwarn butterknife.**
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**

# OkHttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** {*;}
-keep interface com.squareup.okhttp.** {*;}
-dontwarn okio.**

#腾讯Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
#海康威视
-keep class com.hikvision.netsdk.** {*;}
-dontwarn com.hikvision.**
-keep class com.hik.mcrsdk.** {*;}
-dontwarn com.hik.**


-keep class org.MediaPlayer.PlayM4.** {*;}
-dontwarn org.MediaPlayer.**


-dontwarn com.squareup.okhttp.**
-keep class com.zhouyou.http.** {*;}
-keep interface com.zhouyou.http.** {*;}

#排除HeWeather
-dontwarn interfaces.heweather.com.interfacesmodule.**
-keep class interfaces.heweather.com.interfacesmodule.** { *;}
