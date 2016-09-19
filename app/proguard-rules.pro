# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/guodong/Android/sdk/tools/proguard/proguard-android.txt
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

#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
#优化 不优化输入的类文件
-dontoptimize
#忽略警告
-ignorewarnings
#保护注解
-keepattributes *Annotation*
#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#apk 包内所有 class 的内部结构
#-dump class_files.txt
#未混淆的类和成员
#-printseeds seeds.txt
#列出从 apk 中删除的代码
#-printusage unused.txt
#混淆前后的映射
#-printmapping mapping.txt
#保持哪些类不被混淆
-keep class * extends android.app.Dialog
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.widget.LinearLayout
-keep public class * extends android.widget.RelativeLayout
-keep public class android.support.**{
	<methods>;
	<fields>;
}

#-libraryjars libs/ipaynow_alipay_v1.0.3.jar
#-libraryjars libs/ipaynow_base_v1.4.0a.jar
#-libraryjars libs/ipaynow_upmp_v1.0.3.jar

-keep class **.R$* {   *;  }
-keep class com.lzy.okhttputils.**
-keep class com.lzy.okhttputils.** { *; }
-keep class com.nostra13.universalimageloader.** { *; }

-keep class com.google.**{*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}
-keep class de.greenrobot.dao.** {*;}
#保持greenDao的方法不被混淆
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
#用来保持生成的表名不被混淆
public static java.lang.String TABLENAME;
}
-keep class **$Properties
