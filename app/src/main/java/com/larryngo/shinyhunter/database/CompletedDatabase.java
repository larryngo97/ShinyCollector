package com.larryngo.shinyhunter.database;

import android.content.Context;

import com.larryngo.shinyhunter.util.ObjectTypeConverters;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.models.CounterDao;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Counter.class}, version = 1, exportSchema = false)
@TypeConverters(ObjectTypeConverters.class)
public abstract class CompletedDatabase extends RoomDatabase {
    private static CompletedDatabase database;

    @NonNull
    public static CompletedDatabase database() {
        return database;
    }

    public abstract CounterDao counterDao();

    @MainThread
    public static void init(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(), CompletedDatabase.class, "completed-database")
                .addMigrations(MIGRATION_1_2)
                //.fallbackToDestructiveMigration()
                .build();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

}
