package com.example.synthesizer;

import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

//class to create object of this class
public class AudioComponentWidgetBase extends Pane {


    Circle mixerSpeaker_;
    AudioComponent ac_;
    AnchorPane parent_;

    //member so that it can be used in the other widgets
    VBox sliderPanel_;

    HBox dragPanel_;

    HBox baseLayout_;

    double mouseXPos, mouseYPos, widgetXPos, widgetYPos;


    Line line_;

    public AudioComponentWidgetBase(AudioComponent ac, AnchorPane parent) {
        System.out.println("adding ac in constructor: " + ac_);
        ac_ = ac;
        parent_ = parent;


        //------------------------WHOLE THING--------------------------------
        baseLayout_ = new HBox();
        baseLayout_.setStyle("-fx-border-color: #000000; -fx-background-color: #7c7979");
        baseLayout_.setSpacing(10);
        baseLayout_.setPadding(new Insets(15));

        //------------DRAG BOX-----------------------------------
        dragPanel_ = new HBox();
        dragPanel_.setStyle("-fx-background-color: #8cea7a; -fx-border-color: black");
        dragPanel_.setPadding(new Insets(5));
        Label drag = new Label("[]");
        dragPanel_.getChildren().add(drag);


        //-----------------MIDDLE HBOX TO TAKE VBOX----------------
        HBox middle = new HBox();
        middle.setStyle("-fx-background-color: black");



        //------------------------BOX FOR SLIDER-------------------------
        sliderPanel_ = new VBox();
        sliderPanel_.setStyle("-fx-background-color: #e86945; -fx-border-color: black");
        sliderPanel_.setPadding(new Insets(10));
        sliderPanel_.setSpacing(5);


        //SETTING THEM TO THE BASE WIDGET------------------------------
        middle.getChildren().add(sliderPanel_);
        baseLayout_.getChildren().add(middle);
       // baseLayout_.getChildren().add(closeInput);

        //to move it
        dragPanel_.setOnMousePressed(e->getPosInfo(e));
        dragPanel_.setOnMouseDragged(e->dragWidget(e));

        //moving the baselout to the main panel"this"
        this.getChildren().add(baseLayout_);
    }

    void endConnection(MouseEvent e, Circle output) {

        //get the speaker from the main application
        Circle speaker = SynthesizeApplication.speaker_;
        Bounds speakerBounds = speaker.localToScene(speaker.getBoundsInLocal());
        double distance;
        distance = Math.sqrt(Math.pow(speakerBounds.getCenterX() - e.getSceneX(), 2.0));
                Math.pow(speakerBounds.getCenterY() - e.getSceneY(),2.0);
        System.out.println("before check if should add connected widgets");

        if(distance<15){
            SynthesizeApplication.connected_widgets_.add(this); //will add the connected to the other opened widgets
            //better to create a new array list for connected widgets only
            System.out.println("added connected widgets:" + this);
        }
        else{
            System.out.println("remove this line");
            parent_.getChildren().remove(line_);
            line_ = null;
        }

    }

   void moveConnection(MouseEvent e, Circle output) {
        Bounds parentBounds = parent_.getBoundsInParent();
        line_.setEndX(e.getSceneX() -parentBounds.getMinX());
        line_.setEndY(e.getSceneY() -parentBounds.getMinY());

    }

  void startConnection(MouseEvent e, Circle output) {

        if (line_!=null){
            parent_.getChildren().remove(line_);
        }
        Bounds parentBounds = parent_.getBoundsInParent();
        Bounds bounds = output.localToScene(output.getBoundsInLocal());

        line_ = new Line();
        line_.setStrokeWidth(5);
        line_.setStartX(bounds.getCenterX() - parentBounds.getMinX());
        line_.setStartY(bounds.getCenterY()- parentBounds.getMinY());
        line_.setEndX(e.getSceneX());
        line_.setEndY(e.getSceneY());

        parent_.getChildren().add(line_);

    }

  void dragWidget(MouseEvent e) {
        double deltaX = e.getSceneX() - mouseXPos;
        double deltaY = e.getSceneY() - mouseYPos;
        this.relocate(deltaX + widgetXPos, deltaY + widgetYPos);
        //trying to get the line to move with the widget
        line_.setStartX(deltaX + widgetXPos + 210);
        line_.setStartY(deltaY + widgetYPos + 70);
    }

   void getPosInfo(MouseEvent e) {
        mouseXPos = e.getSceneX();
        mouseYPos = e.getSceneY();
        widgetXPos = this.getLayoutX();
        widgetYPos = this.getLayoutY();

    }


    void closeWidget(ActionEvent e) {
        //removing from parent
        parent_.getChildren().remove(this);
        //removing from arraylist of widgets
        SynthesizeApplication.widgets_.remove(this);
        SynthesizeApplication.connected_widgets_.remove(this);
        parent_.getChildren().remove(line_);
    }

    public AudioComponent getAudioComponent(){
        return ac_;
    }

    //handle drawing the line

}





