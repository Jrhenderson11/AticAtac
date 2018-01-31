package com.aticatac.ui.quit.utils;

import javafx.scene.shape.Rectangle;

import java.util.function.Function;

public class Option {
    private boolean selected;
    private Function<Void, Void> select;
    private String text;
    private Rectangle hitbox;

    public Option(Function<Void, Void> select, String text) {
        this.select = select;
        this.text = text;
        this.hitbox = new Rectangle(0, 0);
        this.selected = false;
    }

    public boolean selected() {
        return selected;
    }

    public void select() {
        selected = true;
    }

    public void unselect() {
        selected = false;
    }


    public void execute() {
        select.apply(null);
    }

    public String getText() {
        return text;
    }

    public void setHitbox(Rectangle rectangle) {
        this.hitbox = rectangle;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
