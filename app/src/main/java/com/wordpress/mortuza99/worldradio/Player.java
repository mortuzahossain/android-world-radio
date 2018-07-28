package com.wordpress.mortuza99.worldradio;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Player extends AppCompatActivity {

    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private ExtractorsFactory extractorsFactory;
    private TrackSelection.Factory trackSelectionFactory;
    private TrackSelector trackSelector;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private DataSource.Factory dataSourceFactory;
    private MediaSource mediaSource;

    boolean flag = true;
    int Duration = 100;
    RotateAnimation rotateAnimation;
    CircleImageView circleImageView;
    Button playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        circleImageView = findViewById(R.id.image);
        playBtn = findViewById(R.id.btnPlay);
        TextView name = findViewById(R.id.name);

        // Receiving Data From Intent
        String nameVal = getIntent().getStringExtra("NAME");
        String imageUrlVal = getIntent().getStringExtra("IMAGE");
        String streamUrlVal = getIntent().getStringExtra("STREAMURL");
        streamUrlVal = "http://66.45.232.131:9994/;stream/1/";

        // Setting The Name in the text view
        name.setText(nameVal);

        // Setting the image view
        Picasso.get()
                .load(imageUrlVal)
                .into(circleImageView);

        // Adding animation to the circular image view
        rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        bandwidthMeter = new DefaultBandwidthMeter();
        extractorsFactory = new DefaultExtractorsFactory();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);

        defaultBandwidthMeter = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"), defaultBandwidthMeter);


        mediaSource = new ExtractorMediaSource(Uri.parse(streamUrlVal), dataSourceFactory, extractorsFactory, null, null);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        player.prepare(mediaSource);


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    stopPlayer();
                    flag = false;
                } else {
                    startPlayer();
                    flag = true;
                }
            }
        });

        startPlayer();

    }

    private void stopPlayer() {
        playBtn.setBackgroundResource(R.drawable.play);
        player.setPlayWhenReady(false);
        Duration = 10;
        rotateAnimation.setDuration(Duration);
        circleImageView.startAnimation(rotateAnimation);
    }

    private void startPlayer() {
        playBtn.setBackgroundResource(R.drawable.stop);
        Duration = 1000;
        player.setPlayWhenReady(true);
        rotateAnimation.setDuration(Duration);
        circleImageView.startAnimation(rotateAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.setPlayWhenReady(false);
    }
}
