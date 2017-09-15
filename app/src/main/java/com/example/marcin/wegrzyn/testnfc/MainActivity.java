package com.example.marcin.wegrzyn.testnfc;

import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    public static final int READ_LOADER_ID = 1;
    public static final int WRITE_LOADER_ID = 2;


    public static final String TEXT_VIEW = "textView";
    private TextView textView;
    private Button WriteButton;
    private EditText editTextData;
    private EditText editTextAdr;
    private ProgressBar progressBar;
    private String getEditText;

    private NfcAdapter nfcAdapter;

    private Tag tag;
    private PendingIntent pendingIntent;

    private IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

    private String[][] techListsArray = new String[][] {
            new String[] { NfcF.class.getName()},
            new String[] {NfcA.class.getName()}
    };

    private IntentFilter[] intentFiltersArray = new IntentFilter[] {intentFilter, };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Toast.makeText(this, R.string.no_nfc,Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        textView = (TextView) findViewById(R.id.TV);
        editTextData = (EditText) findViewById(R.id.ETdata);
        editTextAdr = (EditText) findViewById(R.id.ETadr);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        WriteButton = (Button) findViewById(R.id.butWrite);

        WriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tag!=null){
                    String adr = String.valueOf(editTextAdr.getText());
                    String data = String.valueOf(editTextData.getText());

                    if(adr==null||data==null||adr.length()<2||data.length()<8||Integer.parseInt(adr,16)>15){
                        Toast.makeText(getBaseContext(), R.string.correct_values,Toast.LENGTH_SHORT).show();
                    }else {
                        getEditText = adr + data;
                        writeLoader();
                    }

                }else Toast.makeText(getBaseContext(),getString(R.string.no_tag),Toast.LENGTH_SHORT).show();

            }
        });

        try {
            intentFilter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw  new RuntimeException("Fail add data type",e);
        }
        if(savedInstanceState!= null&&!savedInstanceState.isEmpty()){
            textView.setText(savedInstanceState.getCharSequence(TEXT_VIEW));
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void writeLoader() {
        getLoaderManager().restartLoader(WRITE_LOADER_ID,null,MainActivity.this).forceLoad();
    }

    private void readLoader() {
        getLoaderManager().restartLoader(READ_LOADER_ID,null,MainActivity.this).forceLoad();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFiltersArray,techListsArray);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null ){
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Toast.makeText(this, R.string.tag_detected,Toast.LENGTH_SHORT).show();
            textView.setText("id: "+String.valueOf(tag.getId())+"\n");

            String [] strings = tag.getTechList();
            for (String string : strings) {
                textView.append(string+"\n");
            }
            textView.append("\n\n");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(TEXT_VIEW,textView.getText());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.read:
                if(tag!=null){
                    readLoader();
                }else Toast.makeText(getBaseContext(), R.string.no_tag,Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);

        if(id == READ_LOADER_ID)return new ReadLoader(this,tag);
        if(id == WRITE_LOADER_ID)return new WriteLoader(this,new Wrap(tag,getEditText));
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        textView.setText(data);
        progressBar.setVisibility(View.INVISIBLE);
        if(loader.getId()==2)readLoader();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
