package com.example.synthesizer;

import java.util.ArrayList;

public class Mixer implements AudioComponent {

    private final ArrayList<AudioComponent> connectedInputs = new ArrayList<>();

    // Override the method to generate an AudioClip by combining input clips.
    @Override
    public AudioClip getClip() {
        // Create an AudioClip to hold the combined audio data.
        AudioClip addedClips = new AudioClip();

        // Iterate through each connected input (sine waves or waves).
        for (AudioComponent input : connectedInputs) {
            // Get the AudioClip from the input source.
            AudioClip tempClip = input.getClip();

            // Iterate through the samples in the AudioClip.
            // ***divide the total samples by 2 when combining multiple sound waves
            for (int i = 0; i < tempClip.TOTAL_SAMPLES / 2; i++) {
                // Retrieve the current sample value from the combined AudioClip.
                double tempData = addedClips.getSample(i);

                // Add the corresponding sample value from the input AudioClip.
                tempData += tempClip.getSample(i);

                // Ensure the sample value is within the valid range and set it in the combined AudioClip.
                addedClips.setSample(i, clampValue(tempData));
            }
        }

        // Return the combined AudioClip containing the mixed audio data.
        return addedClips;
    }

    // Override the method to check if this component has any connected input sources.
    @Override
    public boolean hasInput() {
        // Return true if there are connected input sources; otherwise, return false.
        return !connectedInputs.isEmpty();
    }

    // Override the method to connect an input source to this mixer.
    @Override
    public void connectInput(AudioComponent input) {
        // Add the provided input source to the list of connected inputs.
        connectedInputs.add(input);
    }

    // A helper method to clamp a sample value within the valid range for audio data.
    private int clampValue(double sample) {
        double max = Short.MAX_VALUE;
        double min = Short.MIN_VALUE;

        if (sample < min) {
            sample = min;
        } else if (sample > max) {
            sample = max;
        }

        // Convert the clamped sample value to an integer and return it.
        return (int) sample;
    }
}

/*
***You have two audio sources: Source A and Source B, each producing audio with a total of 4 samples (2 samples for the
   left channel and 2 samples for the right channel).

    Source A (stereo): [A_L1, A_R1, A_L2, A_R2]
    Source B (stereo): [B_L1, B_R1, B_L2, B_R2]

    When you combine these sources, you want to mix the left and right channels separately.

    Combined (stereo): [A_L1 + B_L1, A_R1 + B_R1, A_L2 + B_L2, A_R2 + B_R2]

    So, you must divide by two to make sure your handling both the left and right channels separately when combining
    audio sources. If you're working with mono audio sources, you wouldn't need to divide by two,
    as there would be only one channel to consider.
 */
//
//
//
//
//
//package com.example.synthesizer;
//
//import java.util.ArrayList;
//
//public class Mixer implements AudioComponent {
//
//    ArrayList<AudioClip> input_ = new ArrayList<AudioClip>();
//
//    @Override
//    public AudioClip getClip() {
//        AudioClip result = new AudioClip();
//        //loop through array of waves
//        for(AudioClip wave : input_){
//            //for each wave through the wave
//            for(int i = 0; i < AudioClip.TOTAL_SAMPLES; i++){
//                //Add sample to result audio-clip (to add waves you add corresponding amplitudes)
//                int sample = (int)(result.getSample(i) + wave.getSample(i));
//                //Ensure the adjusted Sample value stays within the valid range (clamping the sound)
//                int max = Short.MAX_VALUE;
//                int min = Short.MIN_VALUE;
//
//                if(sample < min){
//                   sample = min;
//                }
//                else if(sample > max){
//                    sample = max;
//                }
//                result.setSample(i, sample);
//            }
//        }
//    return result;
//    }
//
//    @Override
//    public boolean hasInput() {
//        return true;
//    }
//
//    @Override
//    public void connectInput(AudioComponent input) {
//
//        VolumeAdjuster lowerVolume = new VolumeAdjuster(.5);
//        lowerVolume.connectInput(input);
//
//        input_.add(lowerVolume.getClip());
//
//    }
//}
