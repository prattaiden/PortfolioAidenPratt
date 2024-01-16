package com.example.synthesizer;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class VolumeAdjusterWidget extends AudioComponentWidgetBase {

    Slider volSlider_;
    public VolumeAdjusterWidget(AudioComponent ac, AnchorPane parent) {

        super(ac, parent);

        System.out.println("VolumeAdjusterWidget adding ac in constructor: " + ac);

        //WIDGETLABEL
        Label titlelabel = new Label("VOLUME");
        sliderPanel_.getChildren().add(titlelabel);

        //SLIDER
        volSlider_ = new Slider(0, 10, 3);
        Label freqLabel = new Label("Volume: 3");
        volSlider_.setOnMouseDragged(e -> handleVolSlider(e, volSlider_, freqLabel));
        sliderPanel_.getChildren().add(freqLabel);
        sliderPanel_.getChildren().add(volSlider_);

    }

        private void handleVolSlider(MouseEvent e, Slider volSlider, Label vSlider){
            AudioComponent ac1 = getAudioComponent();
            int result = (int) volSlider.getValue();
            vSlider.setText("Volume: " + result);
            ((VolumeAdjuster) ac1).getVolumeScale(result);


        }

}


