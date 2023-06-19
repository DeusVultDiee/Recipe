package com.example.recipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.EditActivity;
import com.example.recipe.R;
import com.example.recipe.db.MyConstans;
import com.example.recipe.db.MyDBManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>
{
    private Context context;
    private List<ListItem> mainArray;

    public MainAdapter(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_list_layout, parent, false);
        return new MyViewHolder(view, context, mainArray);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position)
    {
        holder.setData(mainArray.get(position).getTitle(), mainArray.get(position).getProducts());
    }

    @Override
    public int getItemCount()
    {
        return mainArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Context context;
        private TextView tvTitle, tvProducts;
        private List<ListItem> mainArray;
        public MyViewHolder(@NonNull @NotNull View itemView, Context context, List<ListItem> mainArray)
        {
            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvProducts = itemView.findViewById(R.id.tvProducts);
            itemView.setOnClickListener(this);
        }

        public void setData(String title, String products)
        {
            tvTitle.setText(title);
            tvProducts.setText(products);
        }

        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(context, EditActivity.class);
            i.putExtra(MyConstans.LIST_ITEM_INTENT, mainArray.get(getAdapterPosition()));
            i.putExtra(MyConstans.EDIT_STATE, false);
            context.startActivity(i);
        }
    }

    public void updateAdapter(List<ListItem> newList)
    {
        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeItem(int pos, MyDBManager dbManager)
    {
        dbManager.delete(mainArray.get(pos).getId());
        mainArray.remove(pos);
        notifyItemRangeChanged(0, mainArray.size());
        notifyItemRemoved(pos);
    }

}
