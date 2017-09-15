package com.example.marcin.wegrzyn.testnfc;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Marcin on 16.07.2017 :)
 */

public class WriteLoader extends AsyncTaskLoader{

    Wrap wrap;
    public WriteLoader(Context context,Wrap wrap) {
            super(context);

        this.wrap = wrap;
    }

    @Override
    public Object loadInBackground() {
        NfcUtils.writePage(wrap.getTag(),wrap.getData());
        return null;
    }
}
