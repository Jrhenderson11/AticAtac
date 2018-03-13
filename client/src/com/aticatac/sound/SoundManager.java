package com.aticatac.sound;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.aticatac.world.Player;
import com.aticatac.world.items.Gun;

public class SoundManager{
		
	public static boolean debug = false, shoot = true;
	public static Clip menuClip, battleClip;
	private static float menuVol = -1, battleVol = -1, shootVol = -1;
	private final static float max = 6, min = -10;
	private static String battleFile = "./assets/music/hesitation.wav";
	
	private static boolean soundEnabled;
	
	public SoundManager(boolean newSoundEnabled) {
		this.soundEnabled = newSoundEnabled;
	}
	
	public void playClick() {	
		if (!soundEnabled) return;
		//https://opengameart.org/content/menu-selection-click
		File file = new File("assets/music/click.wav");
		Clip clip = play(file);
		clip.start();
	}
	
	public void playShoot(Player player){
		if (!soundEnabled) return;
		//http://soundbible.com/1405-Dry-Fire-Gun.html
		File file = new File("assets/music/dryfire.wav"); //choice 0, out of paint/default
		Gun gun = player.getGun();
		int choice = 0;
		
		if (gun != null){
			choice = gun.getType();
		}

		if (gun.enoughPaint(player.getPaintLevel())){
			switch(choice){
			case 1:
				//https://opengameart.org/content/flatshot-complete-sfx-pack
				file = new File("assets/music/shoot.wav");
				break;
			case 2:
				//http://soundbible.com/642-Splat.html
				file = new File("assets/music/splat.wav");
				break;
			case 3:
				//http://soundbible.com/144-Spraying-Deodorant.html
				file = new File("assets/music/spray.wav");
				break;
			}
		}
		
		
		Clip clip = play(file);
		FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		vol.setValue(shootVol);
		if (shoot){
			clip.start();
		}
	}

	public void playBgMenu(){
		if (!soundEnabled) return;
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
		if (!soundEnabled) return;
		try {
			//https://opengameart.org/content/hesitation-synth-electronic-loop	
			File file = new File(battleFile);
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
		if (!soundEnabled) return null;
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
	
	//gain can be negative.
	//Setting the volume only works if you change it before playing the clip.
	public void setMenuVolume(float gain){
		muteAll();
		if (gain == min){
			stopMenuBg();
		}else{
			stopMenuBg();
			menuVol = gain;
			if (!soundEnabled) return;
			playBgMenu();
			
		}
		
	}
	
	public void setBattleVolume(float gain){
		muteAll();
		if (gain == min){
			stopBattleBg();
		}else{
			stopBattleBg();
			battleVol = gain;
			if (!soundEnabled) return;
			playBgBattle();
		}
	}
	
	public void setShootVolume(float gain){
		muteAll();
		if (gain == min){
			shoot = false;
		}else{
			shoot = true;
			shootVol = gain;
			File file = new File("assets/music/shoot.wav");
			if (!soundEnabled) return;
			Clip clip = play(file);
			FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			vol.setValue(shootVol);
			clip.start();
		}
	}
	
	public boolean check(Clip clip){
		if (clip != null && clip.isRunning()){
			return true;
		}
		return false;
	}
	
	public void muteAll(){
		stopMenuBg();
		stopBattleBg();
	}
	
	public void setBgChoice(int choice){
		stopBattleBg();
		switch(choice){
		case 1:
			battleFile = "./assets/music/hesitation.wav";
			break;
		case 2:
			battleFile = "./assets/music/battle.wav";
			break;
		case 3:
			battleFile = "./assets/music/battle2.wav";
			break;
		case 4:
			battleFile = "./assets/music/newbattle.wav";
		}
		if (!soundEnabled) return;
		playBgBattle();
	}

}
