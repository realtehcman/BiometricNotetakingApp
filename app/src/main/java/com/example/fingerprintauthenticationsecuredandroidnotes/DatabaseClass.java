package com.example.fingerprintauthenticationsecuredandroidnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


import android.widget.Toast;

import androidx.annotation.Nullable;
/*

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


import android.app.Activity;
import android.os.Bundle;
*/

//https://medium.com/@balvierjha/database-encryption-saving-data-securely-in-android-fbb80795dba1
//https://www.secureideas.com/blog/2014/10/sqlite-good-bad-embedded-database.html
//AES - 256 ENCRYPTION
//https://discuss.zetetic.net/t/how-does-sqlcipher-compare-to-other-sqlite-encryption-extensions/2864
//https://www.youtube.com/watch?v=FkaGAsywNmU
public class DatabaseClass extends SQLiteOpenHelper {

    Context context;
    private static final String DatabaseName = "MyNotes";
    private static final int DatabaseVersion = 1;

    private static final String TableName = "mynotes";
    private static final String ColumnId = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnDescription = "description";

    private static final String GET_DATA = MainActivity.pass;

/*    private void InitializeSQLCipher() {
        net.sqlcipher.database.SQLiteDatabase.loadLibs(this);
        File databaseFile = getDatabasePath("demo.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        net.sqlcipher.database.SQLiteDatabase database = net.sqlcipher.database.SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);
        database.execSQL("create table t1(a, b)");
        database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"one for the money",
                "two for the show"});
    }*/

    public DatabaseClass(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TableName + " (" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ColumnTitle + " TEXT, " +
                ColumnDescription + " TEXT);";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }

    void addNotes(String title,String description) {

        SQLiteDatabase db = this.getWritableDatabase(GET_DATA);

        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long resultValue = db.insert(TableName, null, cv);
        if (resultValue == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Note added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor getAllNotes() {
        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase database = this.getReadableDatabase(GET_DATA);

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteAllNotes() {
        SQLiteDatabase database = this.getWritableDatabase(GET_DATA);
        String query = "DELETE FROM " + TableName;
        database.execSQL(query);
    }

    public void updateNote(String id, String title, String description) {
        SQLiteDatabase database = this.getWritableDatabase(GET_DATA);
        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle,title);
        cv.put(ColumnDescription,description);

        long result = database.update(TableName,cv,"id=?",new String[]{id});
        if (result==-1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteSingleItem(String id) {
        SQLiteDatabase database = this.getWritableDatabase(GET_DATA);
        long result = database.delete(TableName, "id=?",new String[]{id});
        if (result==-1){
            Toast.makeText(context, "Item Not Deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
