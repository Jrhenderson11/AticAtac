package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.ui.utils.Drawable;
import javafx.scene.canvas.GraphicsContext;

public class ClientInfoBrick implements Drawable{

    private final int offset;
    private ClientInfo mine;

    public ClientInfoBrick(ClientInfo info, int i) {
        this.mine = info;
        this.offset = i;
    }

    @Override
    public void draw(GraphicsContext gc, long now) {

    }
}
