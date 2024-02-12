# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/gaurav/Downloads/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
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
-ignorewarnings

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-verbose
#-dontobfuscate
-forceprocessing
-dontwarn com.google.**.R
-dontwarn com.google.**.R$*
-dontwarn com.google.android.gms.internal.**
-dontnote
-keepattributes SourceFile,LineNumberTable
-dontwarn com.payu.custombrowser.Bank

-keep public class com.google.ads.** {
    public protected *;
}


-keep public class com.google.android.gms.internal.** {
    public protected *;
}

-keep public class com.google.android.gms.ads.** {
    public protected *;
}

-keep class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    java.lang.String NULL;
}

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses

-keepclassmembers class com.payu.custombrowser.** {
*;
}

-keepclassmembers class * {
@android.webkit.JavascriptInterface <methods>;
}

-keepattributes *Annotation*, Exceptions, Signature, Deprecated, SourceFile, SourceDir, LineNumberTable, LocalVariableTable, LocalVariableTypeTable, Synthetic, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations, AnnotationDefault, InnerClasses
-keep class org.bytedeco.javacpp.** {*;}
-dontwarn java.awt.**
-dontwarn org.bytedeco.javacv.**
-dontwarn org.bytedeco.javacpp.**
-keep public class com.payu.sdk.ProcessPaymentActivity$PayUJavaScriptInterface
-keep public class * implements com.payu.sdk.ProcessPaymentActivity$PayUJavaScriptInterface

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
 #GSON Config
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class org.eclipse.paho.client.mqttv3.logging.JSR47Logger { *; }
-keep class android.support.** { *; }
-keep interface android.support.** { *; }


# Application classes that will be serialized/deserialized over Gson
-keep class com.partseazy.android.ui.model.** { *; }


##---------------Begin: proguard configuration for Applogic  ------>>>>>

##keep json classes
# -keepclassmembernames class * extends com.applozic.mobicommons.json.JsonMarker {
#    !static !transient <fields>;
# }
#
# -keepclassmembernames class * extends com.applozic.mobicommons.json.JsonParcelableMarker {
#    !static !transient <fields>;
# }


-dontwarn com.google.android.gms.**
-dontnote com.google.android.gms.**
-keep class com.google.android.gms.location.ActivityRecognitionResult

-keepclassmembers class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final java.lang.String NULL;
}

-keep,allowobfuscation @interface com.google.android.gms.common.annotation.KeepName
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

 # Google Cloud Messaging.
-keep,allowshrinking class **.GCMIntentService

-dontwarn com.mixpanel.**

#-dontwarn com.applozic.**


-dontwarn org.greenrobot.eventbus.**


-keepclassmembers class ** {
    public void onEvent*(**);
}

-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}




#keep json classes
 -keepclassmembernames class * extends com.applozic.mobicommons.json.JsonMarker {
    !static !transient <fields>;
 }

 -keepclassmembernames class * extends com.applozic.mobicommons.json.JsonParcelableMarker {
    !static !transient <fields>;
 }



##---------------Begin: proguard MoEngage   <<<<----------

#
#-dontwarn com.google.android.gms.location.**
#-dontwarn com.google.android.gms.gcm.**
#-dontwarn com.google.android.gms.iid.**
#
#-keep class com.google.android.gms.gcm.** { *; }
#-keep class com.google.android.gms.iid.** { *; }
#-keep class com.google.android.gms.location.** { *; }

-keep class com.moe.pushlibrary.activities.** { *; }
#-keep class com.moengage.locationlibrary.GeofenceIntentService
-keep class com.moe.pushlibrary.InstallReceiver
#-keep class com.moengage.push.MoEPushWorker
-keep class com.moe.pushlibrary.providers.MoEProvider
#-keep class com.moengage.receiver.MoEInstanceIDListener
#-keep class com.moengage.worker.MoEGCMListenerService
-keep class com.moe.pushlibrary.models.** { *;}
#-keep class com.moengage.core.GeoTask
#-keep class com.moengage.location.GeoManager
#-keep class com.moengage.inapp.InAppManager
#-keep class com.moengage.push.PushManager
#-keep class com.moengage.inapp.InAppController


#-dontwarn com.moengage.location.GeoManager
#-dontwarn com.moengage.core.GeoTask
#-dontwarn com.moengage.receiver.*
#-dontwarn com.moengage.worker.*
#-dontwarn com.moengage.inapp.ViewEngine

-keep class com.delight.**  { *; }

-keep class cn.pedant.** { *; }

-keep class com.shockwave.**

