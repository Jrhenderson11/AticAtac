package com.aticatac.ui.settings;

import com.aticatac.sound.SoundManager;
import com.aticatac.utils.SystemSettings;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Settings extends Scene{
	private static int menuVol = 50, shootVol = 50, battleVol = 50;
	private static int bgChoice = 1;
	
	public Settings(Group root, Scene mainMenu, Stage primaryStage){
		super(root);
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        Label title = new Label("Settings");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setLayoutX(width/2);
       Slider sMenu, sBattle, sShoot;
       
       sMenu = makeSlider(400, 100, "menu");
       sBattle = makeSlider(400, 150, "battle");
       sShoot = makeSlider(400, 200, "shoot");
        
       Label lblMenu = new Label("Menu Volume:");
       lblMenu.setTranslateX(100);
       lblMenu.setTranslateY(100);
       
       Label lblBattle = new Label("Battle Volume:");
       lblBattle.setTranslateX(100);
       lblBattle.setTranslateY(150);
       
       Label lblShoot = new Label("Shoot Volume:");
       lblShoot.setTranslateX(100);
       lblShoot.setTranslateY(200);
       
       Label lblBg = new Label("Change battle song:");
       lblBg.setTranslateX(100);
       lblBg.setTranslateY(275);
       ToggleGroup group = new ToggleGroup();

       RadioButton btn1 = new RadioButton("Hesitation");
       btn1.setLayoutX(100);
       btn1.setLayoutY(300);
       btn1.setToggleGroup(group);

       RadioButton btn2 = new RadioButton("Fighting is not an option.");
       btn2.setToggleGroup(group);
       btn2.setLayoutX(100);
       btn2.setLayoutY(325);
        
       RadioButton btn3 = new RadioButton("Chiptune Techno");
       btn3.setToggleGroup(group);
       btn3.setLayoutX(100);
       btn3.setLayoutY(350);
       
       RadioButton btn4 = new RadioButton("8 bit battle theme");
       btn4.setToggleGroup(group);
       btn4.setLayoutX(100);
       btn4.setLayoutY(375);
       
       if (bgChoice == 1)
    	   btn1.setSelected(true);
       else if (bgChoice == 2)
    	   btn2.setSelected(true);
       else if (bgChoice == 3)
    	   btn3.setSelected(true);
       else if (bgChoice == 4)
    	   btn4.setSelected(true);   
           
       
       SoundManager m = new SoundManager();
       
       group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
    	      public void changed(ObservableValue<? extends Toggle> ov,
    	          Toggle old_toggle, Toggle new_toggle) {
    	        if (group.getSelectedToggle() != null) {
    	        	Toggle selected = group.getSelectedToggle();
    	          if (selected == btn1){
    	        	  bgChoice = 1;
    	          }else if(selected == btn2){
    	        	  bgChoice = 2;
    	          }else if (selected == btn3){
    	        	  bgChoice = 3;
    	          }else if (selected == btn4){
    	        	  bgChoice = 4;
    	          }
    	          m.setBgChoice(bgChoice);
    	          
    	        	
    	        }
    	      }
    	    });
       
       root.getChildren().add(canvas); 
       root.getChildren().add(title);
       root.getChildren().add(sMenu);
       root.getChildren().add(sBattle);
       root.getChildren().add(sShoot);
       root.getChildren().add(lblMenu);
       root.getChildren().add(lblBattle);
       root.getChildren().add(lblShoot);
       root.getChildren().add(btn1);
       root.getChildren().add(btn2);
       root.getChildren().add(btn3);
       root.getChildren().add(btn4); 
       root.getChildren().add(lblBg);
        
	}
	
	public Slider makeSlider(int x, int y, String type){
        SoundManager m = new SoundManager();

		 Slider slider = new Slider();
	        slider.setMin(-10);
	        slider.setMax(6);
	        slider.setValue(-2);
	        slider.setMinorTickCount(5);
	        slider.setShowTickLabels(false);
	        slider.setShowTickMarks(true);
	        slider.setSnapToTicks(true);
	        
	        slider.setLayoutX(x);
	        slider.setLayoutY(y);
	        
	        slider.valueProperty().addListener(
	                (observable, oldvalue, newvalue) ->
	                {
	                    int currentValue = newvalue.intValue();
	                    System.out.println(currentValue);
	                    if (type.equals("menu"))
	                    		m.setMenuVolume(currentValue);
	                    else if (type.equals("battle"))
	                    		m.setBattleVolume(currentValue);
	                    else if (type.equals("shoot"))
	                    		m.setShootVolume(currentValue);
	                } );
	        return slider;
		
	}
	
	
}
