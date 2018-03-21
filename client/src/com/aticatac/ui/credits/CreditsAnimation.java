package com.aticatac.ui.credits;

import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class CreditsAnimation extends AnimationTimer {
    private final Canvas canvas;
    private final long then;
    private final ArrayList<CreditsItems> names;

    public CreditsAnimation(Canvas canvas, ArrayList<CreditsItems> names, long then) {
        super();
        this.canvas = canvas;
        this.then = then;
        this.names = names;
    }

    @Override
    public void handle(long now) {
        canvas.setWidth(SystemSettings.getScreenWidth());
        canvas.setHeight(SystemSettings.getScreenHeight());
        UIDrawer.background(canvas.getGraphicsContext2D(), Color.gray(0.3));
        CreditsDrawer.title(canvas.getGraphicsContext2D(), now - then);
        CreditsDrawer.creditsItems(canvas.getGraphicsContext2D(), names, now - then);
    }



}
