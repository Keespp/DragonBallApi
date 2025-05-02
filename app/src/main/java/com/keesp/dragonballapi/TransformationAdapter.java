package com.keesp.dragonballapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TransformationAdapter extends RecyclerView.Adapter<TransformationAdapter.ViewHolder> {

    private List<Transformation> transformations;
    private Context context;

    public TransformationAdapter(Context context, List<Transformation> transformations) {
        this.context = context;
        this.transformations = transformations;
    }

    @Override
    public TransformationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transformation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransformationAdapter.ViewHolder holder, int position) {
        Transformation transformation = transformations.get(position);
        holder.nameText.setText(transformation.getName());
        holder.kiText.setText(transformation.getKi());
        Glide.with(context)
                .load(transformation.getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return transformations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView kiText;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.transformation_name);
            imageView = itemView.findViewById(R.id.transformation_image);
            kiText = itemView.findViewById(R.id.transformation_ki);
        }
    }
}
