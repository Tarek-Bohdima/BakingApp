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

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bakingapp.Constants;
import com.example.bakingapp.R;
import com.example.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.viewmodels.SharedlViewModel;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import timber.log.Timber;

public class StepDetailFragment extends Fragment {

    private FragmentStepDetailBinding fragmentStepDetailBinding;
    private boolean isTwoPane;
    private boolean isLandscape;
    private SharedlViewModel sharedlViewModel;
    private int currentStepPosition;
    private Steps currentStep;
    private PlayerView playerView;
    private List<Steps> currentStepList;
    private Button nextButton;
    private Button previousButton;
    private SimpleExoPlayer player;
    private ImageView placeholder;
    private final int currentWindow = 0;

    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance() {
        return new StepDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTwoPane = getResources().getBoolean(R.bool.isTablet);
        sharedlViewModel = new ViewModelProvider(requireActivity()).get(SharedlViewModel.class);
        currentStep = sharedlViewModel.getStepsList().get(0);
        Timber.tag(Constants.TAG).d("StepDetailFragment: onCreate() called currentStep :%s", currentStep.getDescription());
        if (!isTwoPane) {
            customBackNavigation();
        }
    }

    private void customBackNavigation() {
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


        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        initializeVariables(currentStep);
        observeCurrentStep();
    }

    private void observeCurrentStep() {
        sharedlViewModel.getCurrentStep().observe(getViewLifecycleOwner(), steps -> {
            Timber.tag(Constants.TAG).d("StepDetailFragment: observeCurrentStep() called with currentStep = [%s]", currentStep);
            if (isTwoPane) {
                stopPlayer();
            }
            initializeVariables(steps);
            releasePlayer();
            initializePlayer();
        });
    }

    private void initializeVariables(Steps steps) {

        currentStepList = sharedlViewModel.getStepsList();
        currentStep = steps;
        currentStepPosition = currentStep.getId();
        playerView = fragmentStepDetailBinding.exoPlayerView;
        placeholder = fragmentStepDetailBinding.placeholder;
        if (!isLandscape) {
            TextView stepDescription = fragmentStepDetailBinding.stepDescription;
            String currentStepDescription = currentStep.getDescription();
            assert stepDescription != null;
            stepDescription.setText(currentStepDescription);
            previousButton = fragmentStepDetailBinding.previousStepButton;
            nextButton = fragmentStepDetailBinding.nextStepButton;
            assert previousButton != null;
            previousButton.setOnClickListener(this::onPreviousClick);
            nextButton.setOnClickListener(this::onNextClick);
        }
    }

    private void onPreviousClick(View v) {
        if (currentStepPosition > 0) {
            enableButton(nextButton);
            stopPlayer();
            Steps newStep = currentStepList.get(currentStepPosition - 1);
            sharedlViewModel.getCurrentStep().setValue(newStep);
        } else {
            disableButton(previousButton);
            Toast.makeText(requireActivity(), "Beginning of Steps", Toast.LENGTH_SHORT).show();
        }
    }

    private void onNextClick(View v) {
        if (currentStepPosition < currentStepList.size() - 1) {
            enableButton(previousButton);
            stopPlayer();
            Steps newStep = currentStepList.get(currentStepPosition + 1);
            sharedlViewModel.getCurrentStep().setValue(newStep);
        } else {
            disableButton(nextButton);
            Toast.makeText(requireActivity(), "End of Steps", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableButton(Button currentButton) {
        currentButton.setClickable(true);
        currentButton.setBackgroundColor(Color.BLUE);
    }

    private void disableButton(Button currentButton) {
        currentButton.setClickable(false);
        currentButton.setBackgroundColor(Color.GRAY);
    }

    private void initializePlayer() {
        String uri = currentStep.getVideoURL();
        if (TextUtils.isEmpty(uri)) {
            hidePlayer();
        } else {
            showPlayer();
            player = new SimpleExoPlayer.Builder(requireActivity()).build();
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
            sharedlViewModel.getPlayerPosition().observe(getViewLifecycleOwner(), aLong -> player.seekTo(currentWindow,aLong));

            sharedlViewModel.getPlayWhenReady().observe(getViewLifecycleOwner(), aBoolean -> player.setPlayWhenReady(aBoolean));
            player.prepare();
        }
    }

    private void showPlayer() {
        placeholder.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
    }

    private void hidePlayer() {
        playerView.setVisibility(View.INVISIBLE);
        placeholder.setVisibility(View.VISIBLE);
        releasePlayer();
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
        Long playerPosition = player.getCurrentPosition();
        sharedlViewModel.savePosition(playerPosition);
        boolean playWhenReady = player.getPlayWhenReady();
        sharedlViewModel.savePlayWhenReady(playWhenReady);

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
            player.release();
            player = null;
        }
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}