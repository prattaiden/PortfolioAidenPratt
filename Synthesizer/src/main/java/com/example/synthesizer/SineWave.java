package com.example.synthesizer;

public class SineWave implements AudioComponent {

    //Initializing variles
    //used to store frequency in the sine waves
    public double frequency_;
    double sampleRate = 44100;

    //Constructor  for sine wave
    public SineWave(double freq){
        this.frequency_=freq;

    }

    //method creates and fills in an AudioClip with sample values from SINE WAVE
    public AudioClip getClip(){
        //new AudioCLip is created called clipTest
        //will be used to store generated sine wave

        System.out.println("Sinew get clip");
        AudioClip clipTest = new AudioClip();
        //setting the result
        //set array of bytes
        for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++){
            //fill the array with the formula
            //calculate half the length of the audio data array
            //each sample is 2 byes , 16 bits
            int result = (int) (Short.MAX_VALUE * Math.sin(2 * Math.PI * frequency_ * i / sampleRate));
            //call set sample method and store the result at the current position in the audio array
            clipTest.setSample(i, result);
        }
        //returning clipTest after
        return clipTest;
    }
        //sample[ i ] = maxValue * sine( 2*pi*frequency * i / sampleRate );


    @Override
    public boolean hasInput(){
        return false;
    }

    public void connectInput(AudioComponent input){

        //if my array has input, then set has input to true
    }
    public void updateSineFrequency(int freq){

        frequency_ = freq;
    }
}



