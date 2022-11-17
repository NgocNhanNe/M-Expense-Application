package food.app.demo.Model;

import java.io.Serializable;

public class Expense implements Serializable {

    private Integer expense_id;
    private String expense_type;
    private Float expense_amount;
    private String expense_name;
    private String expense_notes;
    private String expense_date;
    private String expense_time;
    private String location;
    private Integer trip_id;


    @Override
    public String toString() {
        return "Expense{" +
                "expense_id=" + expense_id +
                ", expense_type='" + expense_type + '\'' +
                ", expense_amount=" + expense_amount +
                ", expense_name='" + expense_name + '\'' +
                ", expense_notes='" + expense_notes + '\'' +
                ", expense_date='" + expense_date + '\'' +
                ", expense_time='" + expense_time + '\'' +
                ", location='" + location + '\'' +
                ", trip_id=" + trip_id +
                '}';
    }

    public Expense(Integer expense_id, String expense_type, Float expense_amount, String expense_name, String expense_notes, String expense_date, String expense_time, String location, Integer trip_id) {
        this.expense_id = expense_id;
        this.expense_type = expense_type;
        this.expense_amount = expense_amount;
        this.expense_name = expense_name;
        this.expense_notes = expense_notes;
        this.expense_date = expense_date;
        this.expense_time = expense_time;
        this.location = location;
        this.trip_id = trip_id;
    }

    public Expense(){

    }

    public Integer getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(Integer expense_id) {
        this.expense_id = expense_id;
    }

    public String getExpense_type() {
        return expense_type;
    }

    public void setExpense_type(String expense_type) {
        this.expense_type = expense_type;
    }

    public Float getExpense_amount() {
        return expense_amount;
    }

    public void setExpense_amount(Float expense_amount) {
        this.expense_amount = expense_amount;
    }

    public String getExpense_name() {
        return expense_name;
    }

    public void setExpense_name(String expense_name) {
        this.expense_name = expense_name;
    }

    public String getExpense_notes() {
        return expense_notes;
    }

    public void setExpense_notes(String expense_notes) {
        this.expense_notes = expense_notes;
    }

    public String getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(String expense_date) {
        this.expense_date = expense_date;
    }

    public String getExpense_time() {
        return expense_time;
    }

    public void setExpense_time(String expense_time) {
        this.expense_time = expense_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }
}

