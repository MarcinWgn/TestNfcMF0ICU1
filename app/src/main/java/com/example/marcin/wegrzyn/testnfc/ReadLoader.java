package com.example.marcin.wegrzyn.testnfc;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.nfc.Tag;

import static com.example.marcin.wegrzyn.testnfc.NfcUtils.readAllData;

/**
 * Created by Marcin on 16.07.2017 :)
 */

public class ReadLoader extends AsyncTaskLoader<String> {

    Tag tag;

    public ReadLoader(Context context, Tag tag) {
        super(context);
        this.tag = tag;
    }

    @Override
    public String loadInBackground() {
        return readAllData(tag);
    }
}
