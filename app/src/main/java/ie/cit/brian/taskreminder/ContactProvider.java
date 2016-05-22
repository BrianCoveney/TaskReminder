package ie.cit.brian.taskreminder;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

/**
 * Created by brian on 17/05/16.
 */
public class ContactProvider extends android.content.ContentProvider {

    // unique namespace
    static final String PROVIDER_NAME = "ie.cit.brian.taskreminder.ContactProvider";

    static final String URL = "content://" + PROVIDER_NAME +
            "/cpcontacts";

    public static final Uri CONTENT_URL = Uri.parse(URL);

    static final String id = "id";
    public static final String name = "name";
    public static final String email = "email";

    static final int uriCode = 1;

    private static HashMap<String, String> values;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "cpcontacts", uriCode);
    }


    private SQLiteDatabase sqlDB;
    public static final String DATABASE_NAME = "myContacts";
    public static final String TABLE_NAME = "names";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_DB_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " name TEXT NOT NULL, email VARCHAR);";

    private Button deleteDBBtn;


    @Override
    public boolean onCreate() {

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sqlDB = dbHelper.getWritableDatabase();

        if(sqlDB != null){
            return true;
        }
        return false;
    }





    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)){

            case uriCode:
                queryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = queryBuilder.query(sqlDB, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)){

            case uriCode:
                return "vnd.android.cursor.dir/cpcontacts";
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = sqlDB.insert(TABLE_NAME, null, values);

        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URL, rowID);

            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }else{
            Toast.makeText(getContext(), "Row Insert Failed",
                    Toast.LENGTH_LONG).show();
            return null;
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted = 0;

        switch (uriMatcher.match(uri)){

            case uriCode:
                rowsDeleted = sqlDB.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int rowsUpdated = 0;

        switch (uriMatcher.match(uri)){

            case uriCode:
                rowsUpdated = sqlDB.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }





    // inner class to Create and Manage our Database
    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqlDB) {
            sqlDB.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {

            sqlDB.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
            onCreate(sqlDB);

        }

//        public void deleteDatabase() {
//
//
//            String myPath = DB_PATH + DATABASE_NAME;
//            SQLiteDatabase.deleteDatabase(new File(myPath));
//
//        }



    }
}
