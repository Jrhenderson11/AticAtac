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
	
	/**
	 * Control booleans
	 */
	public static boolean debug = false, shoot = true;
	/**
	 * Clip objects for the main menu and battle music
	 */
	public static Clip menuClip, battleClip;
	/**
	 * The starting volumes for the 3 sound types
	 */
	private static float menuVol = -1, battleVol = -1, shootVol = -1;
	/**
	 * Maximum and minimum volumes
	 */
	private final static float max = 6, min = -10;
	/**
	 * Initial battle music file path
	 */
	private static String battleFile = "./assets/music/hesitation.wav";
	
	/**
	 * Plays a click noise
	 */
	public void playClick() {	
		//https://opengameart.org/content/menu-selection-click
		File file = new File("assets/music/click.wav");
		Clip clip = play(file);
		clip.start();
	}
	
	/**
	 * Plays a shooting noise depending on the weapon of the given player
	 * @param player The Player who shot
	 */
	public void playShoot(Player player){
		//http://soundbible.com/1405-Dry-Fire-Gun.html
		File file = new File("assets/music/dryfire.wav"); //choice 0, out of paint/default
		Gun gun = player.getGun();
		int choice = 0;
		
		if (gun != null){
			choice = gun.getType();
		}

		if (gun.enoughPaint()){
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

	/**
	 * Plays the background menu music
	 */
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
	
	/**
	 * Stops the background menu music
	 */
	public void stopMenuBg(){
		if(check(menuClip)){
			menuClip.close();
		}
	}
	
	/**
	 * Plays the background in game music
	 */
	public void playBgBattle(){
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
	
	/**
	 * Stops the background in game music
	 */
	public void stopBattleBg(){
		if (check(battleClip)){
			battleClip.close();
		}
	}
	
	/**
	 * Plays the given audio file
	 * @param file The audio file to play
	 * @return The initialised Clip object, or null.
	 */
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
	
	/**
	 * Sets the main menu music volume
	 * @param gain The volume to set it to, can be negative
	 */
	public void setMenuVolume(float gain){
		muteAll();
		if (gain == min){
			stopMenuBg();
		}else{
			stopMenuBg();
			menuVol = gain;
			playBgMenu();
			
		}
		
	}
	
	/**
	 * Sets the main menu music volume
	 * @param gain The volume to set it to, can be negative
	 */
	public void setBattleVolume(float gain){
		muteAll();
		if (gain == min){
			stopBattleBg();
		}else{
			stopBattleBg();
			battleVol = gain;
			playBgBattle();
		}
	}
	
	/**
	 * Sets the in game shooting volume
	 * @param gain The volume to set it to, can be negative
	 */
	public void setShootVolume(float gain){
		muteAll();
		if (gain == min){
			shoot = false;
		}else{
			shoot = true;
			shootVol = gain;
			File file = new File("assets/music/shoot.wav");
			Clip clip = play(file);
			FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			vol.setValue(shootVol);
			clip.start();
		}
	}
	
	/**
	 * Checks if the given clip is running
	 * @param clip The clip to check
	 * @return True if it is running
	 */
	public boolean check(Clip clip){
		if (clip != null && clip.isRunning()){
			return true;
		}
		return false;
	}
	
	/**
	 * Mutes all audio
	 */
	public void muteAll(){
		stopMenuBg();
		stopBattleBg();
	}
	
	/**
	 * Sets the background battle music to the given choide
	 * @param choice The identifying number, 1-4.
	 */
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
		
		playBgBattle();
	}

}
