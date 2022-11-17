package food.app.demo.Trip;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import food.app.demo.Model.Expense;
import food.app.demo.R;

public class DetailTripContainExpenseAdapter extends RecyclerView.Adapter<DetailTripContainExpenseAdapter.MyViewHolder> {

    public List<Expense> getExpenseList() {
        return expenseList;
    }
    List<Expense> expenseList;
    private DetailTripContainExpenseAdapter.IClickItemListener iClickItemListener;

    public void setExpenseList(List<Expense> expenseLists) {
        expenseList = expenseLists;
        notifyDataSetChanged();
    }

    public DetailTripContainExpenseAdapter(List<Expense> expenseList, IClickItemListener iClickItemListener) {
        this.expenseList = expenseList;
        this.iClickItemListener = iClickItemListener;
    }

    public interface IClickItemListener{
        void onClickItemExpenseInTrip(Expense expense);
    }
    @NonNull
    @Override
    public DetailTripContainExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailTripContainExpenseAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_trip_row,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull DetailTripContainExpenseAdapter.MyViewHolder holder, int position) {

        Expense expense = expenseList.get(position);
        holder.ex_id.setText(position+1+"");
        holder.ex_name.setText(expense.getExpense_name());
        holder.ex_amount.setText(String.valueOf(expense.getExpense_amount()));
        holder.ex_date.setText(expense.getExpense_date());
        holder.ex_type.setText(expense.getExpense_type());
        holder.ex_note.setText(expense.getExpense_notes());

        if(expense.getExpense_notes().equals("Done")){
            holder.status.setImageResource(R.drawable.green);
            holder.ex_note.setText("Approved!");
        }
        else {
            holder.status.setImageResource(R.drawable.red);
            holder.ex_note.setText("Pending...");
        }

        if(expense.getExpense_type().equals("Food")){
            holder.ex_typeImg.setImageResource(R.drawable.food);
        }
        else if (expense.getExpense_type().equals("Hotel")){
            holder.ex_typeImg.setImageResource(R.drawable.hotel);
        }
        else if (expense.getExpense_type().equals("Documents")){
            holder.ex_typeImg.setImageResource(R.drawable.doc);
        }
        else if (expense.getExpense_type().equals("Transports")){
            holder.ex_typeImg.setImageResource(R.drawable.transportation);
        }
        else if (expense.getExpense_type().equals("Others")){
            holder.ex_typeImg.setImageResource(R.drawable.other);
        }
        holder.ex_trip_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickItemExpenseInTrip(expense);
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ex_trip_layout;
        ImageView ex_typeImg, status;
        TextView ex_type,ex_name,ex_date, ex_amount,ex_id, ex_note;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ex_id = itemView.findViewById(R.id.Expense_id);
            ex_type = itemView.findViewById(R.id.expense_type);
            ex_date = itemView.findViewById(R.id.expense_date);
            ex_name = itemView.findViewById(R.id.expense_name);
            ex_amount = itemView.findViewById(R.id.expense_amount);
            ex_typeImg = itemView.findViewById(R.id.type_img_row);
            status = itemView.findViewById(R.id.status);
            ex_note = itemView.findViewById(R.id.expense_status);
            ex_trip_layout = itemView.findViewById(R.id.expense_trip_linear);

        }
    }

}
