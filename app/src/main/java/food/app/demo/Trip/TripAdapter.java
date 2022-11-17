package food.app.demo.Trip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import food.app.demo.db.DatabaseHelper;
import food.app.demo.Model.Trip;
import food.app.demo.R;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    public List<Trip> getAllTrip() {
        return tripList;
    }
    List<Trip> tripList;
    List<Trip> oldTripList;
    Context context;
    private IClickItemListener iClickItemListener;

    public void setAllTrip(List<Trip> listTrips) {
        tripList = listTrips;
        notifyDataSetChanged();
    }

    public interface IClickItemListener{
        void onLickItemTrip(Trip trip);
    }


    public TripAdapter(List<Trip> tripList, Context context, IClickItemListener iClickItemListener) {
        this.tripList = tripList;
        this.oldTripList = tripList;
        this.context = context;
        this.iClickItemListener = iClickItemListener;
    }

    @NonNull
    @Override
    public TripAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_row,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DatabaseHelper db = new DatabaseHelper(context);
        Trip trip = tripList.get(position);
        int trip_id = trip.getTrip_id();
        holder.trip_id.setText(String.valueOf(trip_id));
        holder.trip_name.setText(trip.getTrip_name());
        holder.trip_departure.setText(trip.getTrip_departure());
        holder.trip_destination.setText(trip.getTrip_destination());
        holder.trip_startDate.setText(trip.getTrip_startDate());
        holder.trip_endDate.setText(trip.getTrip_endDate());
        holder.trip_price.setText(String.valueOf(db.getTotalExpensesInTrip(String.valueOf(trip_id))));
        if(trip.getType() == 0){
            holder.trip_img.setImageResource(R.drawable.international);
        }
        else if(trip.getType() == 1){
            holder.trip_img.setImageResource(R.drawable.domestic);
        }
        holder.mainTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onLickItemTrip(trip);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView trip_name,trip_departure, trip_destination, trip_startDate, trip_endDate,trip_price, trip_id;
        ImageView trip_img;
        LinearLayout mainTrip;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            trip_name = itemView.findViewById(R.id.trip_name);
            trip_departure = itemView.findViewById(R.id.departure_txt);
            trip_destination = itemView.findViewById(R.id.destination_txt);
            trip_startDate = itemView.findViewById(R.id.tripStart_date);
            trip_endDate = itemView.findViewById(R.id.tripEnd_date);
            trip_price=itemView.findViewById(R.id.trip_price);
            trip_img = itemView.findViewById(R.id.trip_img);
            trip_id = itemView.findViewById(R.id.trip_id);
            mainTrip = itemView.findViewById(R.id.mainTrip);

        }
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()) {
                    tripList = oldTripList;
                } else {
                    List<Trip> list = new ArrayList<>();
                    for (Trip trip : oldTripList) {
                        if (trip.getTrip_name().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(trip);
                        }
                        else if (trip.getTrip_startDate().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(trip);
                        }
                        else if (trip.getTrip_endDate().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(trip);
                        }
                        else if (trip.getTrip_destination().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(trip);
                        }
                        else if (trip.getTrip_departure().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(trip);
                        }
                    }
                    tripList = list;
                }
                FilterResults result = new FilterResults();
                result.values = tripList;
                return result;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tripList = (List<Trip>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
