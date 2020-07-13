package tvonline.indotv;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class PlayerActivity extends AppCompatActivity {

    LinearLayout admoblinear;
    Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen = false;
    private ImageView mFullScreenIcon;
    private FrameLayout mFullScreenButton;
    private int mResumeWindow;
    private long mResumePosition;
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";


    ImageView imageView,imgposter;
    ImageButton playbutton;
    String title,poster,url,cat,lang;
    TextView tvjudul,tvcat,tvlang;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;




    private PlayerView playerView;
    private SimpleExoPlayer player;


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
//            initializePlayer();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (playerView != null && playerView.getPlayer() != null) {
            mResumeWindow = playerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, playerView.getPlayer().getContentPosition());

            playerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();


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

    @Override
    public void onResume() {
        super.onResume();

        if (playerView == null) {

            playerView =  findViewById(R.id.video_view);






        }
        initFullscreenDialog();
        initFullscreenButton();
        initializePlayer();



        if (mExoPlayerFullscreen) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
            mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.exo_controls_fullscreen_enter));
            mFullScreenDialog.show();
        }



//        initFullscreenDialog();
//        initFullscreenButton();
//
//        initializePlayer();
//
//        if (mExoPlayerFullscreen) {
//            ((ViewGroup) playerView.getParent()).removeView(playerView);
//            mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.exo_controls_fullscreen_enter));
//            mFullScreenDialog.show();
//        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("title");
        poster = getIntent().getStringExtra("poster");
        url=getIntent().getStringExtra("url");
        lang=getIntent().getStringExtra("lang");
        cat=getIntent().getStringExtra("cat");
        setContentView(R.layout.activity_movie_info);



        //Recycleview
        tvjudul=findViewById(R.id.judul);
        tvcat=findViewById(R.id.cat);
        tvlang=findViewById(R.id.lang);
        tvjudul.setText(title);
        tvcat.setText(cat);
        tvlang.setText(lang);
        playerView = findViewById(R.id.video_view);
        imageView=findViewById(R.id.imagebg);
        playbutton=findViewById(R.id.playbutton);
        imgposter=findViewById(R.id.poster);
        Glide.with(getApplicationContext()).load(poster).error(R.drawable.tv).into(imageView);
        playerView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        playbutton.setVisibility(View.GONE);
        imgposter.setVisibility(View.GONE);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(SplashActivity.banneradmob);
        admoblinear=findViewById(R.id.banner_container);
        admoblinear.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


//        playbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playerView.setVisibility(View.VISIBLE);
//                imageView.setVisibility(View.GONE);
//                playbutton.setVisibility(View.GONE);
//                imgposter.setVisibility(View.GONE);
//                initializePlayer();
//            }
//        });

    }
    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.exo_controls_fullscreen_exit));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(playerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(PlayerActivity.this, R.drawable.exo_controls_fullscreen_enter));
    }

    private void initFullscreenButton() {

        PlaybackControlView controlView = playerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }




    private void initializePlayer() {

        player = ExoPlayerFactory.newSimpleInstance(this);
        player.stop();
        playerView.setPlayer(player);
        Uri uri = Uri.parse(url);
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "TV"));
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .setAllowChunklessPreparation(true)
                        .createMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(hlsMediaSource, false, false);
    }





}
