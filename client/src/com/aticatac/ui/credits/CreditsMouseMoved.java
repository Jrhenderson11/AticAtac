package com.aticatac.ui.credits;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static com.aticatac.ui.credits.CreditsItems.unselectAll;

public class CreditsMouseMoved implements EventHandler<MouseEvent> {
    private final ArrayList<CreditsItems> creditsItems;
    public  CreditsMouseMoved(ArrayList<CreditsItems> creditsItems) {
        this.creditsItems = creditsItems;
    }
    @Override
    public void handle(MouseEvent event) {

        int mousedId = mouseInHitbox(event.getX(), event.getY());

        unselectAll(creditsItems);

        if (mousedId == -1) return;
        else {
            creditsItems.get(mousedId).select();
        }
    }

    /**
     * Returns the index of the hitbox of the MenuItem the mouse if in
     * @param x The mouse X position
     * @param y The mouse Y position
     * @return The index of the hitbox of the selected MenuItem
     */
    private int mouseInHitbox(double x, double y) {

        for (int i = 0; i < creditsItems.size(); i++) {
            Rectangle box = creditsItems.get(i).getHitbox();
            if (x >= box.getX() && x <= box.getWidth() + box.getX()
                    && y >= box.getY() && y <= box.getY() + box.getHeight()) {
                return i;
            }
        }
        return -1;
    }
}
