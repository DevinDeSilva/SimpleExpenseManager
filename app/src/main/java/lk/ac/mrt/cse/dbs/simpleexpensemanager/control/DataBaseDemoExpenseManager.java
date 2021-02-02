package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;


import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.DBConfig.DataBaseHelper;


public class DataBaseDemoExpenseManager extends ExpenseManager{
    public  Context context;
    public DataBaseDemoExpenseManager(Context context) {
        this.context = context;
        setup();
    }


    @Override
    public void setup() {
        DataBaseHelper dbHelper = DataBaseHelper.getDBHelper(this.context,null,1);
        TransactionDAO DataBaseTransactionDAO = new DataBaseTransactionDAO(dbHelper.getReadableDatabase(),dbHelper.getWritableDatabase());

        setTransactionsDAO(DataBaseTransactionDAO);

        AccountDAO DataBaseAccountDAO = new DataBaseAccountDAO(dbHelper.getReadableDatabase(),dbHelper.getWritableDatabase());
        setAccountsDAO(DataBaseAccountDAO);

        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);
    }
}
