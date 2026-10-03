# Keep Retrofit & Gson models
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
