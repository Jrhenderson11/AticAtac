package com.aticatac.ui.lobby.browser.handlers;

import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.lobby.browser.utils.LBDrawer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class LBMouseMoved implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        int mSelect = getHitboxSelected(event.getX(), event.getY());
        Browser.select(mSelect);
    }

    private int getHitboxSelected(double x, double y) {
    	//TODO: null ptr exception here
        ArrayList<Rectangle> hitboxes = (ArrayList<Rectangle>) LBDrawer.getHitboxs().clone();

        for (int i = 0; i < hitboxes.size(); i++) {
            Rectangle box = hitboxes.get(i);

            if (x >= box.getX() && x <= box.getWidth() + box.getX()
                    && y >= box.getY() && y <= box.getY() + box.getHeight()) {
                return i;
            }

        }
        return -1;
    }
}
