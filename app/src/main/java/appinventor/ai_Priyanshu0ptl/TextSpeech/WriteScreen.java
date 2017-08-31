package appinventor.ai_Priyanshu0ptl.TextSpeech;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Locale;

public class WriteScreen extends AppCompatActivity {

    AdView adView, adView1;
    AdRequest adRequest;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView resultTaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Write");
        setContentView(R.layout.activity_write);

        adRequest = new AdRequest.Builder().build();
        adView = (AdView) findViewById(R.id.adView5);
        adView.loadAd(adRequest);
        adView1 = (AdView) findViewById(R.id.adView6);
        adView1.loadAd(adRequest);

        resultTaker= (TextView) findViewById(R.id.resultTaker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

    public void WriteResult (View view){
        promptSpeechInput();
    }

    private  void promptSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Something...");

        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }
        catch (Exception e ){
            Toast.makeText(getApplicationContext(),"This feature is not Supported on you device",Toast.LENGTH_SHORT).show();
        }
    }

    public void shareText (View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, resultTaker.getText());
        startActivity(Intent.createChooser(intent, "Share On"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resultTaker.setText(result.get(0));
                }
                break;
            }

        }
    }
}
