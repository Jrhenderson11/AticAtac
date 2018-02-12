package com.aticatac.sound;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;


public class SoundManager{


	public void playClick() {	
		//https://opengameart.org/content/menu-selection-click
		String musicFile = "assets/music/click.wav";

		Media sound = new Media(new File(musicFile).toURI().toString());
		
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	
	public void playShoot(){
		String file = "assets/music/shoot.wav";
		Media sound = new Media(new File(file).toURI().toString());
		
		MediaPlayer player = new MediaPlayer(sound);
	}


	
	
	
}
