# This keeps are to prevent errors by proguard optimizations / ofuscating

-keep class com.kuriamind.modules.blocks.Block { *; }
-keep class com.kuriamind.modules.stats.DailyStats { *; }
-keep class com.kuriamind.modules.stats.AppDayStats { *; }


# Remove debug logs
-assumenosideeffects class android.util.Log {
        public static *** v(...);
        public static *** d(...);
        public static *** i(...);
    }
