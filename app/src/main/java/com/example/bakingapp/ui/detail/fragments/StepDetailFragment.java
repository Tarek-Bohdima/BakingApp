package com.example.bakingapp.ui.detail.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bakingapp.Constants;
import com.example.bakingapp.R;
import com.example.bakingapp.databinding.FragmentStepDetailBinding;
import com.example.bakingapp.model.Steps;
import com.example.bakingapp.ui.detail.viewmodels.RecipeDetailViewModel;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class StepDetailFragment extends Fragment {

    private Steps currentStep;
    private PlayerView playerView;
    private TextView stepDescription;
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

    private static void onNextBtnClick(View v) {

    }

    private static void onPreviousBtnClick(View v) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentStepDetailBinding fragmentStepDetailBinding = DataBindingUtil
                .inflate(inflater,
                        R.layout.fragment_step_detail,
                        container, false);

        stepDescription = fragmentStepDetailBinding.stepDescription;
        nextButton = fragmentStepDetailBinding.nextStepButton;
        previousButton = fragmentStepDetailBinding.previousStepButton;
        playerView = fragmentStepDetailBinding.exoPlayerView;


        return fragmentStepDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecipeDetailViewModel mViewModel = new ViewModelProvider(requireActivity()).get(RecipeDetailViewModel.class);
        currentStep = mViewModel.getCurrentStep();
        Timber.tag(Constants.TAG).d("StepDetailFragment: onViewCreated() called with: video url: " + currentStep.getVideoURL());
        stepDescription.setText(currentStep.getDescription());
        Timber.tag(Constants.TAG).d("StepDetailFragment: onViewCreated() called with: step description: " + currentStep.getDescription());
        previousButton.setOnClickListener(StepDetailFragment::onPreviousBtnClick);

        nextButton.setOnClickListener(StepDetailFragment::onNextBtnClick);
    }

    private void initializePlayer() {

        player = new SimpleExoPlayer.Builder(requireActivity()).build();

        playerView.setPlayer(player);

        String uri = currentStep.getVideoURL();
        MediaItem mediaItem = MediaItem.fromUri(uri);
//        MediaItem mediaItem = MediaItem.fromUri("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
        player.setMediaItem(mediaItem);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
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