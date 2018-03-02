package com.aticatac.sound;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager{
	
public static boolean debug = false;
public static Clip menuClip, battleClip;
private static float menuVol = 0, battleVol = 0;
private final static float max = 6, min = -10;
	
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
				FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				vol.setValue(menuVol);
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
		if(check(menuClip)){
			menuClip.close();
		}
	}
	
	
	public void playBgBattle(){
		try {
			//https://opengameart.org/content/hesitation-synth-electronic-loop	
			File file = new File("./assets/music/hesitation.wav");
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			
			Clip clip = AudioSystem.getClip();
			if (!debug){
				clip.open(in);
				FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				vol.setValue(battleVol);
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
		if (check(battleClip)){
			battleClip.close();
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
	
	//gain can be negative
	public void setMenuVolume(float gain){
		float gained = menuVol + gain;
		if (gained > max){
			menuVol = max;
		}else if (gained < min){
			stopMenuBg();
		}else{
			menuVol += gain;
		}
		
	}
	
	public void setGameVolume(float gain){
		float gained = battleVol + gain;
		if (gained > max){
			battleVol = max;
		}else if (gained < min){
			stopBattleBg();
		}else{
			battleVol += gain;
		}
	}
	
	public boolean check(Clip clip){
		if (clip != null && clip.isRunning()){
			return true;
		}
		return false;
	}

}
