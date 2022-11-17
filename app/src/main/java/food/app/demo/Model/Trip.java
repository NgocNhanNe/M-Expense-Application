package food.app.demo.Model;

import java.io.Serializable;

public class Trip implements Serializable {
    private Integer trip_id;
    private String trip_name;
    private Integer trip_type;
    private String trip_departure;
    private String trip_destination;
    private String trip_startDate;
    private String trip_endDate;
    private Integer trip_risk;
    private String trip_description;
    private Float expenses;

    @Override
    public String toString() {
        return "Trip{" +
                "trip_id=" + trip_id +
                ", trip_name='" + trip_name + '\'' +
                ", trip_type=" + trip_type +
                ", trip_departure='" + trip_departure + '\'' +
                ", trip_destination='" + trip_destination + '\'' +
                ", trip_startDate='" + trip_startDate + '\'' +
                ", trip_endDate='" + trip_endDate + '\'' +
                ", trip_risk=" + trip_risk +
                ", trip_description='" + trip_description + '\'' +
                ", expenses=" + expenses +
                '}';
    }

    public Trip(Integer trip_id, String trip_name, Integer trip_type, String trip_departure, String trip_destination, String trip_startDate, String trip_endDate, Integer trip_risk, String trip_description) {
        this.trip_id = trip_id;
        this.trip_name = trip_name;
        this.trip_type = trip_type;
        this.trip_departure = trip_departure;
        this.trip_destination = trip_destination;
        this.trip_startDate = trip_startDate;
        this.trip_endDate = trip_endDate;
        this.trip_risk = trip_risk;
        this.trip_description = trip_description;
    }

    public Trip(Integer trip_id, String trip_name, Integer type, String trip_destination, String trip_departure, String trip_startDate, String trip_endDate, Integer trip_risk, String trip_description, Float expenses) {
        this.trip_id = trip_id;
        this.trip_name = trip_name;
        this.trip_type = type;
        this.trip_departure = trip_departure;
        this.trip_destination = trip_destination;
        this.trip_startDate = trip_startDate;
        this.trip_endDate = trip_endDate;
        this.trip_risk = trip_risk;
        this.trip_description = trip_description;
        this.expenses = expenses;
    }

    public Trip(){

    }

    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public Integer getType() {
        return trip_type;
    }

    public void setType(Integer type) {
        this.trip_type = type;
    }


    public String getTrip_departure() {
        return trip_departure;
    }

    public void setTrip_departure(String trip_departure) {
        this.trip_departure = trip_departure;
    }

    public String getTrip_destination() {
        return trip_destination;
    }

    public void setTrip_destination(String trip_destination) {
        this.trip_destination = trip_destination;
    }

    public String getTrip_startDate() {
        return trip_startDate;
    }

    public void setTrip_startDate(String trip_startDate) {
        this.trip_startDate = trip_startDate;
    }

    public String getTrip_endDate() {
        return trip_endDate;
    }

    public void setTrip_endDate(String trip_endDate) {
        this.trip_endDate = trip_endDate;
    }

    public Integer getTrip_risk() {
        return trip_risk;
    }

    public void setTrip_risk(Integer trip_risk) {
        this.trip_risk = trip_risk;
    }

    public String getTrip_description() {
        return trip_description;
    }

    public void setTrip_description(String trip_description) {
        this.trip_description = trip_description;
    }

    public Float getExpenses() {
        return expenses;
    }

    public void setExpenses(Float expenses) {
        this.expenses = expenses;
    }

}
