package com.aticatac.ui.quit.utils;

import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class QuitDrawer {

    public static void options(GraphicsContext gc, ArrayList<Option> options) {

        for (int i = 0; i < options.size(); i++) {

            gc.save();
            gc.setStroke(Color.GRAY);


        }

    }

}
