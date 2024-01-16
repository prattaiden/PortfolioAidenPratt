package com.example.synthesizer;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class MixerWidget extends AudioComponentWidgetBase{
    public MixerWidget(AudioComponent ac, AnchorPane parent) {
        super(ac, parent);

        //WIDGETLABEL
        Label titlelabel = new Label("MIXER");
        sliderPanel_.getChildren().add(titlelabel);

        //-----------------VBOX FOR SPEAKER AND X------------------------
        VBox closeInput = new VBox();

        closeInput.setStyle("-fx-background-color: #ffffff; -fx-border-color: black");

        Button Closebtn = new Button("X");
        Closebtn.setOnAction(e -> closeWidget(e));
        Closebtn.setPadding(new Insets(6));

        Circle outputSpeaker = new Circle(100, 100, 10);
        outputSpeaker.setFill(Color.rgb(0, 0, 0));

        mixerSpeaker_ = new Circle(100, 100, 10);
        mixerSpeaker_.setFill(Color.rgb(200, 2, 100));

        closeInput.getChildren().add(mixerSpeaker_);
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




}
