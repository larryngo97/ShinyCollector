package com.larryngo.shinyhunter.database;

import android.content.Context;

import com.larryngo.shinyhunter.ObjectTypeConverters;
import com.larryngo.shinyhunter.models.Counter;
import com.larryngo.shinyhunter.models.CounterDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Counter.class}, version = 3, exportSchema = false)
@TypeConverters(ObjectTypeConverters.class)
public abstract class DatabaseHolder extends RoomDatabase {

    private static DatabaseHolder database;

    @NonNull
    public static DatabaseHolder database() {
        return database;
    }

    public abstract CounterDao counterDao();

    public static void init(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(), DatabaseHolder.class, "hunting-database")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .fallbackToDestructiveMigration()
                .build();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE counters "
                    + " ADD COLUMN position INTEGER NOT NULL DEFAULT 0");
        }
    };

}
