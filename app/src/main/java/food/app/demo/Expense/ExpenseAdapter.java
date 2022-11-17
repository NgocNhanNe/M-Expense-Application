package food.app.demo.Expense;

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

import food.app.demo.Model.Expense;
import food.app.demo.R;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder>{

    List<Expense> expenseList;
    List<Expense> oldExList;
    public List<Expense> getListExpense() {
        return expenseList;
    }
    Context context;
    private IClickItemListener iClickItemListener;

    public void setAllTrip(List<Expense> expenseLists) {
        expenseList = expenseLists;
        notifyDataSetChanged();
    }

    public interface IClickItemListener{
        void onLickItemExpense(Expense expense);
    }

    public ExpenseAdapter(List<Expense> expenseList, Context context, IClickItemListener iClickItemListener) {
        this.expenseList = expenseList;
        this.oldExList = expenseList;
        this.context = context;
        this.iClickItemListener = iClickItemListener;
    }

    @NonNull
    @Override
    public ExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpenseAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Expense expense = expenseList.get(position);
        String expense_id = String.valueOf(expense.getExpense_id());

        holder.ex_id.setText(expense_id);
        holder.ex_name.setText(expense.getExpense_name());
        holder.ex_amount.setText(String.valueOf(expense.getExpense_amount()));

        if(expense.getExpense_type().equals("Food")){
            holder.ex_img.setImageResource(R.drawable.food);
        }
        else if (expense.getExpense_type().equals("Hotel")){
            holder.ex_img.setImageResource(R.drawable.hotel);
        }
        else if (expense.getExpense_type().equals("Documents")){
            holder.ex_img.setImageResource(R.drawable.doc);
        }
        else if (expense.getExpense_type().equals("Transports")){
            holder.ex_img.setImageResource(R.drawable.transportation);
        }
        else if (expense.getExpense_type().equals("Others")){
            holder.ex_img.setImageResource(R.drawable.other);
        }

        holder.mainExpense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                iClickItemListener.onLickItemExpense(expense);
            }
        });


    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout mainExpense;
        TextView ex_name, ex_amount,ex_id, trip_name;
        ImageView ex_img;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            trip_name = itemView.findViewById(R.id.trip_name);
            ex_id = itemView.findViewById(R.id.expense_id);
            ex_name = itemView.findViewById(R.id.expense_name);
            ex_amount = itemView.findViewById(R.id.expense_amount);
            ex_img = itemView.findViewById(R.id.type_img);
            mainExpense = itemView.findViewById(R.id.mainExpense);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()) {
                    expenseList = oldExList;
                } else {
                    List<Expense> list = new ArrayList<>();
                    for (Expense expense : oldExList) {
                        if (expense.getExpense_name().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(expense);
                        }
                        else if (expense.getExpense_type().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(expense);
                        }
                        else if (expense.getExpense_date().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(expense);
                        }
                        else if (expense.getExpense_time().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(expense);
                        }
                        else if (expense.getExpense_amount().toString().contains(strSearch.toLowerCase())) {
                            list.add(expense);
                        }
                    }
                    expenseList = list;
                }
                FilterResults result = new FilterResults();
                result.values = expenseList;
                return result;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                expenseList = (List<Expense>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
