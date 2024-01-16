package com.example.synthesizer;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;


public class Main {
//    public static void main(String[] args) throws LineUnavailableException {
//        // Get properties from the system about samples rates, etc.
//        // AudioSystem is a class from the Java standard library.
//        Clip c = AudioSystem.getClip(); // Note, this is different from our AudioClip class.
//
//// This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
//        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );
//
//
//
//        //=======================GENERATING WAVES================================
//        //making a new sinewave of type audiocomponent
//        AudioComponent gen = new SineWave(2000); // Your code
//        AudioComponent gen2 = new SineWave(100);
//        AudioComponent gen3 = new SineWave(400);
//
//
//
//        //---------------MIXER WAVES TEST -----------------------
//        Mixer mixewaves = new Mixer();
//        mixewaves.connectInput(gen2);
//        mixewaves.connectInput(gen3);
//
//        //---------------SQUARE WAVES TEST ----------------------
//        SquareWave squarewave = new SquareWave(300);
//
//        //---------------WHITE NOISE TEST------------------------
//        WhiteNoise white = new WhiteNoise(0);
//
//        //--------------------VOLUME ADJUSTER----------------------
//        VolumeAdjuster lowerVolume = new VolumeAdjuster( .3);
//        lowerVolume.connectInput(mixewaves);
//
//        //----------------LINEAR RAMP TEST and VFSINEWAVE ------------
//        //create VFSineWave
//        VFSineWave vfSineWave = new VFSineWave();
//        //create a linear ramp
//        LinearRamp linearRamp = new LinearRamp(50, 2000);
//        //connect the Linear Ramp as an input to the VFSineWave
//        vfSineWave.connectInput(linearRamp);
//        //get the audio clip
//
//
//
//        //===========================PLAY AUDIO CLIP===============================/
//        //AudioClip clip = gen.getClip();
//        //AudioClip clip = mixewaves.getClip();
//        AudioClip clip = lowerVolume.getClip();
//        //AudioClip clip = squarewave.getClip();
//        //AudioClip clip = white.getClip();
//        //AudioClip clip = vfSineWave.getClip();
//
//
//
//        c.open( format16, clip.getData(), 0, clip.getData().length ); // Reads data from our byte array to play it.
//
//        System.out.println( "About to play..." );
//        c.start(); // Plays it.
//        c.loop( 2 ); // Plays it 2 more times if desired, so 6 seconds total
//
//// Makes sure the program doesn't quit before the sound plays.
//        while( c.getFramePosition() < AudioClip.TOTAL_SAMPLES || c.isActive() || c.isRunning() ){
//            // Do nothing while we wait for the note to play.
//        }
//
//        System.out.println( "Done." );
//        c.close();
//
//
//
//    }

}