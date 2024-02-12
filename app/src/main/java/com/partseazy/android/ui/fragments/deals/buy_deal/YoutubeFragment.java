package com.partseazy.android.ui.fragments.deals.buy_deal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.R;
import com.partseazy.android.constants.AppConstants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by naveen on 18/7/17.
 */

public class YoutubeFragment extends android.support.v4.app.Fragment implements YouTubePlayer.OnInitializedListener {

    public static final String VIDEO_ID = "video_id";

    private YouTubePlayer youTubePlayer;
    public YouTubePlayerSupportFragment youTubePlayerFragment;
    private String videoId;
    protected View view;

    public static YoutubeFragment newInstance(String videoId) {
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_ID, videoId);
        YoutubeFragment fragment = new YoutubeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoId = getArguments().getString(VIDEO_ID);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // if (view == null) {
        view = inflater.inflate(R.layout.fragment_youtube, container, false);
        //}

//        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.youtubeFrameLYT, youTubePlayerFragment).commit();
//
//        youTubePlayerFragment.initialize(AppConstants.PARTSEAZY_APP_KEY,this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        FragmentManager mFragmentManager = getChildFragmentManager();
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        ;
        mFragmentManager
                .beginTransaction()
                .replace(R.id.youtubeFrameLYT,
                        youTubePlayerFragment, "myProjectListFragment")
                .commit();


        youTubePlayerFragment.initialize(AppConstants.PARTSEAZY_APP_KEY, this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        youTubePlayer = player;
        if (!wasRestored) {
            youTubePlayer.setShowFullscreenButton(false);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            youTubePlayer.loadVideo(videoId);

            youTubePlayer.setPlaybackEventListener(playbackEventListener);
            //    youTubePlayer.play();
        }
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        private boolean interceptPlay = true;

        @Override
        public void onPlaying() {

            if (interceptPlay) {
                youTubePlayer.pause();
                interceptPlay = false;
            }
        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        String errorMessage = youTubeInitializationResult.toString();
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        Log.d("errorMessage:", errorMessage);
    }

    @Override
    public void onDestroyView() {
        if (youTubePlayer != null) {

            youTubePlayer.release();
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (youTubePlayer != null) {
            try {
                youTubePlayer.play();
            } catch (Exception e) {
                CustomLogger.e("Exception", e);
            }
        }
    }
}
