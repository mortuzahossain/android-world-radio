package com.wordpress.mortuza99.worldradio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wordpress.mortuza99.worldradio.R;
import com.wordpress.mortuza99.worldradio.model.RadioChenels;

import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    Context context;
    List<RadioChenels> myDataModels;
    ItemClickListener itemClickListener;


    public MyRecyclerAdapter(Context context, List<RadioChenels> myDataModel) {
        this.context = context;
        this.myDataModels = myDataModel;
    }

    @Override
    public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.MyViewHolder holder, int position) {
        Picasso.get()
                .load(myDataModels.get(position).getImage())
                .into(holder.logo);
        holder.name.setText(myDataModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return myDataModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            logo = itemView.findViewById(R.id.logo);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(view,getAdapterPosition());
            }
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(View v, int position);
    }

}

