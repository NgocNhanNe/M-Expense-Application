package food.app.demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import food.app.demo.Model.Expense;
import food.app.demo.Model.Trip;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database name
    private static final String DATABASE_NAME = "M_Expense_app";
    private static final int DATABASE_VERSION = 1;
    //database table
    private static final String TABLE_TRIP_NAME = "trip";
    private static final String TABLE_EXPENSE_NAME = "expense";
//    private static final String TABLE_TYPE_NAME = "type";

    //column trip table
    private static final String TRIP_ID_COLUMN = "trip_id";
    private static final String TRIP_NAME_COLUMN = "trip_name";
    private static final String TRIP_DEPARTURE_COLUMN = "trip_departure";
    private static final String TRIP_DESTINATION_COLUMN = "trip_destination";
    private static final String TRIP_START_DATE_COLUMN = "trip_startDate";
    private static final String TRIP_END_DATE_COLUMN = "trip_endDate";
    private static final String TRIP_RISK_COLUMN = "trip_risk";
    private static final String TRIP_TYPE_COLUMN = "trip_type";
    private static final String TRIP_DESCRIPTION_COLUMN = "trip_description";

    //column expense table
    private static final String EXPENSE_ID_COLUMN = "expense_id";
    private static final String EXPENSE_NAME_COLUMN = "expense_name";
    private static final String EXPENSE_AMOUNT_COLUMN = "expense_amount";
    private static final String EXPENSE_NOTES_COLUMN = "expense_notes";
    private static final String EXPENSE_DATE_COLUMN = "expense_date";
    private static final String EXPENSE_TIME_COLUMN = "expense_time";
    private static final String EXPENSE_TYPE_COLUMN = "expense_type";
    private static final String EXPENSE_LOCATION_COLUMN = "expense_location";

//    //column type
//    private static final String TYPE_ID_COLUMN = "type_id";
//    private static final String TYPE_COLUMN = "type";

    public SQLiteDatabase database;

    private static final String DATABASE_CREATE_TRIP = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s DATE, " +
                    "   %s DATE, " +
                    "   %s INTEGER, " +
                    "   %s INTEGER, " +
                    "   %s TEXT)",
            TABLE_TRIP_NAME,TRIP_ID_COLUMN, TRIP_NAME_COLUMN, TRIP_DEPARTURE_COLUMN,TRIP_DESTINATION_COLUMN, TRIP_START_DATE_COLUMN,TRIP_END_DATE_COLUMN,TRIP_RISK_COLUMN,TRIP_TYPE_COLUMN,TRIP_DESCRIPTION_COLUMN);
