package com.superapp.notnhacvuivangbolerotrochoiamnhac.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.banner.RevMobBanner;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.R;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.application.AnalyticsApplication;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.utils.BaseGameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ResultActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    private static final String TAG = "ResultActivity";
    private static final int RC_SIGN_IN = 1990;
    private int result;
    private int mHighScore;
    private int type = 1;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;
    private RevMob revmob;
    private RevMobBanner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        result = getIntent().getIntExtra("Score", 0);
        TextView textView = (TextView) findViewById(R.id.txt_result_score);
        textView.setText(String.valueOf(result));
        TextView mtxtHighScore = (TextView) findViewById(R.id.txt_best_score);
        mHighScore = 0;
        String fileName = "";
        if (type == 1) {
            fileName = "bestScoreCountdown.dat";
        } else {
            fileName = "bestScoreTocdo.dat";
        }

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        Log.i(TAG, "Setting screen name: result");
        mTracker.setScreenName("Kết quả");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        
        File f = new File(String.valueOf(getFileStreamPath(fileName)));
        if (!f.exists()) {
            try {
                FileOutputStream fou = openFileOutput(fileName, MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fou);
                osw.write("0");
                osw.flush();
                mHighScore = 0;
                osw.close();
                fou.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileReader fr = null;
            BufferedReader br = null;
            try {
                fr = new FileReader(getFileStreamPath(fileName));
                br = new BufferedReader(fr);
                String s = br.readLine();

                mHighScore = Integer.parseInt(s);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Some thing is wrong", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Some thing is wrong", Toast.LENGTH_LONG).show();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "Some thing is wrong", Toast.LENGTH_LONG).show();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Some thing is wrong", Toast.LENGTH_LONG).show();
                    }
                }
                if (fr != null) {

                    try {
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Some thing is wrong", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
        if (result > mHighScore) {

            mHighScore = result;
//            if (mWifi.isConnected() || mMobile.isConnected()) {
//                if (isSignedIn()) {
//                    if (type == 1) {
//                        Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.LeaderBoard),
//                                mHighScore);
//                    }
//                } else {
//                    mGoogleApiClient.connect();
//                }
//            }
            //mtxtHighScore.setTextColor(getResources().getColor(R.color.textcoloryellow));
            try {
                f = new File(String.valueOf(getFileStreamPath(fileName)));
                if (f.exists()) {
                    f.delete();
                }
                FileOutputStream fou = openFileOutput(fileName, MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fou);
                osw.write("" + (result));
                osw.flush();
                osw.close();
                fou.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        assert mtxtHighScore != null;
        mtxtHighScore.setText("" + mHighScore);
        ImageButton playAgain = (ImageButton) findViewById(R.id.btn_play_again);
        assert playAgain != null;
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, GamePlayTocDoActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ImageButton home = (ImageButton) findViewById(R.id.btn_home);
        assert home != null;
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageButton share = (ImageButton) findViewById(R.id.btn_share);
        assert share != null;
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                FacebookSdk.sdkInitialize(getApplicationContext());
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                ShareDialog shareDialog = new ShareDialog(ResultActivity.this);
                shareDialog.show(ResultActivity.this, content);
            }
        });

        startRevMobSession();
    }

    public void startRevMobSession() {
        //RevMob's Start Session method:
        revmob = RevMob.startWithListener(this, new RevMobAdsListener() {
            @Override
            public void onRevMobSessionStarted() {
                loadBanner(); // Cache the banner once the session is started
                Log.i("RevMob","Session Started");
            }
            @Override
            public void onRevMobSessionNotStarted(String message) {
                //If the session Fails to start, no ads can be displayed.
                Log.i("RevMob","Session Failed to Start");
            }
        }, "588af7646ba989107a64666c");
        if (revmob!=null){
            loadBanner();
        }
        Log.i("Revmob","ahihi");
    }

    public void loadBanner(){
        banner = revmob.preLoadBanner(this, new RevMobAdsListener(){
            @Override
            public void onRevMobAdReceived() {
                showBanner();
                Log.i("RevMob","Banner Ready to be Displayed"); //At this point, the banner is ready to be displayed.
            }
            @Override
            public void onRevMobAdNotReceived(String message) {
                Log.i("RevMob","Banner Not Failed to Load");
            }
            @Override
            public void onRevMobAdDisplayed() {
                Log.i("RevMob","Banner Displayed");
            }
        });
    }

    public void showBanner(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    if (banner.getParent()==null){
                    ViewGroup view = (ViewGroup) findViewById(R.id.bannerLayout);
                    view.addView(banner);
                    }
                    banner.show(); //This method must be called in order to display the ad.

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
        //Toast.makeText(ResultActivity.this, "Haha", Toast.LENGTH_SHORT).show();
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed(): attempting to resolve");
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }
        if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                RC_SIGN_IN, getString(R.string.signin_other_error))) {
            mResolvingConnectionFailure = false;
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        if (p == null) {
            Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
            // displayName = "???";
        } else {
            if (type == 1) {
                Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.LeaderBoard),
                        mHighScore);
            }
            // Toast.makeText(ResultActivity.this, "Updated new score", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!banner.isShown()) {
            loadBanner();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }
}
