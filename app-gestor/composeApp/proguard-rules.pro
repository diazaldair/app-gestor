# Regla para que Room pueda instanciar la base de datos generada en KMP
-keep class * extends androidx.room.RoomDatabase { <init>(); }
