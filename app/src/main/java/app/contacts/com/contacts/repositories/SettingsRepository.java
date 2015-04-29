package app.contacts.com.contacts.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.contacts.com.contacts.models.Settings;

public class SettingsRepository  extends SQLiteOpenHelper {

    public SettingsRepository(Context context) {
        super(context, "Contacts", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlQueries.CREATE_SETTINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {}

    public Settings getSettings() {

        Cursor cursor = this.getReadableDatabase().rawQuery(SqlQueries.GET_SETTINGS, new String[] {});

        Settings settings = new Settings();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                settings.setSortBy(cursor.getInt(cursor.getColumnIndex("sort_by")));
                settings.setOrder(cursor.getInt(cursor.getColumnIndex("order")));
            }
        }
        return settings;
    }
}