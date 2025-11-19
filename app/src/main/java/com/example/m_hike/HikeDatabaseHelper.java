package com.example.m_hike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class HikeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MHike.db";
    // Tăng Version để buộc onUpgrade chạy, khắc phục lỗi cấu trúc bảng
    private static final int DATABASE_VERSION = 2;

    // Tên bảng và cột cho HIKES
    private static final String TABLE_HIKES = "hikes";
    private static final String COL_HIKE_ID = "id";
    private static final String COL_HIKE_NAME = "name";
    private static final String COL_HIKE_LOCATION = "location";
    private static final String COL_HIKE_DATE = "date";
    private static final String COL_HIKE_PARKING = "parking";
    private static final String COL_HIKE_LENGTH = "length";
    private static final String COL_HIKE_DIFFICULTY = "difficulty";
    private static final String COL_HIKE_DESCRIPTION = "description";
    private static final String COL_HIKE_WEATHER = "weather_condition";
    private static final String COL_HIKE_EQUIPMENT = "equipment_required";

    // Tên bảng và cột cho OBSERVATIONS
    private static final String TABLE_OBSERVATIONS = "observations";
    private static final String COL_OBS_ID = "id";
    private static final String COL_OBS_HIKE_ID = "hike_id";
    private static final String COL_OBS_OBSERVATION = "observation";
    private static final String COL_OBS_TIME = "time";
    private static final String COL_OBS_COMMENTS = "comments";


    public HikeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Lệnh SQL để tạo bảng HIKES
        String CREATE_HIKES_TABLE = "CREATE TABLE " + TABLE_HIKES + " ("
                + COL_HIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_HIKE_NAME + " TEXT NOT NULL,"
                + COL_HIKE_LOCATION + " TEXT NOT NULL,"
                + COL_HIKE_DATE + " TEXT NOT NULL,"
                + COL_HIKE_PARKING + " TEXT NOT NULL,"
                + COL_HIKE_LENGTH + " REAL NOT NULL,"
                + COL_HIKE_DIFFICULTY + " TEXT NOT NULL,"
                + COL_HIKE_DESCRIPTION + " TEXT,"
                + COL_HIKE_WEATHER + " TEXT,"
                + COL_HIKE_EQUIPMENT + " TEXT" + ")";
        db.execSQL(CREATE_HIKES_TABLE);

        // Lệnh SQL để tạo bảng OBSERVATIONS
        String CREATE_OBSERVATIONS_TABLE = "CREATE TABLE " + TABLE_OBSERVATIONS + " ("
                + COL_OBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_OBS_HIKE_ID + " INTEGER NOT NULL,"
                + COL_OBS_OBSERVATION + " TEXT NOT NULL,"
                + COL_OBS_TIME + " TEXT NOT NULL,"
                + COL_OBS_COMMENTS + " TEXT,"
                + "FOREIGN KEY(" + COL_OBS_HIKE_ID + ") REFERENCES " + TABLE_HIKES + "(" + COL_HIKE_ID + ") ON DELETE CASCADE" + ")";
        db.execSQL(CREATE_OBSERVATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }


    // -------------------------------------------------------------------------
    // --- Phương thức CRUD cho HIKES ------------------------------------------
    // -------------------------------------------------------------------------

    public long addHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HIKE_NAME, hike.getName());
        values.put(COL_HIKE_LOCATION, hike.getLocation());
        values.put(COL_HIKE_DATE, hike.getDate());
        values.put(COL_HIKE_PARKING, hike.getParkingAvailable());
        values.put(COL_HIKE_LENGTH, hike.getLength());
        values.put(COL_HIKE_DIFFICULTY, hike.getDifficultyLevel());
        values.put(COL_HIKE_DESCRIPTION, hike.getDescription());
        values.put(COL_HIKE_WEATHER, hike.getWeatherCondition());
        values.put(COL_HIKE_EQUIPMENT, hike.getEquipmentRequired());

        long id = db.insert(TABLE_HIKES, null, values);
        db.close();
        return id;
    }

    /**
     * Lấy tất cả các chuyến đi từ cơ sở dữ liệu. (ĐÃ SỬA LỖI ĐỌC DỮ LIỆU VÀ QUẢN LÝ TÀI NGUYÊN)
     */
    public List<Hike> getAllHikes() {
        List<Hike> hikeList = new ArrayList<>();
        // Sử dụng getReadableDatabase()
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_HIKES + " ORDER BY " + COL_HIKE_DATE + " DESC";
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Hike hike = new Hike();

                    hike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HIKE_ID)));
                    hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_NAME)));
                    hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_LOCATION)));
                    hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_DATE)));
                    hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_PARKING)));
                    hike.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_HIKE_LENGTH)));
                    hike.setDifficultyLevel(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_DIFFICULTY)));
                    hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_DESCRIPTION)));
                    hike.setWeatherCondition(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_WEATHER)));
                    hike.setEquipmentRequired(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_EQUIPMENT)));

                    hikeList.add(hike);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return hikeList;
    }

    /**
     * Lấy một chuyến đi cụ thể dựa trên ID.
     */
    public Hike getHike(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Hike hike = null;

        try {
            cursor = db.query(TABLE_HIKES, null, COL_HIKE_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                hike = new Hike();
                hike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_HIKE_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_NAME)));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_LOCATION)));
                hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_DATE)));
                hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_PARKING)));
                hike.setLength(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_HIKE_LENGTH)));
                hike.setDifficultyLevel(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_DIFFICULTY)));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_DESCRIPTION)));
                hike.setWeatherCondition(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_WEATHER)));
                hike.setEquipmentRequired(cursor.getString(cursor.getColumnIndexOrThrow(COL_HIKE_EQUIPMENT)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return hike;
    }

    /**
     * Cập nhật thông tin chi tiết của chuyến đi.
     */
    public int updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HIKE_NAME, hike.getName());
        values.put(COL_HIKE_LOCATION, hike.getLocation());
        values.put(COL_HIKE_DATE, hike.getDate());
        values.put(COL_HIKE_PARKING, hike.getParkingAvailable());
        values.put(COL_HIKE_LENGTH, hike.getLength());
        values.put(COL_HIKE_DIFFICULTY, hike.getDifficultyLevel());
        values.put(COL_HIKE_DESCRIPTION, hike.getDescription());
        values.put(COL_HIKE_WEATHER, hike.getWeatherCondition());
        values.put(COL_HIKE_EQUIPMENT, hike.getEquipmentRequired());

        int result = db.update(TABLE_HIKES, values, COL_HIKE_ID + " = ?",
                new String[]{String.valueOf(hike.getId())});
        db.close();
        return result;
    }

    /**
     * Xóa một chuyến đi cụ thể dựa trên ID.
     */
    public int deleteHike(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_HIKES, COL_HIKE_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    // -------------------------------------------------------------------------
    // --- Phương thức CRUD cho OBSERVATIONS -----------------------------------
    // -------------------------------------------------------------------------

    public long addObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_OBS_HIKE_ID, observation.getHikeId());
        values.put(COL_OBS_OBSERVATION, observation.getObservation());
        values.put(COL_OBS_TIME, observation.getTimeOfTheObservation());
        values.put(COL_OBS_COMMENTS, observation.getAdditionalComments());

        long id = db.insert(TABLE_OBSERVATIONS, null, values);
        db.close();
        return id;
    }

    public List<Observation> getAllObservationsForHike(int hikeId) {
        List<Observation> observationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_OBSERVATIONS +
                    " WHERE " + COL_OBS_HIKE_ID + " = " + hikeId +
                    " ORDER BY " + COL_OBS_ID + " DESC";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Observation observation = new Observation();
                    observation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_OBS_ID)));
                    observation.setHikeId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_OBS_HIKE_ID)));
                    observation.setObservation(cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_OBSERVATION)));
                    observation.setTimeOfTheObservation(cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_TIME)));
                    observation.setAdditionalComments(cursor.getString(cursor.getColumnIndexOrThrow(COL_OBS_COMMENTS)));

                    observationList.add(observation);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return observationList;
    }

    public int updateObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_OBS_OBSERVATION, observation.getObservation());
        values.put(COL_OBS_TIME, observation.getTimeOfTheObservation());
        values.put(COL_OBS_COMMENTS, observation.getAdditionalComments());

        int result = db.update(TABLE_OBSERVATIONS, values, COL_OBS_ID + " = ?",
                new String[]{String.valueOf(observation.getId())});
        db.close();
        return result;
    }

    public int deleteObservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_OBSERVATIONS, COL_OBS_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public void deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HIKES);
        db.close();
    }
}