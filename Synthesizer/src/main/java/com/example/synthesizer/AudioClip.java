package com.example.synthesizer;

import java.util.Arrays;

public class AudioClip {

    //duration is twop seconds
    static public double _duration = 2.0;

    //sample rate is 44100 samples per second
    static public int _sampleRate = 44100;

    //member variable that contains the array of bytes
    public byte[] audioClipArray;

    //Define the total number of samples
    public static final int TOTAL_SAMPLES = ((int) (_duration * _sampleRate));


    //AUDIOCLIP constructor which initializes the byte array
    public AudioClip(){
        //*2 because 2 bytes per sample, array is total number of bytes?
        audioClipArray = new byte[(int) ((TOTAL_SAMPLES) * 2)];

    }

    //---------------METHOD INT GETSAMPLE------------------
    //returns the sample at index as an int
    public int getSample (int index){
        //setting location of each byte
        byte b1 = audioClipArray[index*2];
        byte b2 = audioClipArray[index*2 +1];
        //return type intitialization as integer
        int result = 0;

        //compare with bitwise manipulation (b1-0) | (0-b2) = (b1-b2) as a short
        result = b2<<8 | (b1 & 0xFF);
        return result;

    }

    //---------------METHOD SET SAMPLE--------------------
    public void setSample (int index, int value){
        audioClipArray[index * 2] = (byte) value;
        audioClipArray[index * 2 + 1] = (byte) (value >> 8);

    }
    //---------------METHOD GET DATA-----------------------
    //returns a copy of the array
    public byte[] getData(){
        return Arrays.copyOf(audioClipArray, (int) (_duration * _sampleRate * 2));
    }

};


