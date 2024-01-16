package com.example.synthesizer;
import java.util.Random;


public class WhiteNoise implements AudioComponent {


    //Initializing variables
    //used to store frequency in the sine waves
    public double frequency_;
    double sampleRate = 44100;

    public WhiteNoise(double freq){
        this.frequency_ = freq;

    }

    @Override
    public AudioClip getClip() {

        //creating an audio clip to store the generated white noise
        AudioClip white = new AudioClip();

        //maximum amplitude for a short
        int maxValue = Short.MAX_VALUE;

        Random randNum = new Random();



        //looping through each sample in an audioclip
        for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {

            //int rand1 picks a random number in the range short min value to short.max_value
            int rand1 = randNum.nextInt(Short.MIN_VALUE, Short.MAX_VALUE);



            white.setSample(i, rand1);

        }
        return white;
    }

    //white noise does not have inputs
    @Override
    public boolean hasInput() {
        return false;
    }

    //no input for whitenoise
    @Override
    public void connectInput(AudioComponent input) {

    }


    public void updateWhiteFrequency(int freq){
        frequency_ = freq;
    }

}
