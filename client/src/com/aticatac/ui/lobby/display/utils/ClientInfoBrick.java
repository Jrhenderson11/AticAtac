package com.aticatac.ui.lobby.display.utils;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.ui.utils.Drawable;
import javafx.scene.canvas.GraphicsContext;

public class ClientInfoBrick implements Drawable{

    private ClientInfo mine;

    public ClientInfoBrick(ClientInfo info) {

        this.mine = info;

    }

    @Override
    public void draw(GraphicsContext gc, long now) {

    }
}
