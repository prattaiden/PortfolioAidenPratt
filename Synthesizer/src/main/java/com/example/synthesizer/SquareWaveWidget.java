package com.example.synthesizer;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SquareWaveWidget extends AudioComponentWidgetBase {
    public SquareWaveWidget(AudioComponent ac, AnchorPane parent) {
        super(ac, parent);

        //WIDGETLABEL
        Label titlelabel = new Label("SQAURE WAVE");
        sliderPanel_.getChildren().add(titlelabel);

        //SLIDER
        Slider squareFreqSlider = new Slider(100, 800, 450);
        Label freqLabel = new Label("Frequency: 450");
        squareFreqSlider.setOnMouseDragged(e->handleFrequencySlider(e, squareFreqSlider, freqLabel));
        sliderPanel_.getChildren().add(freqLabel);
        sliderPanel_.getChildren().add(squareFreqSlider);

        //-----------------VBOX FOR SPEAKER AND X------------------------
        VBox closeInput = new VBox();

        closeInput.setStyle("-fx-background-color: #ffffff; -fx-border-color: black");

        Button Closebtn = new Button("X");
        Closebtn.setOnAction(e -> closeWidget(e));
        Closebtn.setPadding(new Insets(6));

        Circle outputSpeaker = new Circle(100, 100, 10);

        outputSpeaker.setFill(Color.rgb(0, 0, 0));

        closeInput.getChildren().add(Closebtn);
        closeInput.getChildren().add(outputSpeaker);
        closeInput.getChildren().add(dragPanel_);
        closeInput.setSpacing(5);
        closeInput.setPadding(new Insets(10));


        //HANDLE OUTPUT
        outputSpeaker.setOnMousePressed(e-> startConnection(e, outputSpeaker));
        outputSpeaker.setOnMouseDragged(e-> moveConnection (e, outputSpeaker));
        outputSpeaker.setOnMouseReleased(e-> endConnection(e, outputSpeaker));

        baseLayout_.getChildren().add(closeInput);


    }

    private void handleFrequencySlider(MouseEvent e, Slider freqSlider, Label Flabel) {
        AudioComponent ac1 = getAudioComponent();
        int result = (int)freqSlider.getValue();
        Flabel.setText("Frequency: " + result);
        ((SquareWave)ac1).updateSquareFrequency(result);

    }
}

