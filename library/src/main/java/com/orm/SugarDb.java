package com.orm;

import android.content.Context;
import android.util.Log;

import com.orm.util.ManifestHelper;
import com.orm.util.SecureHelper;
import com.orm.util.SugarCursorFactory;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import static com.orm.util.ManifestHelper.getDatabaseVersion;
import static com.orm.util.ManifestHelper.getDebugEnabled;

public class SugarDb extends SQLiteOpenHelper {

    private final SchemaGenerator schemaGenerator;
    private SQLiteDatabase sqLiteDatabase;
    private SecureHelper secureHelper;
    private int openedConnections = 0;

    public SugarDb(Context context) {
        super(context, ManifestHelper.getDatabaseName(context),
                new SugarCursorFactory(getDebugEnabled(context)), getDatabaseVersion(context));
        schemaGenerator = new SchemaGenerator(context);
        secureHelper = new SecureHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        schemaGenerator.createDatabase(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        schemaGenerator.doUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    public synchronized SQLiteDatabase getDB() {
        if (this.sqLiteDatabase == null) {
            this.sqLiteDatabase = getWritableDatabase(secureHelper.getKey());
        }

        return this.sqLiteDatabase;
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase(String password) {
        Log.d("SUGAR", "getReadableDatabase");
        openedConnections++;
        return super.getReadableDatabase(password);
    }

    @Override
    public synchronized void close() {
        Log.d("SUGAR", "getReadableDatabase");
        openedConnections--;
        if(openedConnections == 0) {
            Log.d("SUGAR", "closing");
            super.close();
        }
    }
}
