package com.example.synthesizer;

public class VFSineWave implements AudioComponent{
    private double phase_ = 0;

    private AudioComponent input_;

    @Override
    public AudioClip getClip() {
        int maxValue = Short.MAX_VALUE;

        AudioClip audioClip = new AudioClip();
        AudioClip inputClip = input_.getClip();

        //looping through each sample in an audioclip
        for (int i = 0; i < audioClip.TOTAL_SAMPLES; i++) {
            //calculating waveform value of the sample
            phase_ += 2 * Math.PI * inputClip.getSample(i) / audioClip._sampleRate;
            int sampleValue = (int) (maxValue * Math.sin(phase_));

            audioClip.setSample(i, sampleValue);

        }
        return audioClip;
    }

    @Override
    public boolean hasInput() {
        return true;
    }

    //connect an audioComponent as an input for VFSineWave
    @Override
    public void connectInput(AudioComponent input) {
        input_ = input;
    }
}
