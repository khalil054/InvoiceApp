/*
package test.invoicegenerator.databaseutilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SqliteHelper {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private String DbName="InvoiceGenerator";
    private String TABLE_NAME="AddNewClient";
    private static final String CLIENT_ID = "id";
    public static final String CLIENT_Name = "ClientName";
    public static final String CLIENT_PHONE = "ClientPhone";
    public static final String CLIENT_CITY = "ClientCity";
    public static final String CLIENT_EMAIL = "ClientEmail";
    public static final String CLIENT_ADDRESS = "ClientAddress";
    public static final String CLIENT_STATE = "ClientState";
    public static final String CLIENT_ZIPCODE = "ClientZipCode";
    public static final String CLIENT_COUNTRY = "ClientCountry";

    public SqliteHelper(Context context) {
        this.context = context;
        sqLiteDatabase=context.openOrCreateDatabase(DbName,MODE_PRIVATE,null);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS AddNewClient(id INTEGER PRIMARY KEY AUTOINCREMENT, ClientName VARCHAR, ClientEmail VARCHAR, ClientCity VARCHAR,ClientPhone VARCHAR,ClientAddress VARCHAR,ClientState VARCHAR,ClientZipCode VARCHAR,ClientCountry VARCHAR)");

    }

                    */
/*Insert Client Info*//*


   public boolean AddNewClient(ContentValues contentValues){
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) > 0;

    }

            */
/*Remove Client Info*//*


    public boolean DeleteSingleClient(String Id){
        return sqLiteDatabase.delete(TABLE_NAME, CLIENT_ID+"='" + Id + "'", null) > 0;
    }

                */
/*Remove Client Table*//*


     public boolean ClearClientTable(){
        return sqLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }

    */
/*Fetch ALl Client Table DB*//*


    public List<Client> AllClientDataList(){
        List<Client>AllClients=new ArrayList<>();

        @SuppressLint("Recycle") Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    int clientID=cursor.getInt(cursor.getColumnIndex(CLIENT_ID));
                    String clientName=cursor.getString(cursor.getColumnIndex(CLIENT_Name));
                    String clientPhone=cursor.getString(cursor.getColumnIndex(CLIENT_PHONE));
                    String clientCity=cursor.getString(cursor.getColumnIndex(CLIENT_CITY));
                    String clientEmail=cursor.getString(cursor.getColumnIndex(CLIENT_EMAIL));
                    String clientAddress=cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS));
                    String clientCountry=cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS));
                    String clientZipCode=cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS));
                    String clientState=cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS));

                  //  AllClients.add(0,new ClientInfo(clientID,clientName,clientPhone,clientCity,clientEmail,clientAddress));
                    AllClients.add(0,new Client(clientID,clientName,clientPhone,clientEmail,clientAddress,clientCity,clientState,clientCountry,clientZipCode));
                    cursor.moveToNext();
                }
            }
        }

        return AllClients;
    }
    */
/*Fetch  Client Table Size DB*//*


   public int getClientTableSize(){

        int tbSize=0;
        @SuppressLint("Recycle") Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        if(cursor!=null) {
            if (cursor.getCount() > 0) {

                tbSize=cursor.getCount();
            }
        }
        return tbSize;
    }

}
*/
