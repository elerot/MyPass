package com.elerot.mypass2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by selcuk.celik on 21.12.2015.
 */
public class DBAdapter {
    private static final String DATABASE_NAME = "MyPass.db";

    private static final int DATABASE_VERSION = 2;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    /*private static final String CREATE_TABLE = "Create table if not exists USERS "
            + "(ID integer primary key autoincrement, UserName VARCHAR, Pass VARCHAR, Description VARCHAR);";
            */
    private static final String CREATE_TABLE = "Create table if not exists DATAS "
            + "(ID integer primary key autoincrement, UserID integer, DisplayName VARCHAR, UserName VARCHAR, Pass VARCHAR, Description VARCHAR);";

    DBAdapter(Context ctx) {
        DBHelper = new DatabaseHelper(ctx);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                //db.execSQL(CREATE_TABLE);
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //Log.w()
                db.execSQL("DROP TABLE IF EXISTS USERS");
                db.execSQL("DROP TABLE IF EXISTS DATAS");
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public DBAdapter openR() throws SQLException {
        db = DBHelper.getReadableDatabase();
        return this;
    }

    private DBAdapter openW() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long UpsertData(long DataID, long UserID, String Title, String UserName, String Pass, String Detail) {
        long result = -1;
        try {
            openW();
            ContentValues initialValues = new ContentValues();
            initialValues.put("DisplayName", Title);
            initialValues.put("UserID", Integer.parseInt(String.valueOf(UserID)));
            initialValues.put("UserName", UserName);
            initialValues.put("Pass", Pass);
            initialValues.put("Description", Detail);
            if (DataID < 0) {
                result = db.insert("DATAS", null, initialValues);
            } else {
                result = db.update("DATAS", initialValues, "ID=" + DataID, null);
            }
            close();
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
        return result;
    }

    public Cursor GetAllDatas(long userID) {
        Cursor mCursorU = null;
        try {
            String queryU = "SELECT ID _id,DisplayName,UserName,Pass,Description FROM DATAS Where UserID=" + userID;
            mCursorU = db.rawQuery(queryU, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mCursorU;
    }

    public String GetPass(long dataID) {
        String pass = "";
        try {
            openR();
            String queryU = "SELECT Pass FROM DATAS Where ID=" + dataID;
            Cursor mCursorU = db.rawQuery(queryU, null);
            if (mCursorU != null) {
                mCursorU.moveToFirst();
                pass = mCursorU.getString(0);
            }
            if (mCursorU != null) {
                mCursorU.close();
            }
            close();
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
        return pass;
    }

    public long InsertUser(String UserName, String Pass) {
        long result = -1;
        try {
            openW();
            ContentValues initialValues = new ContentValues();
            initialValues.put("UserName", UserName);
            initialValues.put("Pass", Pass);
            result = db.insert("USERS", null, initialValues);
            close();
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
        return result;
    }
    public boolean deleteAllData() {
        boolean b = false;
        try {
            openW();
            b = db.delete("DATAS", "1=1", null) > 0;
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }
    public boolean deleteData(long rowId) {
        boolean b = false;
        try {
            openW();
            b = db.delete("DATAS", "ID=" + rowId, null) > 0;
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public long CheckUser(String userName) {
        long result = -1;
        try {
            openR();
            String queryU = String.format("SELECT * FROM USERS WHERE UserName = '%s'", userName);
            Cursor mCursorU = db.rawQuery(queryU, null);
            if (mCursorU != null) {
                if (mCursorU.moveToFirst())
                    result = Long.parseLong(mCursorU.getString(0));
            }
            if (mCursorU != null) {
                mCursorU.close();
            }
            close();
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
        return result;
    }

    public long GetUserID(String userName, String pass) {
        long result = -1;
        try {
            if (CheckUser(userName) > -1) {
                openR();
                String query = "SELECT * FROM USERS WHERE UserName = '" + userName + "' and Pass='" + pass + "'";
                Cursor mCursor = db.rawQuery(query, null);
                if (mCursor != null) {
                    if (mCursor.moveToFirst())
                        result = Long.parseLong(mCursor.getString(0));
                    else
                        result = 0;
                }
                if (mCursor != null) {
                    mCursor.close();
                }
            }
            close();
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
        return result;
    }

}

