package com.aticatac.ui.utils;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public abstract class Button implements Drawable {
    private Rectangle hitbox;
    private String text;
    private boolean selected;

    public Button(Rectangle hitbox, String text) {
        this.hitbox = hitbox;
        this.text = text;
        this.selected = false;
    }

    public void toggle() {
        this.selected = !this.selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public abstract void click();

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    boolean clickCollides(MouseEvent e) {

        double x = e.getX();
        double y = e.getY();

        if (x >= hitbox.getX() && x <= hitbox.getWidth() + hitbox.getX()
                && y >= hitbox.getY() && y <= hitbox.getY() + hitbox.getHeight()) {
            return true;
        }

        return false;
    }

    public String getText() {
        return text;
    }
}
