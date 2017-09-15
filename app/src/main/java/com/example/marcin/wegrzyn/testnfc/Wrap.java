package com.example.marcin.wegrzyn.testnfc;

import android.nfc.Tag;

/**
 * Created by Marcin on 16.07.2017 :)
 */

 class Wrap {
    private Tag tag;
    private String data;

    public Wrap(Tag tag, String data) {
        this.tag = tag;
        this.data = data;
    }

     Tag getTag() {
        return tag;
    }

     String getData() {
        return data;
    }


}
