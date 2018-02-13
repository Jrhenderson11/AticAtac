package com.aticatac.ui.quit.handlers;

import com.aticatac.ui.quit.utils.Option;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static com.aticatac.ui.quit.utils.Option.unselectAll;

public class QuitMouseMoved implements EventHandler<MouseEvent> {

    private final ArrayList<Option> options;

    public QuitMouseMoved(ArrayList<Option> options) {
        this.options = options;
    }

    @Override
    public void handle(MouseEvent event) {
        int mousedId = mouseInHitbox(event.getX(), event.getY());

        unselectAll(options);

        if (mousedId == -1) return;
        else {
            options.get(mousedId).select();
        }
    }
    private int mouseInHitbox(double x, double y) {

        for (int i = 0; i < options.size(); i++) {
            Rectangle box = options.get(i).getHitbox();
            if (x >= box.getX() && x <= box.getWidth() + box.getX()
                    && y >= box.getY() && y <= box.getY() + box.getHeight()) {
                return i;
            }
        }
        return -1;
    }

}
