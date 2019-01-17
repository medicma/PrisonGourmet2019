package com.wolfenterprisesllc.prisongourmet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 13;
    private static final String DATABASE_NAME = "Favorite_Things2";
    private final String TABLE_NAME = "Favorites_Recipies2";
    private final String RECIPIE_NAME = "Recipie_Name";
    private final String KEY_ID = "id";



    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    DBHandler(Recipie recipie, Object o, Context context, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHandler(MainActivity mainActivity, String o, Context context, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHandler(RecyclerViewAdapter recyclerViewAdapter, String o, Context context, int i) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RECIPIE_NAME + " TEXT " +
                ");";
        db.execSQL(query);
        db.close();  //had to remove this here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        //be sure to change version number when you do this
    }

    private Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(true, TABLE_NAME, null, null, null, RECIPIE_NAME, null, RECIPIE_NAME + " ASC", null);
    }

    void addRecipie(String theRecipie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RECIPIE_NAME, theRecipie);
        db.insert(TABLE_NAME, null, values);
    }

    public void deleteRecipie(String deleteThis) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, RECIPIE_NAME + " = " + deleteThis, null);
        db.close();
    }

    List<RecipieHolder> getAllRecipies() { //called from favorite click only
        SQLiteDatabase db = this.getWritableDatabase();

        List<RecipieHolder> holder = new ArrayList<>();
        Cursor cursor = getAllData();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ArrayList<String> mArrayList = new ArrayList<>();
            RecipieHolder myHolder = new RecipieHolder();
            mArrayList.add(cursor.getString(1));
            myHolder.setRecipie(String.valueOf(mArrayList).replace("[", "").replace("]", ""));
            holder.add(myHolder);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return holder;
    }

    int isItThere(String deleteThis) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<RecipieHolder> holder = new ArrayList<>();
        Cursor cursor = getAllData();
        cursor.moveToFirst();
        ArrayList<String> mArrayList2 = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            RecipieHolder myHolder = new RecipieHolder();
            mArrayList2.add(cursor.getString(1));
            myHolder.setRecipie(String.valueOf(mArrayList2).replace("[", "").replace("]", ""));
            holder.add(myHolder);
            cursor.moveToNext();
        }ContentValues contentValues = new ContentValues();
        if (String.valueOf(mArrayList2).replace("[", "").replace("]", "").contains(deleteThis)) {
            cursor.close();
            db.close();
            return 1;
        } else {
            cursor.close();
            db.close();
            return 0;
        }
    }

    public int updateRecipie(RecipieHolder theOtherHolder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RECIPIE_NAME, theOtherHolder.getRecipie());
        db.close();
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(theOtherHolder.getRecipie())});
    }
}

