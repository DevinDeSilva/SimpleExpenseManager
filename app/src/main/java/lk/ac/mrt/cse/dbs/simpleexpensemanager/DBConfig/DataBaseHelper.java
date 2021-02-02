package lk.ac.mrt.cse.dbs.simpleexpensemanager.DBConfig;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String ACCOUNT_TABLE = "account_table";
    public static final String ACCOUNT_ID = "ID";
    public static final String ACCOUNT_ACCOUNTNO = "accountNo";
    public static final String ACCOUNT_BANKNAME = "bankName";
    public static final String ACCOUNT_HOLDERNAME = "accountHolderName";
    public static final String ACCOUNT_BALANCE = "balance";
    public static final String TRANSACTION_TABLE = "transaction_table";
    public static final String TRANSACTION_ID = "ID";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_ACCOUNTNO = "accountNo";
    public static final String TRANSACTION_EXPENSETYPE = "expenseType";
    public static final String TRANSACTION_AMOUNT = "amount";




    private static DataBaseHelper DBHelper = null;

    private DataBaseHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "180118_db", factory, version);
    }

    public static DataBaseHelper getDBHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        if(DataBaseHelper.DBHelper == null){
            DataBaseHelper.DBHelper = new DataBaseHelper(context,factory,version);
        }

        return DataBaseHelper.DBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + "(" +
                ACCOUNT_ACCOUNTNO + " TEXT PRIMARY KEY," +
                ACCOUNT_BANKNAME + " TEXT NOT NULL," +
                ACCOUNT_HOLDERNAME + " TEXT NOT NULL," +
                ACCOUNT_BALANCE + " REAL" +
                ");");
        sqLiteDatabase.execSQL("CREATE TABLE  IF NOT EXISTS "+TRANSACTION_TABLE+" ("+
                TRANSACTION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TRANSACTION_DATE+" DATE NOT NULL ," +
                TRANSACTION_ACCOUNTNO+" TEXT NOT NULL,"+
                TRANSACTION_EXPENSETYPE+" TEXT CHECK("+TRANSACTION_EXPENSETYPE+" IN ('EXPENSE', 'INCOME')),"+
                TRANSACTION_AMOUNT+" REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int version,int next_version) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }
}
