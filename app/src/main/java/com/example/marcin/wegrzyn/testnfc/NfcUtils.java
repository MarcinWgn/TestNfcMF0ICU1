package com.example.marcin.wegrzyn.testnfc;

import android.nfc.Tag;
import android.nfc.tech.NfcA;

import java.io.IOException;

/**
 * Created by Marcin on 15.07.2017 :)
 */

public class NfcUtils {

    static final byte NfcATagRead = 0x30;
    static final byte NfcATagWrite= (byte) 0xA2;


    static String readAllData(Tag tag){
        byte[] command = {NfcATagRead,0};
        String out ="";
        for (int i = 0 ; i<16; i++){
            command[1] = (byte) i;
            out += toHexStr((byte) i)+"h # "+readTag(tag,command)+"\n";
        }
        return out;
    }
    static boolean writePage(Tag tag, String dataToWrite){

        byte[]data = getStringHex(dataToWrite);
        byte[]command = new byte[data.length+1];
        command [0] = NfcATagWrite;
        System.arraycopy(data,0,command,1,data.length);
        writeTag(tag,command);
        return false;
    }

    private static byte [] getStringHex (String s){
        if(s!=null){
            if (s.length() % 2 != 0) {
                s = s.substring(0, s.length() - 1);
            }

            byte [] data = new byte[s.length()/2];
            int j = 0;

            for (int i = 0 ; i<s.length(); i=i+2){

                data[j]= (byte) Integer.parseInt(s.substring(i,i+2),16);
                j++;
            }
            return data;
        }else
            return null;
    }

    private static String readTag (Tag tag,byte[]command){
        NfcA nfcA = NfcA.get(tag);
        try {
            nfcA.connect();

            byte[] page = nfcA.transceive(command);
            return byteArrayToHexString(page);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (nfcA != null){
                try {
                    nfcA.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    private static void writeTag (Tag tag,byte[]command){
        NfcA nfcA = NfcA.get(tag);
        try {
            nfcA.connect();

            nfcA.transceive(command);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (nfcA != null){
                try {
                    nfcA.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String byteArrayToHexString(byte[] data){

        String out = "";

        for (int i =0  ; i<4 ; i++) {

            out += toHexStr(data[i]) + " ";
        }
        return out;
    }

    private static String toHexStr(byte data){
        return String.format("%02X", data);
    }



}
