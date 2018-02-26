package com.aticatac.utils;

import com.aticatac.world.World;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {
	
	public static final int ROUND_DURATION = 60; 
	public static final int POST_ROUND_DURATION = 8;
	public static final int PRE_ROUND_DURATION = 3;
	private World world;
	
	public GameTimer(World world) {
		this.world = world;
	}

	/**
	 * Starts a countdown timer that counts the world's clock down from PRE_ROUND_DURATION to 0
	 */
	public void startCountdownTimer() {
		world.setRoundTime(PRE_ROUND_DURATION);
		Timeline timeline = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        ae -> {world.changeRoundTime(-1);}));
		timeline.setCycleCount(PRE_ROUND_DURATION); //3 second countdown
		timeline.play();
		startReadyTimer();
	}
	
	/**
	 * Starts the 'ready' timer that starts the game after the PRE_ROUND_DURATION.
	 * Called by startCountdownTimer().
	 */
	private void startReadyTimer() {
		Timeline timeline = new Timeline(new KeyFrame(
		        Duration.millis(PRE_ROUND_DURATION * 1000),
		        ae -> world.startGame()));
		timeline.setCycleCount(1);
		timeline.play();
	}
	
	/**
	 * Starts the round timer that counts the world clock down from
	 */
	public void startRoundTimer() {
		Timeline timeline = new Timeline(new KeyFrame(
		        Duration.millis(1000), //every second
		        ae -> world.changeRoundTime(1)));
		timeline.setCycleCount(ROUND_DURATION); //60 seconds
		timeline.play();
	}
	
	public void startEndRoundDelay() {
		Timeline timeline = new Timeline(new KeyFrame(
		        Duration.millis(POST_ROUND_DURATION * 1000),
		        ae -> world.newRound()));
		timeline.setCycleCount(1);
		timeline.play();
	}
}