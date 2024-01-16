package com.example.synthesizer;

public class LinearRamp implements AudioComponent{

    private final double start_;

    private final double stop_;

    public LinearRamp(double start, double stop){
        start_ = start;
        stop_ = stop;
    }
    @Override
    public AudioClip getClip() {
       AudioClip linearClip = new AudioClip();

       int numSamples = linearClip.TOTAL_SAMPLES;

       for (int i = 0; i < numSamples; i++){
           double sampleValues = (start_ * (numSamples - i) + stop_ * i) /numSamples;
           linearClip.setSample(i, (int) sampleValues);
       }

       return linearClip;

    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {

    }
}
