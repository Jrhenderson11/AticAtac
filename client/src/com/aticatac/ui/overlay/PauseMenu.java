package com.aticatac.ui.overlay;

import java.awt.Point;
import java.awt.Rectangle;

import com.aticatac.sound.SoundManager;
import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class PauseMenu {

	/**
	 * Constant for RESUME
	 */
	public static final int RESUME = 1;
	/**
	 * Constant for QUIT_TO_MENU
	 */
	public static final int QUIT_TO_MENU = 2;
	/**
	 * Constant for QUIT_GAME
	 */
	public static final int QUIT_GAME = 3;
	/**
	 * The stage this PauseMenu is part of, used for changing the scene
	 */
	private Stage stage;
	/**
	 * The Main menu to return to when QUIT_TO_MENU
	 */
	private MainMenu mainmenu;
	/**
	 * Controls the displaying of pause menu. True for displaying the pause menu
	 */
	private boolean paused;
	/**
	 * The current selection for RESUME, QUIT_TO_MENU, QUIT_GAME, or 0 for none selected
	 */
	private int selection;
	
	/**
	 * Creates a pause menu.
	 * @param stage The stage this pause menu is a child of
	 * @param mainmenu The MainMenu to return to when RESUME
	 */
	public PauseMenu(Stage stage, MainMenu mainmenu) {
		this.stage = stage;
		this.mainmenu = mainmenu;
		this.paused = false;
		this.selection = 0;
	}
	
	/**
	 * Handles clicks to the screen
	 * @param mousePosition The position of the mouse
	 */
	public int handleClick() {
		if (paused) {
			if (selection == RESUME) {
				setPaused(false);
				return RESUME;
			} else if (selection == QUIT_TO_MENU) {
				SoundManager m = new SoundManager();
				m.muteAll();
				m.playBgMenu();
				stage.setScene(mainmenu);
				return QUIT_TO_MENU;
			} else if (selection == QUIT_GAME) {
				Platform.exit();
				System.exit(0);
				return QUIT_GAME;
			}
		}
		return 0;
	}
	
	/**
	 * Handles the mouse
	 * @param mousePosition The position of the mouse, used for highlighting
	 */
	public void handleHover(Point mousePosition) {
		if (paused) {
			int width  = SystemSettings.getScreenWidth();
			int height = SystemSettings.getScreenHeight();
			int boxWidth  = width / 3;
			int boxHeight = height / 2;
			int buttonHeight = boxHeight / 4;
			if (mousePosition.getX() > (width / 3) && mousePosition.getX() < (2 * (width / 3))) {
				if (mousePosition.getY() > (height / 4) && mousePosition.getY() < (3 * (height / 4))) {
					Rectangle rect;
					selection = 0;
					for (int i = 1; i < 4; i++) {
						rect = new Rectangle((width / 3), (height / 4) + (i * buttonHeight), boxWidth, buttonHeight);	
						if (rect.contains(mousePosition)) {
							selection = i;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Draws the pause menu to the given GraphicsContext
	 * @param gc The GraphicsContext to draw to
	 */
	public void draw(GraphicsContext gc) {
		if (paused) {
			int width  = SystemSettings.getScreenWidth();
			int height = SystemSettings.getScreenHeight();
			int boxWidth  = width / 3;
			int boxHeight = height / 2;
			double tintOpacity = 0.5;
			double boxOpacity = 0.8;
			Color color = Color.BLACK;
			Color boxColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), boxOpacity);
			Color tintColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), tintOpacity);
			
			//draw tint
			gc.setFill(tintColor);
			gc.fillRect(0, 0, width, height);
			
			//draw box
			gc.setFill(boxColor);
			gc.fillRect(width/3, height/4, boxWidth, boxHeight);
			gc.setStroke(Color.WHITE);
			gc.strokeRect(width/3, height/4, boxWidth, boxHeight);
			
			int i = 1; //used for spacing between text
			
			//draw PAUSED title
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setFill(Color.WHITE);
			gc.setFont(UIDrawer.OVERLAY_FONT);
			gc.fillText("PAUSED", width/2, (height/4) + (i++ *(boxHeight / 5)));
			
			
			//draw resume button
			gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
			if (selection == i-1) {
				gc.setFont(UIDrawer.OVERLAY_FONT);
			} else {
				gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
			}
			gc.fillText("Resume", width/2, (height/4) + (i++ *(boxHeight / 5)));
			
			
			//draw quit to menu button
			if (selection == i-1) {
				gc.setFont(UIDrawer.OVERLAY_FONT);
			} else {
				gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
			}
			gc.fillText("Quit to Menu", width/2, (height/4) + (i++ *(boxHeight / 5)));
			
			//draw quit game button
			if (selection == i-1) {
				gc.setFont(UIDrawer.OVERLAY_FONT);
			} else {
				gc.setFont(UIDrawer.OVERLAY_FONT_SMALL);
			}
			gc.fillText("Quit Game", width/2, (height/4) + (i++ *(boxHeight / 5)));
			
		}
	}
	
	/**
	 * Returns the current paused status
	 * @return Returns true if paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Change the current paused status
	 * @param paused true for paused, false for not.
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	/**
	 * Toggles the paused state
	 * @return The resulting state
	 */
	public boolean togglePaused() {
		if (paused) {
			setPaused(false);
			return false;
		} else {
			setPaused(true);
			return true;
		}
	}
}
