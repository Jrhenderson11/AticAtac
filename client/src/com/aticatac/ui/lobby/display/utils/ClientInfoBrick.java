package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.ui.utils.Drawable;
import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClientInfoBrick implements Drawable{

    private final int offset;
    private ClientInfo mine;

    public ClientInfoBrick(ClientInfo info, int i) {
        this.mine = info;
        this.offset = i;
    }

    @Override
    public void draw(GraphicsContext gc, long now) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        // Container
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.gray(0.1));
        int x = width / 10;
        int y = (1 + offset) * height / 6;
        int w = 8 * width / 10;
        int h = height / 6;
        gc.strokeRect(x, y, w, h);
        gc.fillRect(x, y, w, h);

    }
}
