package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBConfig.DataBaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DataBaseTransactionDAO implements TransactionDAO {
    private final SimpleDateFormat dateFormat;
    public SQLiteDatabase readableDB;
    public SQLiteDatabase writeableDB;

    public DataBaseTransactionDAO(SQLiteDatabase readableDB,SQLiteDatabase writeableDB){
        this.readableDB = readableDB;
        this.writeableDB =writeableDB;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        System.out.println("logTransaction");
        ContentValues value = new ContentValues();
        value.put(DataBaseHelper.TRANSACTION_DATE, this.dateFormat.format(date));
        value.put(DataBaseHelper.TRANSACTION_ACCOUNTNO,accountNo);
        value.put(DataBaseHelper.TRANSACTION_EXPENSETYPE, String.valueOf(expenseType));
        value.put(DataBaseHelper.TRANSACTION_AMOUNT,String.valueOf(amount));

        if(this.writeableDB.insert(DataBaseHelper.TRANSACTION_TABLE,null,value) == -1){
            System.out.println("insert failed");
        }else{
            System.out.println("insert passed");
        }

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        System.out.println("getAllTransactionLogs");
        List<Transaction> all_transaction = new ArrayList<Transaction>();

        String query = "SELECT * FROM "+ DataBaseHelper.TRANSACTION_TABLE+" ORDER BY "+DataBaseHelper.TRANSACTION_DATE+" DESC;";
        Cursor pointer = this.readableDB.rawQuery(query,null);

        if(pointer.moveToFirst()){
            try {
                do{
                    all_transaction.add(
                            new Transaction(
                                    this.dateFormat.parse(pointer.getString(1)),
                                    pointer.getString(2),
                                    ExpenseType.valueOf(pointer.getString(3).toUpperCase()),
                                    pointer.getDouble(4)
                            ));
                }while(pointer.moveToNext());
            }catch(Exception error){
                error.printStackTrace();
            }
        }

        pointer.close();
        System.out.println(all_transaction);
        return all_transaction;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        System.out.println("getPaginatedTransactionLogs");
        List<Transaction> all_transaction = new ArrayList<Transaction>();

        String query = "SELECT * FROM "+ DataBaseHelper.TRANSACTION_TABLE+" ORDER BY "+DataBaseHelper.TRANSACTION_DATE+" DESC LIMIT ?;";
        Cursor pointer = this.readableDB.rawQuery(query,new String[]{String.valueOf(limit)});

        if(pointer.moveToFirst()){
            try {
                do{
                    all_transaction.add(
                            new Transaction(
                                    this.dateFormat.parse(pointer.getString(1)),
                                    pointer.getString(2),
                                    ExpenseType.valueOf(pointer.getString(3).toUpperCase()),
                                    pointer.getDouble(4)
                            ));
                }while(pointer.moveToNext());
            }catch(Exception error){
                error.printStackTrace();
            }
        }

        pointer.close();
        System.out.println(all_transaction);
        return all_transaction;
    }
}
