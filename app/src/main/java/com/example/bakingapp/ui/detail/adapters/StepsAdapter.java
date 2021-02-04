package com.example.bakingapp.ui.detail.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.StepItemBinding;
import com.example.bakingapp.model.Steps;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    StepItemBinding stepItemBinding;
    List<Steps> steps;

    public StepsAdapter(List<Steps> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        stepItemBinding = StepItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StepsViewHolder(stepItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        Steps currentSteps = steps.get(position);
        holder.bind(currentSteps);
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

        StepItemBinding stepItemBinding;

        public StepsViewHolder(StepItemBinding stepItemBinding) {
            super(stepItemBinding.getRoot());
            this.stepItemBinding = stepItemBinding;
        }

        public void bind(Steps steps) {
            stepItemBinding.stepShortDescription.setText(steps.getShortDescription());
            stepItemBinding.executePendingBindings();
        }
    }
}
