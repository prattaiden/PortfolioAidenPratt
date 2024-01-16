package com.example.synthesizer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import static com.example.synthesizer.VolumeAdjuster.Vscale;


public class SynthesizeApplication extends Application {

    //array lists of widgets and connected to the mixer speaker widgets
   public static ArrayList<AudioComponentWidgetBase> widgets_ = new ArrayList<>();
    public static ArrayList<AudioComponentWidgetBase> connected_widgets_ = new ArrayList<>();


    AnchorPane anchorMain = new AnchorPane();

    public static VolumeAdjuster changeVolume;

    public static VolumeAdjusterWidget VAWidget;

    public static Circle speaker_;

    @Override
    public void start(Stage stage) throws IOException {


        //--------------IMPORTANT FOR MAINLAYOUT-------------------
        //--------------------------BORDER--------------------------
        BorderPane main = new BorderPane();

        //-------------------------LEFT PANEL---------------------

        VBox leftpanel = new VBox();
       leftpanel.setStyle("-fx-background-color: #a3a3ff");
        leftpanel.setSpacing(30);
        leftpanel.setPadding(new Insets(5, 5, 5, 5));

        //Square widget button
        Button squarewaveBTN = new Button("SQAURE");
        leftpanel.getChildren().add(squarewaveBTN);

        //Sine widget button
        Button sinewaveBTN = new Button("SINEWAVE");
        leftpanel.getChildren().add(sinewaveBTN);

        //White noise widget button
        Button whiteBTN = new Button("WHITENOISE");
        leftpanel.getChildren().add(whiteBTN);

        //mixer buttonu
//        Button mixBTN = new Button("mix");
//        leftpanel.getChildren().add(mixBTN);

        //SETTING IT TO THE LEFT
        main.setLeft(leftpanel);


        //---------------------------BOTTOM PANEL---------------------------
        HBox botPanel = new HBox();
        botPanel.setStyle("-fx-background-color: #A3A3FFFF");
        //PLAY BUTTON with border pain
        Button playButton = new Button("PLAY");
        //ADDING PLAYBUTTON TO HBOX MENU
        botPanel.getChildren().add(playButton);
        botPanel.setAlignment(Pos.CENTER);
        main.setBottom(botPanel);

       // ---------------------------CIRCLE----------------------------------
        speaker_ = new Circle(400, 200 , 15);
        anchorMain.getChildren().add(speaker_);
        speaker_.setFill(Color.rgb(0, 0, 0));
        speaker_.relocate(760, 330);
        //COLoR?
        main.setCenter(anchorMain);

        //-----------------------------Constant VOLUME----------------------
        //making volume widget appear
        createWidget(Components.VOLUME);

        //----------------------SCENE FOR BORDER PANE-----------------------------
        Scene scene = new Scene(main, 1000, 800);
        stage.setTitle("Synthesizer");
        stage.setScene(scene);
        stage.show();

        //--------------------buttons to open the widget---------------------------
        squarewaveBTN.setOnAction(e -> {
            createWidget(Components.SQAURE_WAVE);
        });

        sinewaveBTN.setOnAction(e -> {
            createWidget(Components.SINE_WAVE);
        });

        whiteBTN.setOnAction(e -> {
            createWidget(Components.WHITE_NOISE);
        });

//        mixBTN.setOnAction(e -> {
//            createWidget(Components.MIXER);
//    });

        //setting an action, calling a function "handlePLayPress"
        playButton.setOnAction(e-> {
            try {
                handlePlayPress();
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    //------------------------METHODS----------------------------------
    private void createWidget(Components components) {
        AudioComponent audioComponent = null;
        if (components.equals(Components.SINE_WAVE)) {
            audioComponent = new SineWave(450);
            SineWaveWidget sinewidg = new SineWaveWidget(audioComponent, anchorMain);
            anchorMain.getChildren().add(sinewidg);
            sinewidg.relocate(100, 250);
            widgets_.add(sinewidg);
        } else if (components.equals(Components.SQAURE_WAVE)) {
            audioComponent = new SquareWave(450);
            SquareWaveWidget squarewidg = new SquareWaveWidget(audioComponent, anchorMain);
            anchorMain.getChildren().add(squarewidg);
            squarewidg.relocate(100, 100);
            widgets_.add(squarewidg);
        } else if (components.equals(Components.VOLUME)) {
            VAWidget = new VolumeAdjusterWidget(changeVolume, anchorMain);
            anchorMain.getChildren().add(VAWidget);
            VAWidget.relocate(690,620);
        } else if (components.equals(Components.WHITE_NOISE)) {
            audioComponent = new WhiteNoise(450);
            WhiteNoiseWidget whiteWidg = new WhiteNoiseWidget(audioComponent, anchorMain);
            anchorMain.getChildren().add(whiteWidg);
            whiteWidg.relocate(100, 400);
            widgets_.add(whiteWidg);
        } else if (components.equals(Components.MIXER)){
            audioComponent = new Mixer();
            MixerWidget mixWidg = new MixerWidget(audioComponent, anchorMain);
            anchorMain.getChildren().add(mixWidg);
            mixWidg.relocate(400, 200);
            widgets_.add(mixWidg);
        }

    }

    //updated to not need text box, using freqSlider function
    private void handlePlayPress() throws LineUnavailableException {

            //SETTING CLIP C AND THE DEFAULT AUDIOFORMAT
            Clip c = AudioSystem.getClip();
            AudioFormat format16 = new AudioFormat(44100, 16, 1, true, false);

            //ARRAY LIST OF BYTES FROM THE WIDGET
            //this is where it is getting the widget
        System.out.println("# widgets" + widgets_.size());

        //MAKES THE SPEAKER THE MIXER
        Mixer mixer = new Mixer();
        VolumeAdjuster volumeAdjuster = new VolumeAdjuster(10);
        for (AudioComponentWidgetBase w : connected_widgets_){
             // if(!(w.ac_ instanceof  VolumeAdjuster)){
                  AudioComponent ac = w.ac_;
                  mixer.connectInput(ac);
              //}
        }

        double volumeScale = VAWidget.volSlider_.getValue();

        VolumeAdjuster volumeAdjuster1 = new VolumeAdjuster(volumeScale);
        volumeAdjuster1.connectInput(mixer);


        volumeAdjuster.connectInput(mixer);

        System.out.println(volumeAdjuster.toString());
        AudioClip clip = volumeAdjuster.getClip();

            AudioListener listener = new AudioListener(c);

            c.open(format16, clip.getData(), 0, clip.getData().length); // Reads data from our byte array to play it.

            c.start(); // Plays it.

            c.addLineListener(listener);
    }


    public static void main(String[] args) {
        launch();
    }
}