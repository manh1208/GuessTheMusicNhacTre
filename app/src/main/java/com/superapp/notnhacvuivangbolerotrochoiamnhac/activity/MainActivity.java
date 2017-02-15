package com.superapp.notnhacvuivangbolerotrochoiamnhac.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.banner.RevMobBanner;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.R;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.application.AnalyticsApplication;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.model.Question;
import com.superapp.notnhacvuivangbolerotrochoiamnhac.utils.BaseGameUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 1999;
    private static final int RC_UNUSED = 1998;
    private List<Question> questions;
    private ConnectivityManager connManager;
    private NetworkInfo mMobile;
    private NetworkInfo mWifi;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;
    private RevMob revmob;
    private RevMobBanner banner;
    private Activity currentActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        Log.i(TAG, "Setting screen name: Game");
        mTracker.setScreenName("MainMenu");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        Button btnMore = (Button) findViewById(R.id.btn_more);
//        btnMore.setOnClickListener(this);
        connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(AppIndex.API).build();
        checkFacebook();
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
                ViewGroup view = (ViewGroup) findViewById(R.id.bannerLayout);
                view.addView(banner);
                banner.show(); //This method must be called in order to display the ad.
            }
        });
    }

    public void onRateClick(View v) {
//        DataUtils.playSound(getApplicationContext(),R.raw.click);
        Uri uri;
        Intent intent;
//        uri = Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=com.superapp.notnhacvuitretrochoiamnhac");
//        uri=Uri.parse("http://appvn.com/android/details?id=com.superapp.notnhacvuitretrochoiamnhac");
//        uri = Uri.parse("http://play.google.com/store/apps/details?id=com.superapp.notnhacvuitretrochoiamnhac");
         uri = Uri.parse("http://www.bestappsforphone.com");
        intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

    public void onLikeUsClick(View v) {
//        DataUtils.playSound(getApplicationContext(),R.raw.click);
        Uri uri;
        Intent intent;
        uri = Uri.parse("https://www.facebook.com/supercoolappteam");
        intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

    public void onAboutUsClick(View v) {
//        DataUtils.playSound(getApplicationContext(),R.raw.click);
        Uri uri;
        Intent intent;
        uri = Uri.parse("http://www.bestappsforphone.com");
        intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

    public void onHotGameClick(View v) {
//        DataUtils.playSound(getApplicationContext(),R.raw.click);
        Uri uri;
        Intent intent;
//        uri=Uri.parse("http://www.bestappsforphone.com/appotagameofthemonth");
//        uri =Uri.parse("http://www.bestappsforphone.com/kindlegameofthemonth");
//        uri = Uri.parse("http://www.bestappsforphone.com/gameofthemonth");
           uri = Uri.parse("http://www.bestappsforphone.com");
        intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }


    public void onLeaderBoardClick(View v){
        if (mWifi.isConnected() || mMobile.isConnected()) {
            Toast.makeText(MainActivity.this, "is Signed In:  "+ isSignedIn(), Toast.LENGTH_SHORT).show();
            if (isSignedIn()) {
                startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                        RC_UNUSED);
            } else {
                mGoogleApiClient.connect();
            }
        }else{
            Toast.makeText(MainActivity.this, "Không có kết nối internet. Xin hãy bật wifi hoặc dữ liệu di động", Toast.LENGTH_SHORT).show();
//            AlertDialog myAlertDialog = taoMotAlertDialog();
//            myAlertDialog.show();
        }
    }

    private void checkFacebook(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.superapp.guessthepresidentushistoryquiztrivia", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                Intent intent = new Intent(MainActivity.this, GamePlayTocDoActivity.class);
                startActivity(intent);
                break;
//            case R.id.btn_more:
//                intent = new Intent(MainActivity.this, SettingActivity.class);
//                startActivity(intent);
//                break;
        }
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
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                    RC_UNUSED);
        }
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
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }

    public void releaseBanner(){
        banner.release();
            Log.i("RevMob","Banner was release");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
       releaseBanner();
    }
}
