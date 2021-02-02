package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBConfig.DataBaseHelper;



/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */

public class DataBaseAccountDAO implements AccountDAO{
    public SQLiteDatabase readableDB;
    public SQLiteDatabase writeableDB;

    public DataBaseAccountDAO(SQLiteDatabase readableDB,SQLiteDatabase writeableDB){
        this.readableDB = readableDB;
        this.writeableDB =writeableDB;
    }

    @Override
    public List<String> getAccountNumbersList() {
        System.out.println("getAccountNumbersList");
        List<String> list_account_no = new ArrayList<String>();

        String query = "SELECT "+DataBaseHelper.ACCOUNT_ACCOUNTNO+" FROM "+ DataBaseHelper.ACCOUNT_TABLE;
        Cursor pointer = this.readableDB.rawQuery(query,null);

        if(pointer.moveToFirst()){
            do{
                list_account_no.add(pointer.getString(0));
            }while(pointer.moveToNext());
        }

        pointer.close();
        System.out.println(list_account_no);
        return list_account_no;
    }

    @Override
    public List<Account> getAccountsList() {
        System.out.println("getAccountList");
        List<Account> list_accounts = new ArrayList<Account>();

        String query = "SELECT * FROM "+ DataBaseHelper.ACCOUNT_TABLE;
        Cursor pointer = this.readableDB.rawQuery(query,null);

        if(pointer.moveToFirst()){
            do{
                list_accounts.add(new Account(pointer.getString(0),pointer.getString(1),pointer.getString(2),pointer.getDouble(3)));
            }while(pointer.moveToNext());
        }

        pointer.close();
        return list_accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        System.out.println("getAccount");
        Account record = null;
        String query = "SELECT * FROM "+ DataBaseHelper.ACCOUNT_TABLE +" WHERE "+DataBaseHelper.ACCOUNT_ACCOUNTNO+"=?;";
        Cursor pointer = this.readableDB.rawQuery(query,new String[]{accountNo});

        if(pointer.moveToFirst()){
            do{
                record = new Account(pointer.getString(0),pointer.getString(1),pointer.getString(2),pointer.getDouble(3));
            }while(pointer.moveToNext());
        }

        pointer.close();
        return record;
    }

    @Override
    public void addAccount(Account account) {
        System.out.println("addAccount");
        try {
            Account result = this.getAccount(account.getAccountNo());
            if (result != null){
                System.out.println("Account Already exist");
                return;
            }
        }catch(Exception error){
            error.printStackTrace();
        }


        ContentValues value = new ContentValues();

        value.put(DataBaseHelper.ACCOUNT_ACCOUNTNO,account.getAccountNo());
        value.put(DataBaseHelper.ACCOUNT_BANKNAME,account.getBankName());
        value.put(DataBaseHelper.ACCOUNT_HOLDERNAME,account.getAccountHolderName());
        value.put(DataBaseHelper.ACCOUNT_BALANCE,String.valueOf(account.getBalance()));

        if(this.writeableDB.insert(DataBaseHelper.ACCOUNT_TABLE,null,value) == -1){
            System.out.println("insert failed");
        }else{
            System.out.println("insert passed");
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        System.out.println("removeAccount");
        String query = "DELETE FROM "+ DataBaseHelper.ACCOUNT_TABLE + " WHERE "+DataBaseHelper.ACCOUNT_ACCOUNTNO+"=?;";
        this.writeableDB.rawQuery(query, new String[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        System.out.println("updateBalance");
        Account account = this.getAccount(accountNo);
        if (account != null){
            if (expenseType == ExpenseType.EXPENSE){
                account.setBalance(account.getBalance()-amount);
                if (account.getBalance()<0){
                    throw new InvalidAccountException("Account Balance negative.");
                }
            }else{
                account.setBalance(account.getBalance()+amount);
            }

            String query = "UPDATE "+DataBaseHelper.ACCOUNT_TABLE+ " SET "+DataBaseHelper.ACCOUNT_BALANCE+"= ?"+" WHERE "+DataBaseHelper.ACCOUNT_ACCOUNTNO+" =? ;";
            this.writeableDB.rawQuery(query, new String[]{Double.toString(account.getBalance()),account.getAccountNo()});
        }else{
            throw new InvalidAccountException("Account error.");
        }
    }
}
