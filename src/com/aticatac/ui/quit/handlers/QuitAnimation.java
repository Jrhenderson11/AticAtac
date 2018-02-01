package com.aticatac.ui.quit.handlers;

import com.aticatac.ui.quit.utils.Option;
import com.aticatac.ui.quit.utils.QuitDrawer;
import com.aticatac.ui.utils.UIDrawer;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class QuitAnimation extends AnimationTimer {

    private final long then;
    private final GraphicsContext gc;
    private final ArrayList<Option> options;

    public QuitAnimation(GraphicsContext gc, ArrayList<Option> options, long then) {
        this.then = then;
        this.gc = gc;
        this.options = options;
    }

    @Override
    public void handle(long now) {

        UIDrawer.background(gc, Color.BLACK);
        QuitDrawer.options(gc, options, now);
        QuitDrawer.title(gc, now);

    }
}
