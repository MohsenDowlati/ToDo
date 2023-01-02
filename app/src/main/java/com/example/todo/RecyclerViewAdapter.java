package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Tasks> tasks = new ArrayList<>();
    private DataBaseManager db = null;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.parent.setOnClickListener(v -> {
            try {
                db = new DataBaseManager(context);
                tasks.get(holder.getAdapterPosition()).setCompleteness(! tasks.get(holder.getAdapterPosition()).isCompleteness());
                db.update(tasks.get(holder.getAdapterPosition()));
                notifyItemChanged(holder.getAdapterPosition());
            } catch (NullPointerException e) {
                System.out.println(e);
            }

        });

        if (tasks.get(position).getTitle()!=null && tasks.get(position).getDescription()!=null) {
            holder.title.setText(tasks.get(position).getTitle());
            holder.descTxt.setText(tasks.get(position).getDescription());
            String completeText = "";
            if (tasks.get(position).isCompleteness()) {
                completeText = "Task has been done.";
            } else {
                completeText = "Task hasn't completed yet.";
            }
            holder.completeness.setText(completeText);
        }
    }

    private boolean removeItem(int position) {
        db = new DataBaseManager(context);
        if (db.deleteOne(tasks.get(position))) {
            tasks.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
    }

    public void setTasks (ArrayList<Tasks> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title , descTxt , completeness;
        CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxtView);
            descTxt = itemView.findViewById(R.id.descTxtView);
            completeness = itemView.findViewById(R.id.complete);
            parent = itemView.findViewById(R.id.parentCardView);
        }
    }
}
