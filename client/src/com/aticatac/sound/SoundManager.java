package com.aticatac.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager{
	
public static boolean debug = false;
public static Clip menuClip, battleClip;
	
	public void playClick() {	
		//https://opengameart.org/content/menu-selection-click
		File file = new File("assets/music/click.wav");
		Clip clip = play(file);
		clip.start();
	}
	
	public void playShoot(){
		//https://opengameart.org/content/flatshot-complete-sfx-pack
		File file = new File("assets/music/shoot.wav");
		Clip clip = play(file);
		clip.start();
	}

	public void playBgMenu(){
		try {
	    	//https://opengameart.org/content/halloween-rocknroll-music-loop	
			File file = new File("./assets/music/menubg.wav");
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			
			Clip clip = AudioSystem.getClip();
			if (!debug){
				System.out.println("clip is started");
				clip.open(in);
				clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				menuClip = clip;
			}
			
			
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void stopMenuBg(){
		if(menuClip.isRunning()){
			menuClip.close();
		}
	}
	
	
	public void playBgGame(){
		try {
			//https://opengameart.org/content/hesitation-synth-electronic-loop	
			File file = new File("./assets/music/hesitation.wav");
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			
			Clip clip = AudioSystem.getClip();
			if (!debug){
				clip.open(in);
				clip.start();
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				battleClip = clip;
			}
			
			
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void stopBattleBg(){
		if (battleClip.isRunning()){
			battleClip.stop();
		}
	}
	
	public Clip play(File file){
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			
			Clip clip = AudioSystem.getClip();
			clip.open(in);
			return clip;
			
			
			
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public MediaPlayer play(String url){

		Media sound = new Media(new File(url).toURI().toString());
		
		MediaPlayer mediaPlayer = new MediaPlayer(sound);

		return mediaPlayer;
	}

}
