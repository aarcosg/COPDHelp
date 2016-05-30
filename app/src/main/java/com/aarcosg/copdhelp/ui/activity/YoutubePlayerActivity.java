package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YoutubePlayerActivity extends BaseActivity {

    private static final String TAG = YoutubePlayerActivity.class.getCanonicalName();

    private static final String EXTRA_YOUTUBE_DEV_KEY = "extra_youtube_dev_key";
    private static final String EXTRA_VIDEO_ID = "extra_video_id";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private YouTubePlayerSupportFragment mYouTubePlayerFragment;
    private String mYoutubeDevKey;
    private String mVideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player_content);
        mYoutubeDevKey = getIntent().getStringExtra(EXTRA_YOUTUBE_DEV_KEY);
        mVideoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        mYouTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        mYouTubePlayerFragment.setRetainInstance(true);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mYouTubePlayerFragment)
                .commit();
        initializeVideo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST && resultCode == Activity.RESULT_OK) {
            initializeVideo();
        }
    }

    private void initializeVideo() {
        mYouTubePlayerFragment.initialize(mYoutubeDevKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.loadVideo(mVideoId);
                }
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
                if (errorReason.isUserRecoverableError()) {
                    errorReason.getErrorDialog(YoutubePlayerActivity.this, RECOVERY_DIALOG_REQUEST).show();
                } else {
                    String errorMessage = String.format(getString(R.string.exception_message_youtube_player_error), errorReason.toString());
                    Toast.makeText(YoutubePlayerActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void launch(Activity activity, String youtubeDevKey, String videoId) {
        Intent intent = new Intent(activity, YoutubePlayerActivity.class);
        intent.putExtra(EXTRA_YOUTUBE_DEV_KEY, youtubeDevKey);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        ActivityCompat.startActivity(activity, intent, null);
    }
}