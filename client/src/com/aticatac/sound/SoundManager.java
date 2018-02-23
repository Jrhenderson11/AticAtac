package com.aticatac.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
public class SoundManager{
	
public static MediaPlayer bgMenu, bgBattle;
public static AudioPlayer apMenu, apBattle;
public static AudioStream asMenu, asBattle;
public static ContinuousAudioDataStream cadsMenu, cadsBattle;

	public SoundManager(){
		//bgMenu = play(fileMenu);
		//bgGame = play(fileGame);
	}
	
	public void playClick() {	
		//https://opengameart.org/content/menu-selection-click
		String file = "assets/music/click.wav";

		MediaPlayer m = play(file);
		m.play();
	}
	
	public void playShoot(){
		//https://opengameart.org/content/flatshot-complete-sfx-pack
		String file = "assets/music/shoot.wav";
		
		MediaPlayer m = play(file);
		m.play();
		
	}

	
	public void playBgMenu2(){
		AudioPlayer audioPlayer = AudioPlayer.player;
	    AudioStream audioStream;
	    AudioData audioData;

	    ContinuousAudioDataStream loop = null;

	    try
	    {
	    	//https://opengameart.org/content/halloween-rocknroll-music-loop
	    	String file = "./assets/music/menubg.wav";
	    	System.out.println(file);
	        audioStream = new AudioStream(new FileInputStream(file));
	        audioData = audioStream.getData();
	        loop = new ContinuousAudioDataStream(audioData);
	        asMenu = audioStream;
	        cadsMenu = loop;
	    }
	    catch(IOException e)
	    {
	        System.out.println("cant find the file");
	    }
	    
	    apMenu = audioPlayer;
	    apMenu.start(loop);
	}
	
	public void stopMenuBg(){
		try {
			apMenu.stop(cadsMenu);
			cadsMenu.close();
			asMenu.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("attempt to stop");
	}
	

	
	public void playBgGame(){
		AudioPlayer audioPlayer = AudioPlayer.player;
	    AudioStream audioStream;
	    AudioData audioData;

	    ContinuousAudioDataStream loop = null;

	    try
	    {
	    	//https://opengameart.org/content/halloween-rocknroll-music-loop
	    	String file = "./assets/music/hesitation.wav";
	    	System.out.println(file);
	        audioStream = new AudioStream(new FileInputStream(file));
	        audioData = audioStream.getData();
	        loop = new ContinuousAudioDataStream(audioData);
	        asBattle = audioStream;
	        cadsBattle = loop;
	    }
	    catch(IOException e)
	    {
	        System.out.println("cant find the file");
	    }
	    
	    apBattle = audioPlayer;
	    audioPlayer.start(loop);
	}
	
	public void stopBattleBg(){
		try {
			apBattle.stop(cadsBattle);
			cadsBattle.close();
			asBattle.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("attempt to stop");
	}
	
	public MediaPlayer play(String url){

		Media sound = new Media(new File(url).toURI().toString());
		
		MediaPlayer mediaPlayer = new MediaPlayer(sound);

		return mediaPlayer;
	}

}
