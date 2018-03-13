package com.aticatac.ui.settings;

import java.util.ArrayList;

import com.aticatac.sound.SoundManager;
import com.aticatac.utils.SystemSettings;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Settings extends Scene{
	private static int menuVol = 50, shootVol = 50, battleVol = 50;
	private static int bgChoice = 1;
	private static ArrayList<Label> labels = new ArrayList<Label>();
	private static ArrayList<RadioButton> radBtn = new ArrayList<RadioButton>();
	private boolean resizing;
	
	public Settings(Group root, Scene mainMenu, Stage primaryStage){
		super(root);
        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(width, height);
        
        resizing = false;
        labels.clear();
        radBtn.clear();
        
       Label lblMenu = new Label("Menu Volume:");
       labels.add(lblMenu);
       
       Label lblBattle = new Label("Battle Volume:");
       labels.add(lblBattle);
       
       Label lblShoot = new Label("Shoot Volume:");
       labels.add(lblShoot);
       
       Label lblBg = new Label("Change battle song:");
       lblBg.setTextFill(Color.WHITE);

       ToggleGroup group = new ToggleGroup();

       RadioButton btn1 = new RadioButton("Hesitation");
       radBtn.add(btn1);
       btn1.setToggleGroup(group);

       RadioButton btn2 = new RadioButton("Fighting is not an option.");
       btn2.setToggleGroup(group);
       radBtn.add(btn2);
        
       RadioButton btn3 = new RadioButton("Chiptune Techno");
       btn3.setToggleGroup(group);
       radBtn.add(btn3);
       
       RadioButton btn4 = new RadioButton("8 bit battle theme");
       btn4.setToggleGroup(group);
       radBtn.add(btn4);
       
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
       
       GraphicsContext gc = canvas.getGraphicsContext2D();
       SettingsDrawer animation = new SettingsDrawer(gc,System.nanoTime());
       animation.start();
       
       int xLine = (int) (width*0.15);
       int yLine = (int) (height*0.20);
       animation.drawLabel(labels, xLine, yLine, 50);
       animation.drawRadBtn(radBtn, xLine, yLine*3, 25);
       
       lblBg.setLayoutX(xLine);
       lblBg.setLayoutY(yLine*3 - 25);
       
       Slider sMenu, sBattle, sShoot;
       
       int xLine2 = (int) xLine*4;
       sMenu = makeSlider(xLine2, yLine, "menu");
       sBattle = makeSlider(xLine2, yLine + 50, "battle");
       sShoot = makeSlider(xLine2, yLine + 100, "shoot");
       

       Button resize = new Button();
       resize.setText("Click to resize");
       resize.setLayoutX(xLine*4);
       resize.setLayoutY(yLine*3 - 25);
       
       Button back = new Button();
       back.setText("Back");
       back.setLayoutX(xLine*4);
       back.setLayoutY(yLine*3 + 25);
       
       resize.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override 
    	    public void handle(ActionEvent e) {
    	        if (resizing) {
    	        	resizing = false;
    	        	resize.setText("Click to resize");
    	        	primaryStage.setResizable(false);
    	        	canvas.setWidth(SystemSettings.getScreenWidth());
    	            canvas.setHeight(SystemSettings.getScreenHeight());
    	            animation.setGc(canvas.getGraphicsContext2D());
    	            reposition(animation, lblBg, sMenu, sBattle, sShoot, resize, back);
    	        } else {
    	        	resizing = true;
    	        	primaryStage.setResizable(true);
    	        	resize.setText("Drag window to resize, click when done");
    	        }
    	        m.muteAll();
    	    }
    	});
       back.setOnAction(new EventHandler<ActionEvent>() {
   	    	@Override 
   	    	public void handle(ActionEvent e) {
   	    		primaryStage.setResizable(false);
   	    		primaryStage.setScene(mainMenu);
   	    		m.muteAll();
    	        m.playBgMenu();
   	    	}
   		});
       
       setOnKeyPressed(new EventHandler<KeyEvent>() {
 			public void handle(KeyEvent e) {
 				KeyCode code = e.getCode();
 				if (code == KeyCode.ESCAPE) {
 					
 				}
 			}
 	    });
       
       root.getChildren().add(canvas); 
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
       root.getChildren().add(resize);
       root.getChildren().add(back);
        
	}
	
	public void reposition(SettingsDrawer animation, Label lblBg, Slider sMenu, Slider sBattle, Slider sShoot, Button resize, Button back) {
	   int xLine = (int) (SystemSettings.getScreenWidth()*0.15);
       int yLine = (int) (SystemSettings.getScreenHeight()*0.20);
       
       
       lblBg.setLayoutX(xLine);
       lblBg.setLayoutY(yLine*3 - 25);
       
       int xLine2 = (int) xLine*4;
       sMenu.setLayoutX(xLine2);
       sMenu.setLayoutY(yLine);
       sBattle.setLayoutX(xLine2);
       sBattle.setLayoutY(yLine + 50);
       sShoot.setLayoutX(xLine2);
       sShoot.setLayoutY(yLine + 100);

       resize.setLayoutX(xLine*4);
       resize.setLayoutY(yLine*3 - 25);
       back.setLayoutX(xLine*4);
       back.setLayoutY(yLine*3 + 25);
       animation.drawLabel(labels, xLine, yLine, 50);
       animation.drawRadBtn(radBtn, xLine, yLine*3, 25);
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
