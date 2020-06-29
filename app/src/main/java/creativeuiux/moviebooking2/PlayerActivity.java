package creativeuiux.moviebooking2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import adapter.Cast_RecycleviewAdapter;
import modalclass.CastModalClass;

public class PlayerActivity extends AppCompatActivity {

    ImageView imageView,imgposter;
    ImageButton playbutton;
    String title,poster,url,cat,lang;
    TextView tvjudul,tvcat,tvlang;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    Cast_RecycleviewAdapter cast_recycleviewAdapter;

    private ArrayList<CastModalClass> castArrayList;


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

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
//            initializePlayer();
        }
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
        Glide.with(getApplicationContext()).load(poster).error(R.drawable.andhadhun).into(imageView);


        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                playbutton.setVisibility(View.GONE);
                imgposter.setVisibility(View.GONE);
                initializePlayer();
            }
        });




    }



    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);
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