//
//    private static final String DATABASE_CREATE_TYPE = String.format(
//            "CREATE TABLE %s (" +
//                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    "   %s TEXT)",
//            TABLE_TYPE_NAME, TYPE_ID_COLUMN,TYPE_COLUMN);


    private static final String DATABASE_CREATE_EXPENSE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s FLOAT, " +
                    "   %s TEXT, " +
                    "   %s DATE, " +
                    "   %s TIME, " +
                    "   %s TEXT, " +
                    "   %s INTEGER references %s (%s))",
            TABLE_EXPENSE_NAME,EXPENSE_ID_COLUMN, EXPENSE_TYPE_COLUMN,EXPENSE_NAME_COLUMN, EXPENSE_AMOUNT_COLUMN,EXPENSE_NOTES_COLUMN, EXPENSE_DATE_COLUMN,EXPENSE_TIME_COLUMN,EXPENSE_LOCATION_COLUMN,TRIP_ID_COLUMN, TABLE_TRIP_NAME,TRIP_ID_COLUMN);

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TRIP);
        db.execSQL(DATABASE_CREATE_EXPENSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        Log.v(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " +
                newVersion + " - old data lost");
        onCreate(db);
    }

    //trip
    public long addTrip(Trip trip){
        long insertId;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRIP_NAME_COLUMN, trip.getTrip_name());
        values.put(TRIP_TYPE_COLUMN, trip.getType());
        values.put(TRIP_DEPARTURE_COLUMN, trip.getTrip_departure());
        values.put(TRIP_DESTINATION_COLUMN, trip.getTrip_destination());
        values.put(TRIP_START_DATE_COLUMN, trip.getTrip_startDate());
        values.put(TRIP_END_DATE_COLUMN, trip.getTrip_endDate());
        values.put(TRIP_RISK_COLUMN, trip.getTrip_risk());
        values.put(TRIP_DESCRIPTION_COLUMN, trip.getTrip_description());


        // Inserting Row
        insertId = db.insert(TABLE_TRIP_NAME, null, values);
        db.close(); // Closing database connection
        return insertId;
    }

    public int updateTrip(Trip trip,int id) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TRIP_NAME_COLUMN, trip.getTrip_name());
        values.put(TRIP_TYPE_COLUMN, trip.getType());
        values.put(TRIP_DEPARTURE_COLUMN, trip.getTrip_departure());
        values.put(TRIP_DESTINATION_COLUMN, trip.getTrip_destination());
        values.put(TRIP_START_DATE_COLUMN, trip.getTrip_startDate());
        values.put(TRIP_END_DATE_COLUMN, trip.getTrip_endDate());
        values.put(TRIP_RISK_COLUMN, trip.getTrip_risk());
        values.put(TRIP_DESCRIPTION_COLUMN, trip.getTrip_description());

        // updating row
        return database.update(TABLE_TRIP_NAME,
                values,
                TRIP_ID_COLUMN + " = ?",
                new String[] { String.valueOf(id) });
    }


    public void deleteTripById(long id) {
        List<Expense> expenses = new ArrayList<>();
        expenses= getListExpensesByTripId((int) id);
        for (Expense ex:expenses) {
            deleteExpensesById(ex.getExpense_id());
        }
        database = this.getWritableDatabase();
        database.delete(TABLE_TRIP_NAME, TRIP_ID_COLUMN + " = ?",
                new String[] { String.valueOf(id) });
        database.close();
    }

    public void deleteAllTrip(){
        SQLiteDatabase db = this.getWritableDatabase();
        dropAndRecreateTrip(db);
    }

    private void dropAndRecreateTrip(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_NAME);
        dbCreate(db);
    }

    public void dbCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TRIP);
        db.execSQL(DATABASE_CREATE_EXPENSE);
    }

    public List<Trip> getAllTrip() {
        final String query = "SELECT * FROM " + TABLE_TRIP_NAME + " order by " + TRIP_ID_COLUMN + " desc";
        SQLiteDatabase db = this.getReadableDatabase();
        final List<Trip> tripList = new ArrayList<>();
        final Cursor cursor;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Float totalExpenses=getExpensesByTripId(cursor.getInt(0));
                    Trip trip = new Trip(cursor.getInt(0),cursor.getString(1),cursor.getInt(7),cursor.getString(3),cursor.getString(2),
                            cursor.getString(4),cursor.getString(5),cursor.getInt(6),cursor.getString(8),totalExpenses);
                    // Adding object to list
                    System.out.println(trip.toString());
                    tripList.add(trip);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return tripList;
    }
    public Float getExpensesByTripId(int id){
        Float expenses=0f;
        Cursor results = database.query(TABLE_EXPENSE_NAME, new String[] {EXPENSE_ID_COLUMN, EXPENSE_TYPE_COLUMN,EXPENSE_NAME_COLUMN, EXPENSE_AMOUNT_COLUMN,EXPENSE_NOTES_COLUMN, EXPENSE_DATE_COLUMN,EXPENSE_TIME_COLUMN,EXPENSE_LOCATION_COLUMN,TRIP_ID_COLUMN}, TRIP_ID_COLUMN + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        results.moveToFirst();
        while (!results.isAfterLast()) {
            Expense expense = new Expense(results.getInt(0),results.getString(1),results.getFloat(3),results.getString(2),results.getString(4),results.getString(5),results.getString(6),results.getString(7),results.getInt(8));
            expenses+=expense.getExpense_amount();
            results.moveToNext();
        }
        if(expenses==null){
            return null;
        }else return expenses;
    }

    public ArrayList<Expense> getListExpensesByTripId(int Id){
        ArrayList<Expense> expenses = new ArrayList<>();
        Cursor results = database.query(true, TABLE_EXPENSE_NAME, new String[] {EXPENSE_ID_COLUMN, EXPENSE_TYPE_COLUMN, EXPENSE_NAME_COLUMN,EXPENSE_AMOUNT_COLUMN,EXPENSE_NOTES_COLUMN, EXPENSE_DATE_COLUMN,EXPENSE_TIME_COLUMN,EXPENSE_LOCATION_COLUMN,TRIP_ID_COLUMN }, TRIP_ID_COLUMN+ " = ?",
                new String[] {String.valueOf(Id) }, null, null, null,
                null);

        results.moveToFirst();
        while (!results.isAfterLast()) {
            Expense expense=new Expense(results.getInt(0),results.getString(1),results.getFloat(3),results.getString(2),results.getString(4),results.getString(5),results.getString(6),results.getString(7),results.getInt(8));
            expenses.add(expense);
            results.moveToNext();
        }
        if(expenses==null){;
            return null;
        }else return expenses;
    }

    //expense

    public long addExpense(Expense expenses) {
        long insertId;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues rowValues = new ContentValues();
        rowValues.put(EXPENSE_TYPE_COLUMN, expenses.getExpense_type());
        rowValues.put(EXPENSE_NAME_COLUMN,expenses.getExpense_name());
        rowValues.put(EXPENSE_DATE_COLUMN,expenses.getExpense_date() );
        rowValues.put(EXPENSE_TIME_COLUMN, expenses.getExpense_time());
        rowValues.put(EXPENSE_AMOUNT_COLUMN,expenses.getExpense_amount());
        rowValues.put(EXPENSE_LOCATION_COLUMN,expenses.getLocation());
        rowValues.put(EXPENSE_NOTES_COLUMN,expenses.getExpense_notes());
        rowValues.put(TRIP_ID_COLUMN,expenses.getTrip_id());


        // Inserting Row
        insertId = db.insert(TABLE_EXPENSE_NAME, null, rowValues);
        db.close(); // Closing database connection
        return insertId;
    }

    public int updateExpenses(Expense expenses,int id) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EXPENSE_TYPE_COLUMN, expenses.getExpense_type());
        values.put(EXPENSE_AMOUNT_COLUMN, expenses.getExpense_amount());
        values.put(EXPENSE_DATE_COLUMN, expenses.getExpense_date());
        values.put(EXPENSE_TIME_COLUMN, expenses.getExpense_time());
        values.put(EXPENSE_NOTES_COLUMN, expenses.getExpense_notes());
        values.put(EXPENSE_LOCATION_COLUMN, expenses.getLocation());
        values.put(EXPENSE_NAME_COLUMN,expenses.getExpense_name());

        // updating row
        return database.update(TABLE_EXPENSE_NAME,
                values,
                EXPENSE_ID_COLUMN + " = ?",
                new String[] { String.valueOf(id) });
    }

    public List<Expense> getListExpense() {
        ArrayList<Expense> expenses = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSE_NAME + " order by " + EXPENSE_ID_COLUMN + " desc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery(selectQuery, null);
        results.moveToFirst();
        while (!results.isAfterLast()) {
            Expense expense=new Expense(results.getInt(0),results.getString(1),results.getFloat(3),results.getString(2),results.getString(4),results.getString(5),results.getString(6),results.getString(7),results.getInt(8));
            System.out.println(expense.toString());
            expenses.add(expense);
            results.moveToNext();
        }
        if(expenses==null){
            return null;
        }else return expenses;

    }
    public Float getTotalExpensesInTrip(String id){
        Float total=0f;
        String selectQuery = "SELECT " + EXPENSE_AMOUNT_COLUMN+" FROM " + TABLE_EXPENSE_NAME + " WHERE " + TRIP_ID_COLUMN + " = " +id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor results = db.rawQuery(selectQuery, null);
        results.moveToFirst();
        while (!results.isAfterLast()) {

            total+=results.getFloat(0);
            results.moveToNext();
        }
        if(total==null){
            return null;
        }else return total;
    }

    public Float getTotalExpenses(){
        Float total=0f;
        String selectQuery = "SELECT  "+EXPENSE_AMOUNT_COLUMN+" FROM " + TABLE_EXPENSE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery(selectQuery, null);
        results.moveToFirst();
        while (!results.isAfterLast()) {

            total+=results.getFloat(0);
            results.moveToNext();
        }
        if(total==null){
            return null;
        }else return total;
    }

    public ArrayList<Float> getTotalTypeExpenses(){
        Float sum=0f;
        Float sum1=0f;
        Float sum2=0f;
        Float sum3=0f;
        Float sum4=0f;

        ArrayList<Float> totalType = new ArrayList<>();

        String selectQuery = "SELECT " + EXPENSE_AMOUNT_COLUMN + "," + EXPENSE_TYPE_COLUMN +" FROM " + TABLE_EXPENSE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String category = cursor.getString(1);

            if(category.equals("Food"))
            {
                sum+=cursor.getFloat(0);
                cursor.moveToNext();
            }
            if(category.equals("Documents"))
            {
                sum1+=cursor.getFloat(0);
                cursor.moveToNext();
            }
            else if(category.equals("Transports"))
            {
                sum2+=cursor.getFloat(0);
                cursor.moveToNext();
            }
            else if(category.equals("Hotel"))
            {
                sum3+=cursor.getFloat(0);
                cursor.moveToNext();
            }
            else if(category.equals("Others"))
            {
                sum4+=cursor.getFloat(0);
                cursor.moveToNext();
            }
        }
        totalType.add(sum);
        totalType.add(sum1);
        totalType.add(sum2);
        totalType.add(sum3);
        totalType.add(sum4);
        System.out.println(totalType.toString());
        return totalType;
    }


    public void deleteExpensesById(long id) {
        database = this.getWritableDatabase();
        database.delete(TABLE_EXPENSE_NAME, EXPENSE_ID_COLUMN + " = ?",
                new String[] { String.valueOf(id) });
        database.close();
    }


    public ArrayList<String> DownloadFile(int id) {
        final String query = "SELECT  * FROM " + TABLE_EXPENSE_NAME + " WHERE " + TRIP_ID_COLUMN + " = " +id + " order by " + EXPENSE_ID_COLUMN + " desc";
        SQLiteDatabase db = this.getReadableDatabase();
        final ArrayList<String> list = new ArrayList<>();
        final Cursor cursor;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            StringBuilder JsonFormat = new StringBuilder();
            if (cursor.moveToFirst()) {
                JsonFormat.append("\n{\n\t\"").append(cursor.getString(0)).append("\":[");
                do {
                    String type = cursor.getString(1);
                    String name = cursor.getString(2);
                    String amount = String.valueOf(cursor.getFloat(3));
                    String location = cursor.getString(7);
                    String date = cursor.getString(5);
                    String time = cursor.getString(6);
                    String notes = cursor.getString(4);
                    if (!cursor.isLast()) {
                        JsonFormat.append("\n\t\t{\n\t\t\t\"expense name\":" + "\"").append(name).append("\",\n");
                        JsonFormat.append("\n\t\t{\n\t\t\t\"type\":" + "\"").append(type).append("\",\n");
                        JsonFormat.append("\t\t\t\"amount\":" + "\"").append(amount).append("\",\n");
                        JsonFormat.append("\t\t\t\"date\":" + "\"").append(date).append("\",\n");
                        JsonFormat.append("\t\t\t\"time\":" + "\"").append(time).append("\",\n");
                        JsonFormat.append("\t\t\t\"location\":" + "\"").append(location).append("\",\n");
                        JsonFormat.append("\t\t\t\"comments\":" + "\"").append(notes).append("\"\n");
                        JsonFormat.append("\t\t},");
                    } else {
                        JsonFormat.append("\n\t\t{\n\t\t\t\"expense name\":" + "\"").append(name).append("\",\n");
                        JsonFormat.append("\n\t\t{\n\t\t\t\"type\":" + "\"").append(type).append("\",\n");
                        JsonFormat.append("\t\t\t\"amount\":" + "\"").append(amount).append("\",\n");
                        JsonFormat.append("\t\t\t\"date\":" + "\"").append(date).append("\",\n");
                        JsonFormat.append("\t\t\t\"time\":" + "\"").append(time).append("\",\n");
                        JsonFormat.append("\t\t\t\"location\":" + "\"").append(location).append("\",\n");
                        JsonFormat.append("\t\t\t\"comments\":" + "\"").append(notes).append("\"\n");
                        JsonFormat.append("\t\t},");
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            JsonFormat.append("\n\t]\n}");
            list.add(JsonFormat.toString());
            db.close();
        }
        return list;
    }

}
