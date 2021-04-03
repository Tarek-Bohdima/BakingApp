 /*
 * MIT License
 * Copyright (c) 2021.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This project was submitted by Tarek Bohdima as part of the Android Developer
 * Nanodegree At Udacity.
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 * I, the author of the project, allow you to check the code as a reference, but if you
 * submit it, it's your own responsibility if you get expelled.
 */

package com.example.bakingapp.ui.detail.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.ItemStepBinding;
import com.example.bakingapp.model.Steps;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    static int row_index = -1;
    ItemStepBinding itemStepBinding;
    List<Steps> stepsList;
    OnStepClickListener listener;

    public StepsAdapter(List<Steps> stepsList, OnStepClickListener onStepClickListener) {
        this.stepsList = stepsList;
        this.listener = onStepClickListener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemStepBinding = ItemStepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StepsViewHolder(itemStepBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        Steps currentSteps = stepsList.get(position);
        holder.bind(currentSteps, listener);
        if (row_index == position) {
            holder.itemStepBinding.getRoot().setBackgroundColor(Color.parseColor("#FF6200EE"));
            holder.itemStepBinding.stepShortDescription.setTextColor(Color.YELLOW);

        } else {
            holder.itemStepBinding.getRoot().setBackgroundColor(Color.parseColor("#F6C487"));
            holder.itemStepBinding.stepShortDescription.setTextColor(Color.BLACK);
        }
        itemStepBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return stepsList == null ? 0 : stepsList.size();
    }

    public void setStepsData(List<Steps> stepsData) {
        stepsList = stepsData;
        notifyDataSetChanged();
    }

    public interface OnStepClickListener {
        void onStepClick(Steps steps);
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder {

        ItemStepBinding itemStepBinding;

        public StepsViewHolder(ItemStepBinding itemStepBinding) {
            super(itemStepBinding.getRoot());
            this.itemStepBinding = itemStepBinding;
        }

        public void bind(Steps steps, OnStepClickListener listener) {

            itemStepBinding.stepShortDescription.setText(steps.getShortDescription());
            itemStepBinding.setStep(steps);
            itemStepBinding.getRoot().setOnClickListener(v -> {
                changeBackgroundColor();
                listener.onStepClick(steps);
            });
            itemStepBinding.executePendingBindings();
        }

        private void changeBackgroundColor() {
            int position = getAdapterPosition();
            int copyOfRowIndex = row_index;
            row_index = position;
            notifyItemChanged(copyOfRowIndex);
            notifyItemChanged(row_index);
        }
    }
}
