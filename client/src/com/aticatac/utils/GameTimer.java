package com.aticatac.utils;

import java.io.Serializable;

import com.aticatac.world.World;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer implements Serializable {
	
	/**
	 * Standard round duration
	 */
	public static final int ROUND_DURATION = 120;
	/**
	 * Standard post round duration
	 */
	public static final int POST_ROUND_DURATION = 8;
	/**
	 * Standard pre round duration
	 */
	public static final int PRE_ROUND_DURATION = 3;
	/**
	 * The timer used for the countdown during the READY phase
	 */
	private transient Timeline countdownTimer;
	/**
	 * The timer used to start the round after the countdownTimer during READY phase
	 */
	private transient Timeline readyTimer;
	/**
	 * The timer for the round during PLAYING phase
	 */
	private transient Timeline roundTimer;
	/**
	 * The timer for the end round period during the OVER phase
	 */
	private transient Timeline endRoundTimer;
	/**
	 * The world object to manipulate the time of
	 */
	private World world;
	
	/**
	 * Creates a new GameTimer for the given World
	 * @param world The World object to manipulate the time of
	 */
	public GameTimer(World world) {
		this.world = world;
		//init timers but dont start them to avoid nullptr exceptions
		this.countdownTimer = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        ae -> {world.changeRoundTime(-1);}));
		countdownTimer.setCycleCount(PRE_ROUND_DURATION);
		this.readyTimer = new Timeline(new KeyFrame(
		        Duration.millis(PRE_ROUND_DURATION * 1000),
		        ae -> world.startGame()));
		readyTimer.setCycleCount(1);
		this.roundTimer = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        ae -> world.changeRoundTime(1)));
		roundTimer.setCycleCount(ROUND_DURATION);
		this.endRoundTimer = new Timeline(new KeyFrame(
		        Duration.millis(POST_ROUND_DURATION * 1000),
		        ae -> world.newRound()));
		endRoundTimer.setCycleCount(1);
	}

	/**
	 * Starts a countdown timer that counts the world's clock down from PRE_ROUND_DURATION to 0
	 */
	public void startCountdownTimer() {
		world.setRoundTime(PRE_ROUND_DURATION);
		countdownTimer = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        ae -> {world.changeRoundTime(-1); System.out.println("TICK TOCK");}));
		countdownTimer.setCycleCount(PRE_ROUND_DURATION); //3 second countdown
		countdownTimer.play();
		startReadyTimer();
	}
	
	/**
	 * Starts the 'ready' timer that starts the game after the PRE_ROUND_DURATION.
	 * Called by startCountdownTimer().
	 */
	private void startReadyTimer() {
		readyTimer = new Timeline(new KeyFrame(
		        Duration.millis(PRE_ROUND_DURATION * 1000),
		        ae -> world.startGame()));
		readyTimer.setCycleCount(1);
		readyTimer.play();
	}
	
	/**
	 * Starts the round timer that counts the world clock from 0 to 60
	 */
	public void startRoundTimer() {
		roundTimer = new Timeline(new KeyFrame(
		        Duration.millis(1000), //every second
		        ae -> world.changeRoundTime(1)));
		roundTimer.setCycleCount(ROUND_DURATION); //60 seconds
		roundTimer.play();
	}
	
	/**
	 * Starts the end round delay
	 */
	public void startEndRoundDelay() {
		endRoundTimer = new Timeline(new KeyFrame(
		        Duration.millis(POST_ROUND_DURATION * 1000),
		        ae -> world.newRound()));
		endRoundTimer.setCycleCount(1);
		endRoundTimer.play();
	}
	
	/**
	 * Stops the timer of the current phase
	 */
	public void stop() {
		System.out.println("stop: " + world.getGameState().toString());
		switch(world.getGameState()) {
			case READY:
				countdownTimer.stop();
				readyTimer.stop();
				break;
			case PLAYING:
				roundTimer.stop();
				break;
			case OVER:
				endRoundTimer.stop();
				break;
		}
	}
	
	/**
	 * Pauses the timer of the current phase
	 */
	public void pause() {
		System.out.println("pause: " + world.getGameState().toString());
		switch(world.getGameState()) {
			case READY:
				countdownTimer.pause();
				readyTimer.pause();
				break;
			case PLAYING:
				roundTimer.pause();
				break;
			case OVER:
				endRoundTimer.pause();
				break;
		}
	}
	
	/**
	 * Resumes the timer of the current phase
	 */
	public void resume() {
		System.out.println("resume: " + world.getGameState().toString());
		switch(world.getGameState()) {
			case READY:
				countdownTimer.play();
				readyTimer.play();
				break;
			case PLAYING:
				roundTimer.play();
				break;
			case OVER:
				endRoundTimer.play();
				break;
		}
	}
}