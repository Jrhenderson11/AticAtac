package com.aticatac.ui.settings;

import static java.lang.Math.abs;
import static java.lang.StrictMath.sin;

import java.util.ArrayList;

import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class SettingsDrawer extends AnimationTimer{
	private final long then;
    private GraphicsContext gc;
	
	public SettingsDrawer(GraphicsContext gc, long then){
		this.then = then;
		this.gc = gc;
	}

	@Override
	public void handle(long now) {
		double animation = now - then;
        animation = animation / 1000000000;

        UIDrawer.background(gc, Color.BLACK);
        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();

        gc.save();

        gc.setStroke(Color.WHITE);
        gc.setFill(Color.gray(abs(sin(animation))));

        gc.setFont(UIDrawer.TITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("Settings", width / 2, height / 7);
        gc.fillText("Settings", width / 2, height / 7);

        gc.restore();
	}
	
	public void drawLabel(ArrayList<Label> lbl, int x, int y, int space){
		for (int i = 0; i < lbl.size(); i++){
			lbl.get(i).setTextFill(Color.WHITE);
			lbl.get(i).setLayoutX(x);
			lbl.get(i).setLayoutY(y + space*i);
		}
	}
	
	public void drawRadBtn(ArrayList<RadioButton> rad, int x, int y , int space){
		for (int i = 0; i < rad.size(); i++){
			rad.get(i).setTextFill(Color.WHITE);
			rad.get(i).setLayoutX(x);
			rad.get(i).setLayoutY(y + space*i);
		}
	}
	
	public GraphicsContext getGc() {
		return gc;
	}

	public void setGc(GraphicsContext gc) {
		this.gc = gc;
	}
}
