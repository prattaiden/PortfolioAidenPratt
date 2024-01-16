package com.example.synthesizer;

import com.example.synthesizer.AudioClip;
import com.example.synthesizer.AudioComponent;

public class VolumeAdjuster implements AudioComponent {

    private AudioComponent input_;
    public static double Vscale;

    //constructor
    public VolumeAdjuster(double scale) {
        Vscale = scale;
        //AudioClip adjustedSample = new AudioClip();
    }

    //getting a clip from the input and modifying it and return the updated clip
    @Override
    public AudioClip getClip() {

        //original input clip
        AudioClip original = input_.getClip();

        //new result clip that will be returned after modification
        AudioClip result = new AudioClip();//modification

        //loop through the total samples and modify the sound
        for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
            //get the sample value from the original clip
            int adjustedSample = (int) (Vscale * original.getSample(i));

            //if else to ensure adjustment stays within range
            int max = Short.MAX_VALUE;
            int min = Short.MIN_VALUE;

            if (adjustedSample < min) {
                adjustedSample = min;
            } else if (adjustedSample > max) {
                adjustedSample = max;
            }
            result.setSample(i, adjustedSample);

        }

        return result;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {
        System.out.println("volume adjuster connect input : " + input);
        input_ = input;

    }

    public void getVolumeScale(int vol){
        Vscale = vol;
}

}
