package com.example.bakingapp.ui.detail.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.ItemStepBinding;
import com.example.bakingapp.model.Steps;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    ItemStepBinding itemStepBinding;
    List<Steps> steps;
    OnStepClickListener onStepClickListener;

    public interface OnStepClickListener {
        void onStepClick(Steps steps);
    }

    public StepsAdapter(List<Steps> steps, OnStepClickListener onStepClickListener) {
        this.steps = steps;
        this.onStepClickListener = onStepClickListener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemStepBinding = ItemStepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StepsViewHolder(itemStepBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        Steps currentSteps = steps.get(position);
        holder.bind(currentSteps, onStepClickListener);
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    public void setStepsData(List<Steps> stepsData) {
        steps = stepsData;
        notifyDataSetChanged();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder{

        ItemStepBinding itemStepBinding;

        public StepsViewHolder(ItemStepBinding itemStepBinding) {
            super(itemStepBinding.getRoot());
            this.itemStepBinding = itemStepBinding;
        }

        public void bind(Steps steps, OnStepClickListener onStepClickListener) {
            itemStepBinding.stepShortDescription.setText(steps.getShortDescription());
            itemStepBinding.setStep(steps);
            itemStepBinding.setStepItemClick(onStepClickListener);
            itemStepBinding.executePendingBindings();
        }
    }
}
