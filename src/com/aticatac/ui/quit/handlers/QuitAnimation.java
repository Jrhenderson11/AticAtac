package com.aticatac.ui.quit.handlers;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class QuitAnimation extends AnimationTimer {

    private final long then;
    private final GraphicsContext gc;

    public QuitAnimation(GraphicsContext gc, long then) {
        this.then = then;
        this.gc = gc;
    }

    @Override
    public void handle(long now) {

    }
}
