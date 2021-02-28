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

package com.example.bakingapp.ui.detail.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.bakingapp.Constants;
import com.example.bakingapp.R;
import com.example.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.RecipeDetailActivity;
import com.example.bakingapp.ui.detail.viewmodels.RecipeDetailViewModel;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class StepDetailFragment extends Fragment {

    private FragmentStepDetailBinding fragmentStepDetailBinding;
    private RecipeDetailViewModel mViewModel;
    private LiveData<Integer> currentStepPosition;
    private TextView stepDescription;
    private Steps currentStep;
    private PlayerView playerView;
    private List<Steps> currentStepList;
    private Button nextButton;
    private Button previousButton;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance() {
        return new StepDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.tag(Constants.TAG).d("StepDetailFragment: onCreate() called ..............");
        // https://developer.android.com/guide/navigation/navigation-custom-back
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Timber.tag(Constants.TAG).d("StepDetailFragment: onCreateView() called ......... ");
        fragmentStepDetailBinding = DataBindingUtil
                .inflate(inflater,
                        R.layout.fragment_step_detail,
                        container, false);

        return fragmentStepDetailBinding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeVariables();
        observeCurrentStepPosition();
    }

    private void observeCurrentStepPosition() {
        currentStepPosition.observe(getViewLifecycleOwner(), this::onChanged);
    }

    private void onChanged(Integer integer) {
        currentStep = currentStepList.get(integer);
        stepDescription.setText(currentStep.getDescription());
        previousButton.setOnClickListener(this::onClick);
        nextButton.setOnClickListener(this::onClick2);
    }

    private void onClick(View v) {
        if (mViewModel.hasPrevious()) {
            enableButton(previousButton, nextButton);
            mViewModel.previousStep();
            reAttachFragment();
        } else {
            previousButton.setClickable(false);
            previousButton.setBackgroundColor(Color.GRAY);
            Toast.makeText(requireActivity(),
                    "Beginning of Steps", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClick2(View v) {
        if (mViewModel.hasNext()) {
            enableButton(nextButton, previousButton);
            mViewModel.nextStep();
            releasePlayer();
            reAttachFragment();
        } else {
            nextButton.setClickable(false);
            nextButton.setBackgroundColor(Color.GRAY);
            Toast.makeText(requireActivity(),
                    "End of Steps", Toast.LENGTH_SHORT).show();
        }
    }

    private static void enableButton(Button currentButton, Button otherButton) {
        currentButton.setClickable(true);
        currentButton.setBackgroundColor(Color.BLUE);
        otherButton.setBackgroundColor(Color.BLUE);
    }

    private void initializeVariables() {
        mViewModel = new ViewModelProvider(requireActivity()).get(RecipeDetailViewModel.class);
        stepDescription = fragmentStepDetailBinding.stepDescription;
        nextButton = fragmentStepDetailBinding.nextStepButton;
        previousButton = fragmentStepDetailBinding.previousStepButton;
        playerView = fragmentStepDetailBinding.exoPlayerView;
        currentStepPosition = mViewModel.getCurrentStepPosition();
        currentStepList = mViewModel.getStepsList();
        currentStep = currentStepList.get(currentStepPosition.getValue());
        stepDescription.setText(currentStep.getDescription());
    }

    private void reAttachFragment() {
        StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance();
        Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentByTag(RecipeDetailActivity.STEP_DETAIL_PORTRAIT_FRAGMENT);
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(currentFragment);
        fragmentTransaction.add(R.id.item_detail_container, stepDetailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
        stepDescription = null;
        fragmentStepDetailBinding = null;
        mViewModel = null;
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
   /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }*/

    private void initializePlayer() {

        player = new SimpleExoPlayer.Builder(requireActivity()).build();
        playerView.setPlayer(player);
        String uri = currentStep.getVideoURL();
        if (TextUtils.isEmpty(uri)) {
            player.stop(true);
        }else{
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.prepare();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }
}