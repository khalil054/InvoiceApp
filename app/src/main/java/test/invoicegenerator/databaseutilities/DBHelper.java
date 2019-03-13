package test.invoicegenerator.databaseutilities;

/**
 * Created by User on 1/16/2019.
 */

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "invoice.db";
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
    public static final String ITEM_TABLE = "items";
    public static final String DESCRIPTION = "description";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 17);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
      /*  db.execSQL(
                "create table clients " +
                        "(id integer primary key, name text,phone text,email text, mobile text,fax text,contact text, line1 text,line2 text,line3 text, invoice_key INTEGER )"
        );*/
        db.execSQL(
                "create table items " +
                        "(id integer primary key, description text,unit_cost text,quantity text, amount text,taxable text,tax_rate text,additional text, invoice_key INTEGER )"
        );
        db.execSQL(
                "create table invoice " +
                        "(id integer primary key,client_key text,item_key text,subtotal text,discount text,dis_type text,tax_rate text,tax_type text,total_value text,signature text,photo text,comment text,invoice_name text,invoice_date text,due_date text,signature_date text)"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS AddNewClient(id INTEGER PRIMARY KEY AUTOINCREMENT, ClientName VARCHAR, ClientEmail VARCHAR, ClientCity VARCHAR,ClientPhone VARCHAR,ClientAddress VARCHAR,ClientState VARCHAR,ClientZipCode VARCHAR,ClientCountry VARCHAR,invoice_key INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS AddNewClient");
        db.execSQL("DROP TABLE IF EXISTS items");
        db.execSQL("DROP TABLE IF EXISTS invoice");
        onCreate(db);
    }

    public long insertContact(String name, String phone, String email, String contact, String city,
                              String state, String country,String zip_code, long invoice_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ClientName", name);
        contentValues.put("ClientPhone", phone);
        contentValues.put("ClientEmail", email);
        contentValues.put("ClientAddress", contact);
        contentValues.put("ClientCity", city);
        contentValues.put("ClientState", state);
        contentValues.put("ClientCountry", country);
        contentValues.put("ClientZipCode",zip_code);
        contentValues.put("invoice_key", invoice_key);


        return db.insert("AddNewClient", null, contentValues);
    }

    public long insertItemData(String des, String unit_cost, String quntity, String amount, String taxable, String tax_rate,
                               String additional, long invoice_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("description", des);
        contentValues.put("unit_cost", unit_cost);
        contentValues.put("quantity", quntity);
        contentValues.put("amount", amount);
        contentValues.put("taxable", taxable);
        contentValues.put("tax_rate", tax_rate);
        contentValues.put("additional", additional);
        contentValues.put("invoice_key", invoice_key);
        return db.insert("items", null, contentValues);

    }

    public long insertInvoiceData(String invoice_name,String invoice_date,String due_date,String subtotal, String discount, String discount_type,
                                  String tax_rate,
                                  String tax_type, String total_value, String comment,String signature_date,String photo,String signature) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("invoice_name", invoice_name);
        contentValues.put("due_date", due_date);
        contentValues.put("invoice_date", invoice_date);
        contentValues.put("subtotal", subtotal);
        contentValues.put("signature_date",signature_date);
        contentValues.put("signature",signature);
        contentValues.put("photo",photo);
        contentValues.put("discount", discount);
        contentValues.put("dis_type", discount_type);
        contentValues.put("tax_rate", tax_rate);
        contentValues.put("tax_type",tax_type);
        contentValues.put("total_value", total_value);
        contentValues.put("comment", comment);

        long id = db.insert("invoice", null, contentValues);


        return id;
    }

    public void updateInvoiceData(String column, String client_key, long invoice_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE invoice SET " + column + " = " + client_key + " WHERE id = " + invoice_key;

        db.execSQL(strSQL);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        //   Cursor res =  db.rawQuery( "select * from clients where id="+id+"", null );
        Cursor res = db.rawQuery("select * from AddNewClient", null);
        return res;
    }
    public Cursor getData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from AddNewClient where invoice_key="+id+"", null );
        // Cursor res = db.rawQuery("select * from clients", null);
        return res;
    }
    public Cursor getItemsData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from items where invoice_key=" + id + "", null);
        //  Cursor res =  db.rawQuery( "select * from items", null );
        return res;
    }

    public Cursor getInvoiceData() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor res =  db.rawQuery( "select * from invoice where id="+id+"", null );
        Cursor res = db.rawQuery("select * from invoice", null);
        // Cursor res =  db.rawQuery( "select * from invoice", null );
        return res;
    }

    public Cursor getInvoiceData(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from invoice where id=" + id + "", null);
        //  Cursor res =  db.rawQuery( "select * from invoice", null );
        // Cursor res =  db.rawQuery( "select * from invoice", null );
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "AddNewClient");
        return numRows;
    }

    public int getAttachment() {
        SQLiteDatabase db = this.getReadableDatabase();
  /*  Cursor cursor = db.query("invoice", new String[]{"photo",""});
    byte[] data = cursor.getBlob(0);*/
        return 0;
    }

    public boolean updateContact(Integer id, String name, String phone, String email, String street, String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
      /*  return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });*/
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
          //  array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public void updateDiscountInInvoice(String type, String value, long invoice_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        // String strSQL = "UPDATE invoice SET discount="+value+",dis_type="+type+" WHERE id = "+ invoice_key;
        String strSQL = "UPDATE invoice SET discount  = '" + value + "', dis_type = '" + type + "' WHERE id  = '" + invoice_key + "'";
        db.execSQL(strSQL);
    }

    public void updateTaxInInvoice(String type, String s, long invoice_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE invoice SET tax_type='" + type + "',tax_rate=" + s + " WHERE id = " + invoice_key;
        db.execSQL(strSQL);
    }

    public void updateAttachment(Bitmap logo_bitmap, long invoice_id) {


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //  logo_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        String bytes = encodeTobase64(logo_bitmap);

        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE invoice SET photo='" + bytes + "' WHERE id = " + invoice_id;
        db.execSQL(strSQL);

       /* String strFilter = "_id=" + 5;
        ContentValues args = new ContentValues();
        args.put("photo", logo_bitmap);
        long id=db.update("invoice", args, strFilter, null);*/
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public void updateinvoiceCalculation(String s, String s1, String s2,String attach_path,String sign_path,String sign_date, long invoice_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE invoice SET subtotal='" + s + "',total_value='" + s1 + "',comment='" + s2 + "',photo='"+attach_path+"',signature='"+sign_path+"',signature_date='"+sign_date+"' WHERE id = " + invoice_key;
        db.execSQL(strSQL);
    }

    public void updateInvoiceInfo(String invoice_name, String invoice_date, String due_date, long invoice_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE invoice SET invoice_name='" + invoice_name + "',invoice_date='" + invoice_date + "',due_date='" + due_date + "' WHERE id = " + invoice_key;
        db.execSQL(strSQL);
    }
    public void updateInvoice(String column_name ,String column_value,String column2,String column2_value, long invoice_key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE invoice SET '"+column_name+"'='"+ column_value +"','"+column2+"'='"+column2_value+"' WHERE id = " + invoice_key;
        db.execSQL(strSQL);
    }
    public boolean isClientExist(long invoice_key)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String client_key="";
        Cursor res =  db.rawQuery( "select * from invoice where id="+invoice_key+"", null );
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            client_key=res.getString(res.getColumnIndex("client_key"));
            res.moveToNext();
        }
        if(client_key !=null)
            return true;
        else
            return false;
    }
    public Client getClientInformation(long invoice_key)
    {
        //  SQLiteDatabase db = this.getReadableDatabase();
        String client_key="";
        ///  Cursor rs =  db.rawQuery( "select * from clients where invoice_key="+invoice_key+"", null );
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from AddNewClient where invoice_key='"+invoice_key+"'", null );
        // Cursor res = db.rawQuery("select * from clients", null);
        cursor.moveToFirst();
        Client clientInfo = null;
        while (cursor.isAfterLast() == false) {
               int clientID=cursor.getInt(cursor.getColumnIndex(CLIENT_ID));
                String clientName=cursor.getString(cursor.getColumnIndex(CLIENT_Name));
                String clientPhone=cursor.getString(cursor.getColumnIndex(CLIENT_PHONE));
                String clientCity=cursor.getString(cursor.getColumnIndex(CLIENT_CITY));
                String clientEmail=cursor.getString(cursor.getColumnIndex(CLIENT_EMAIL));
                String clientAddress=cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS));
                String clientCountry=cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS));
                String clientZipCode=cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS));
                String clientState=cursor.getString(cursor.getColumnIndex(CLIENT_ADDRESS));

            clientInfo=new Client(clientID,clientName,clientPhone,clientEmail,clientAddress,clientCity,clientState,clientCountry,clientZipCode);



            cursor.moveToNext();
        }
        return clientInfo;
    }

    public String getAttachmentPath(long invoice_id) {
        String path="";
        ///  Cursor rs =  db.rawQuery( "select * from clients where invoice_key="+invoice_key+"", null );
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs =  db.rawQuery( "select * from invoice where id='"+invoice_id+"'", null );
        // Cursor res = db.rawQuery("select * from clients", null);
        rs.moveToFirst();
        while (rs.isAfterLast() == false) {
            // client_key=String.valueOf(res.getInt(res.getColumnIndex("id")));
            path = rs.getString(rs.getColumnIndex("photo"));




            rs.moveToNext();
        }
        return path;
    }

    //code after  client change
    public long AddNewClient(ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_NAME, null, contentValues) ;

    }
    public long UpdateClient(ContentValues contentValues,long invoice_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_NAME,  contentValues,"id='"+invoice_id+"'",null) ;

    }
            /*Remove Client Info*/

    public boolean DeleteSingleClient(String Id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, CLIENT_ID+"='" + Id + "'", null) > 0;
    }

                /*Remove Client Table*/

    public boolean ClearClientTable(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }

    /*Fetch ALl Client Table DB*/

    public List<Client> AllClientDataList(){
        List<Client>AllClients=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
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
                    String clientCountry=cursor.getString(cursor.getColumnIndex(CLIENT_COUNTRY));
                    String clientZipCode=cursor.getString(cursor.getColumnIndex(CLIENT_ZIPCODE));
                    String clientState=cursor.getString(cursor.getColumnIndex(CLIENT_STATE));

                    //  AllClients.add(0,new ClientInfo(clientID,clientName,clientPhone,clientCity,clientEmail,clientAddress));
                    AllClients.add(0,new Client(clientID,clientName,clientPhone,clientEmail,clientAddress,clientCity,clientState,clientCountry,clientZipCode));
                    cursor.moveToNext();
                }
            }
        }

        return AllClients;
    }
    /*Fetch  Client Table Size DB*/

    public int getClientTableSize(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        int tbSize=0;
        @SuppressLint("Recycle") Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        if(cursor!=null) {
            if (cursor.getCount() > 0) {

                tbSize=cursor.getCount();
            }
        }
        return tbSize;
    }

    public String getClientNameOfInvoice(String id) {
        String client_name="";
        ///  Cursor rs =  db.rawQuery( "select * from clients where invoice_key="+invoice_key+"", null );
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs =  db.rawQuery( "select * from AddNewClient where invoice_key='"+id+"'", null );
        // Cursor res = db.rawQuery("select * from clients", null);
        rs.moveToFirst();
        while (rs.isAfterLast() == false) {
            // client_key=String.valueOf(res.getInt(res.getColumnIndex("id")));
            client_name = rs.getString(rs.getColumnIndex("ClientName"));




            rs.moveToNext();
        }
        return client_name;
    }

    //close code
}
