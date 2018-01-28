package com.aticatac.ui.quit;

import com.aticatac.utils.SystemSettings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

public class Quit extends Scene {

    public Quit(Parent root) {
        super(root);
        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        Canvas canvas = new Canvas(width, height);

        //TODO: Are you sure?

    }
}
