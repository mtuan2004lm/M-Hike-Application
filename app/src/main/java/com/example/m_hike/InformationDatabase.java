package com.example.m_hike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InformationDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserInformation.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và cột
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "_id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phone";
    private static final String COL_PASSWORD = "password";

    // Lệnh tạo bảng
    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_NAME + " TEXT," +
                    COL_EMAIL + " TEXT UNIQUE," +
                    COL_PHONE + " TEXT," +
                    COL_PASSWORD + " TEXT)";

    public InformationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /**
     * Chèn thông tin người dùng mới.
     * ĐÃ SỬA LỖI: Không gọi db.close() ở đây.
     */
    public long insertUser(String name, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, phone);
        values.put(COL_PASSWORD, password);

        return db.insert(TABLE_USERS, null, values);
    }

    /**
     * Kiểm tra email và mật khẩu để đăng nhập.
     */
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean userExists = false;

        String[] columns = {COL_ID};
        String selection = COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        try {
            cursor = db.query(TABLE_USERS,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            if (cursor != null && cursor.getCount() > 0) {
                userExists = true;
            }
        } catch (Exception e) {
            // Xử lý lỗi
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userExists;
    }
}