package com.example.synthesizer;

public class SquareWave implements AudioComponent {

    //Initializing variables
    //used to store frequency in the sine waves
    public double frequency_;
    double sampleRate = 44100;

   public SquareWave(double freq){
       this.frequency_ = freq;

   }

    @Override
    public AudioClip getClip() {

        System.out.println("Squarew get clip");
       //creating an audio clip to store the generated square wave
       AudioClip SqaureClip = new AudioClip();

       //maximum amplitude for a short
       int maxValue = Short.MAX_VALUE;

       //int for the sample of the sound
       int sample = 0;
        //looping through each sample in an audioclip
       for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
           //calculating waveform value of the sample
           if ((frequency_ * i / sampleRate) % 1 > 0.5) {
               sample = maxValue;
           }
            else{
               sample = -maxValue;
           }

            SqaureClip.setSample(i, sample);

       }

       return SqaureClip;
    }

    //square wave does not have inputs
    @Override
    public boolean hasInput() {
        return false;
    }

    //no input for square wave
    @Override
    public void connectInput(AudioComponent input) {


    }

    public void updateSquareFrequency(int freq){
       frequency_ = freq;
    }
}



