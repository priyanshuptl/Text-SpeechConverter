package appinventor.ai_Priyanshu0ptl.TextSpeech;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.LANG_MISSING_DATA;
import static android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED;
import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;
import static android.speech.tts.TextToSpeech.SUCCESS;

public class SpeakScreen extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener ,
        ActivityCompat.OnRequestPermissionsResultCallback {

    TextToSpeech tts;
    int result, pitch, speechRate , fileIndex;
    EditText editText;
    float valPitch, valSpeechRate;
    String text , mPath , mFileName , fileToDelete;
    private SeekBar pitchSeekBar, speechRateSeekBar;
    Spinner spinner;
    MediaRecorder mediaRecorder;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private String [] permissions1 = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String [] langs ={"English","Hindi","Canada","Canada_French","China","French","German","Italian","Japanese","Korean","UK","US"};
    private static final int REQUEST_WRITE_PERMISSION = 786;
    File file ;
    private SharedPreferences preferenceSetting;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    boolean isAudioSaved= false;
    private Button saveButton , shareButton;
    private AdView adView, adView1;
    AdRequest adRequest = new AdRequest.Builder().build();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case REQUEST_WRITE_PERMISSION:
                permissionToWriteAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }
        if ( !permissionToWriteAccepted ) {
            ActivityCompat.requestPermissions(this, permissions1, REQUEST_WRITE_PERMISSION);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.setTitle("Record Audio Permission");
                alertDialog.setMessage("You will not be able to record and share the audio generated from text to speech converter." +
                        "to access this feature give the permission to record audio");

                alertDialog.setButton( 1,"Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SpeakScreen.this, permissions,
                                REQUEST_RECORD_AUDIO_PERMISSION);

                    }
                });
                alertDialog.setButton(2, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
            else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,permissions,
                        REQUEST_RECORD_AUDIO_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.setTitle("Write Permission");
                alertDialog.setMessage("You will not be able to record and share the audio generated from text to speech converter." +
                        "to access this feature give the permission to Write storage");

                alertDialog.setButton( 1,"Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SpeakScreen.this, permissions1,
                                REQUEST_WRITE_PERMISSION);

                    }
                });
                alertDialog.setButton(2, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
            else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,permissions1,
                        REQUEST_WRITE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        adView = (AdView) findViewById(R.id.adView3);
        adView.loadAd(adRequest);
        adView1 = (AdView) findViewById(R.id.adView4);
        adView1.loadAd(adRequest);

        pitchSeekBar = (SeekBar) findViewById(R.id.pitchSeekBar) ;
        pitchSeekBar.setOnSeekBarChangeListener(this);

        speechRateSeekBar = (SeekBar) findViewById(R.id.speechRateSeekBar);
        speechRateSeekBar.setOnSeekBarChangeListener(this);

        spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);

        mediaRecorder = new MediaRecorder();

        saveButton = (Button) findViewById(R.id.saveAudioButton);
        shareButton = (Button) findViewById(R.id.shareAudioButton);
        saveButton.setEnabled(false);
        shareButton.setEnabled(false);

        preferenceSetting = getPreferences(PREFERENCE_MODE_PRIVATE);
        fileIndex = preferenceSetting.getInt("fileIndex",1);
        mPath = Environment.getExternalStorageDirectory().getPath()+"/Download/";
        mFileName = "aud" + fileIndex + ".mp3";

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, langs);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        editText = (EditText) findViewById(R.id.editText);
        tts = new TextToSpeech(SpeakScreen.this, new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int status) {
                if(status == SUCCESS){

                    result = tts.setLanguage(Locale.getDefault());
                }
                else{
                    Toast.makeText(getApplicationContext(),"Feature not Supported in your Device",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();

        if(id == R.id.pitchSeekBar) {
            valPitch = (float) ((float)progress*19/1000 + 0.1);
        }
        else if (id == R.id.speechRateSeekBar){
            valSpeechRate =  (float) ((float)progress*19/1000 + 0.1);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        pitch = tts.setPitch(valPitch);
        speechRate = tts.setSpeechRate(valSpeechRate);
    }

    public void saveAudio (View view) {
        Toast.makeText(getApplicationContext(),"Audio Saved as "+mPath+mFileName, Toast.LENGTH_LONG).show();
        fileIndex += 1;
        mFileName = "aud" + fileIndex + ".mp3";
        isAudioSaved = true;
        preferenceSetting = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSetting.edit();
        preferenceEditor.putInt("fileIndex",fileIndex);
        preferenceEditor.commit();
        saveButton.setEnabled(false);
    }

    public void shareAudio (View view){
        File file1;
        if ( isAudioSaved == false ) {
            file1 = new File(mPath + mFileName);
        }
        else {
            int fileToShare = fileIndex-1;
            file1 = new File(mPath + "aud" + fileToShare + ".mp3" );
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(file1);
        shareIntent.setType("audio/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Created using Text to Speech Converter by \"SoftechWorld\".");
        startActivity(Intent.createChooser(shareIntent, "Share Audio"));
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
        if (id == R.id.contactUs) {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void SpeakText (View view){
        if(result == LANG_MISSING_DATA || result == LANG_NOT_SUPPORTED ){
            Toast.makeText(getApplicationContext(), "This Language is not Supported in your Device", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean chk = true;

            fileToDelete = mPath+mFileName;
            file = new File(fileToDelete);
            if ( file.exists()) {
                file.delete();
            }
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            try {
                mediaRecorder.setOutputFile(mPath + mFileName);
            } catch (Exception e){
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try{
                mediaRecorder.prepare();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

            text = editText.getText().toString();

            try{
                mediaRecorder.start();
            } catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            tts.speak(text, QUEUE_FLUSH, null);

            saveButton.setEnabled(true);
            shareButton.setEnabled(true);

            while (chk == true){
                if(!tts.isSpeaking()){
                    if(mediaRecorder != null){
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        isAudioSaved = false;
                    }

                    chk = false;
                }
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(langs[position] == "English" ){
            result = tts.setLanguage(Locale.ENGLISH);
        }
        else if (langs[position] == "Canada" ){
            result = tts.setLanguage(Locale.CANADA);
        }
        else if (langs[position] == "Canada_French" ){
            result = tts.setLanguage(Locale.CANADA_FRENCH);
        }
        else if (langs[position] == "China" ){
            result = tts.setLanguage(Locale.CHINESE);
        }
        else if (langs[position] == "French" ){
            result = tts.setLanguage(Locale.FRENCH);
        }
        else if (langs[position] == "German" ){
            result = tts.setLanguage(Locale.GERMAN);
        }
        else if (langs[position] == "Italian" ){
            result = tts.setLanguage(Locale.ITALIAN);
        }
        else if (langs[position] == "Japanese" ){
            result = tts.setLanguage(Locale.JAPANESE);
        }
        else if (langs[position] == "Korean" ){
            result = tts.setLanguage(Locale.KOREAN);
        }
        else if (langs[position] == "UK" ){
            result = tts.setLanguage(Locale.UK);
        }
        else if (langs[position] == "US" ){
            result = tts.setLanguage(Locale.US);
        }
        else if (langs[position] == "Hindi" ){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                result = tts.setLanguage(Locale.forLanguageTag("hin"));
            }
            else{
                Toast.makeText(getApplicationContext(),"This Language is not Supported in your Device", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
