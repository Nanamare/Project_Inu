package comnanamareproject_inu.httpsgithub.sketch_app;


import android.content.IntentSender;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Login_Activity";
    private GoogleApiClient mGoogleApiClient;
    final static String SAMPLE_VIDEO_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    VideoView videoView;
    SeekBar seekBar;
    Handler updateHandler = new Handler();


    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText tvURL = (EditText)findViewById(R.id.etVieoURL);
        tvURL.setText(SAMPLE_VIDEO_URL);
        videoView = (VideoView)findViewById(R.id.videoView);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        userName = (TextView) findViewById(R.id.userName);
    }

    public void loadVideo(View view) {
        //Sample video URL : http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_2mb.mp4
        EditText tvURL = (EditText) findViewById(R.id.etVieoURL);
        String url = tvURL.getText().toString();

        Toast.makeText(getApplicationContext(), "Loading Video. Plz wait", Toast.LENGTH_LONG).show();
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();


        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {

                                        @Override
                                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                            switch(what){
                                                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                                    // Progress Diaglog
                                                    Toast.makeText(getApplicationContext(), "Buffering", Toast.LENGTH_LONG).show();
                                                    break;
                                                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                                    // Progress Dialog
                                                    Toast.makeText(getApplicationContext(), "Buffering finished.\nResume playing", Toast.LENGTH_LONG).show();
                                                    videoView.start();
                                                    break;
                                            }
                                            return false;
                                        }
                                    }

        );

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                long finalTime = videoView.getDuration();
                TextView tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
                tvTotalTime.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );
                seekBar.setMax((int) finalTime);
                seekBar.setProgress(0);
                updateHandler.postDelayed(updateVideoTime, 100);
                //Toast Box
                Toast.makeText(getApplicationContext(), "Playing Video", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void playVideo(View view){
        videoView.requestFocus();
        videoView.start();

    }

    public void pauseVideo(View view){
        videoView.pause();
    }

    private Runnable updateVideoTime = new Runnable(){
        public void run(){
            long currentPosition = videoView.getCurrentPosition();
            seekBar.setProgress((int) currentPosition);
            updateHandler.postDelayed(this, 100);

        }
    };


    public void mOnClick(View view){
        switch (view.getId()){
            case R.id.btn_con:
                Toast.makeText(this, "접속합니다", Toast.LENGTH_SHORT).show();

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .build();

                mGoogleApiClient.connect();

                break;
        }
    }


    public void onConnected(Bundle bundle) {
        Log.d(TAG, "구글 플레이 연결이 되었습니다.");

        if (!mGoogleApiClient.isConnected() || Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null) {

            Log.d(TAG, "onConnected 연결 실패");

        } else {
            Log.d(TAG, "onConnected 연결 성공");

            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            if (currentPerson.hasImage()) {

                Log.d(TAG, "이미지 경로는 : " + currentPerson.getImage().getUrl());

               /* Glide.with(MainActivity.this)
                        .load(currentPerson.getImage().getUrl())
                        .into(userphoto);*/

            }
            if (currentPerson.hasDisplayName()) {
                Log.d(TAG,"디스플레이 이름 : "+ currentPerson.getDisplayName());
                Log.d(TAG, "디스플레이 아이디는 : " + currentPerson.getId());
                userName.setText(currentPerson.getDisplayName()+"님 안녕 하세요");
            }

        }
    }


    public void onConnectionSuspended(int i) {

    }


    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "연결 에러 " + connectionResult);

        if (connectionResult.hasResolution()) {

            Log.e(TAG,
                    String.format(
                            "Connection to Play Services Failed, error: %d, reason: %s",
                            connectionResult.getErrorCode(),
                            connectionResult.toString()));
            try {
                //이게 핵심?
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, e.toString(), e);
            }
        }else{
            Toast.makeText(getApplicationContext(), "이미 로그인 중", Toast.LENGTH_SHORT).show();
        }
    }
}