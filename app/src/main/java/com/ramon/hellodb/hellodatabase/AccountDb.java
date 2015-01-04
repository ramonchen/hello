package com.ramon.hellodb.hellodatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by ramon on 2015/1/4.
 */
public class AccountDb {
    class AccountItem {
        String desc;
        String userName;
        String password;

        String id;
    }

    private DBHelper mDb;

    private final String ACCOUNT_TABLE_NAME = "ACCOUNT_MAIN";

    private final String COLUMN_KEY = "id";
    private final String COLUMN_DESC = "desc";
    private final String COLUMN_USERNAME = "username";
    private final String COLUMN_PASSWORD = "password";

    AccountDb(Context ctx) {
        mDb = new DBHelper(ctx);
    }

    boolean open(String dbName) {
        try {
            mDb.open(dbName);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (! mDb.isTableExist(ACCOUNT_TABLE_NAME)) {
            // id是自动增长的主键，username和 password为字段名， text为字段的类型
            String sql = "CREATE TABLE " + ACCOUNT_TABLE_NAME;
            sql += " (" + COLUMN_KEY + " integer primary key autoincrement";
            sql += ", " + COLUMN_DESC + " text";
            sql += ", " + COLUMN_USERNAME + " text";
            sql += ", " + COLUMN_PASSWORD + " text)";
            mDb.execSQL(sql);
        }

        return true;
    }

    void close() {
        if (mDb != null) {
            mDb.closeConnection();
        }
    }

    void insert(String desc, String userName, String password) {
        if (mDb == null) {
            return;
        }

        ContentValues values = new ContentValues();

        values.put(COLUMN_DESC, desc);
        values.put(COLUMN_USERNAME, userName);
        values.put(COLUMN_PASSWORD, password);

        mDb.insert(ACCOUNT_TABLE_NAME, values);
    }

    ArrayList<AccountItem> getAccount() {
        ArrayList<AccountItem> accountList = new ArrayList<>();

        Cursor returnCursor = mDb.findList(false, ACCOUNT_TABLE_NAME, new String[] { COLUMN_KEY, COLUMN_DESC, COLUMN_USERNAME, COLUMN_PASSWORD }, null, null, null, null, COLUMN_DESC, null);
        while (returnCursor.moveToNext()) {
            AccountItem item = new AccountItem();
            item.id = returnCursor.getString(returnCursor.getColumnIndexOrThrow(COLUMN_KEY));
            item.desc = returnCursor.getString(returnCursor.getColumnIndexOrThrow(COLUMN_DESC));
            item.userName = returnCursor.getString(returnCursor.getColumnIndexOrThrow(COLUMN_USERNAME));
            item.password = returnCursor.getString(returnCursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            accountList.add(item);
        }

        return accountList;
    }

    void delete(String id) {
        mDb.delete(ACCOUNT_TABLE_NAME, COLUMN_KEY + " = ?", new String[] { id });
    }

    void update(String id, String desc, String userName, String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESC, desc);
        values.put(COLUMN_USERNAME, userName);
        values.put(COLUMN_PASSWORD, password);
        mDb.update(ACCOUNT_TABLE_NAME, values, COLUMN_KEY + " = ?", new String[] { id });
    }
}
