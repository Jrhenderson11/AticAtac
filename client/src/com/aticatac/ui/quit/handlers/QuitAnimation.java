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

    /**
     * Creates a new QuitAnimation for exiting the game
     * @param gc The GraphicsContext to draw to
     * @param options The list of options in the quit menu
     * @param then The current animation time
     */
    public QuitAnimation(GraphicsContext gc, ArrayList<Option> options, long then) {
        this.then = then;
        this.gc = gc;
        this.options = options;
    }

    /**
     * Handles the animation each tick
     * @param now The current animation time
     */
    @Override
    public void handle(long now) {

        double animation = now - then;
        animation = animation / 1000000000;

        UIDrawer.background(gc, Color.BLACK);
        QuitDrawer.options(gc, options, animation);
        QuitDrawer.title(gc, animation);

    }
}
