package appinventor.ai_Priyanshu0ptl.TextSpeech;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private AdView mAdView,mAdView2;
    private InterstitialAd mInterstitialAd = new InterstitialAd(this), mInterstitialAd2 = new InterstitialAd(this),
            mInterstitialAd3 = new InterstitialAd(this), mInterstitialAd4 = new InterstitialAd(this);
    AdRequest adRequest = new AdRequest.Builder().build();

    int REQUEST_RECORD_AUDIO_PERMISSION= 100, REQUEST_WRITE_PERMISSION=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInterstitialAd.setAdUnitId("ca-app-pub-4036219497911064/9237378159");
        mInterstitialAd2.setAdUnitId("ca-app-pub-4036219497911064/9237378159");
        mInterstitialAd3.setAdUnitId("ca-app-pub-4036219497911064/2837467240");
        mInterstitialAd4.setAdUnitId("ca-app-pub-4036219497911064/8552828353");
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd2.loadAd(adRequest);
        mInterstitialAd3.loadAd(adRequest);
        mInterstitialAd4.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                mInterstitialAd.show();
            }
        });

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        mAdView2 = (AdView) findViewById(R.id.adView2);
        mAdView2.loadAd(adRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInterstitialAd2.isLoaded()) {
            mInterstitialAd2.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_About) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void shareApp (View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=appinventor.ai_Priyanshu0ptl.TextSpeech&hl=en");
        startActivity(Intent.createChooser(intent,"Share to "));
    }

    public void gotoSpeakScreen (View view){
        if (mInterstitialAd3.isLoaded()) {
            mInterstitialAd3.show();
        }
        Intent intent = new Intent(this, appinventor.ai_Priyanshu0ptl.TextSpeech.SpeakScreen.class);
        startActivity(intent);
    }

    public void gotoWriteScreen (View view){
        if (mInterstitialAd4.isLoaded()) {
            mInterstitialAd4.show();
        }
        Intent intent = new Intent(this, appinventor.ai_Priyanshu0ptl.TextSpeech.WriteScreen.class);
        startActivity(intent);
    }

}
