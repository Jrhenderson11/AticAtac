package com.aticatac.ui.lobby.display.handlers;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.display.utils.*;
import com.aticatac.ui.utils.Drawable;
import com.aticatac.utils.SystemSettings;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;

public class DAnimator extends AnimationTimer {
    private final GraphicsContext gc;
    private final int selected;
    private final LobbyServer server;
    private HashSet<Drawable> drawables;
    private boolean isLead;

    public DAnimator(GraphicsContext gc, int selected, LobbyServer server) {
        this.gc = gc;
        this.selected = selected;
        this.server = server;
        this.isLead = false;

        Rectangle hitbox;

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();

        // TODO: might have to move this to handle V if its not updating

        drawables = new HashSet<>();

        Lobby lobby = server.updateLobby(selected);
        ClientInfo leader = lobby.getLobbyLeader();
        ArrayList<ClientInfo> peasants  = lobby.getPeasants();
        if (leader.equals(server.myInfo())) isLead = true;

        drawables.add(new ClientInfoBrick(leader, 0));

        for (int i = 0; i < peasants.size(); i++) {
            ClientInfo c = peasants.get(i);
            drawables.add(new ClientInfoBrick(c, i + 1));
            drawables.add(new KickButton(i));
        }

        hitbox = new Rectangle(0, 9 * height / 10, width / 10, height / 10);
        drawables.add(new BackButton(hitbox));
        drawables.add(new LobbyHeader(lobby));
        hitbox = new Rectangle(0, 9 * height / 10, width / 10, height / 10);

        if (isLead) {
            drawables.add(new ReadyStartButton(hitbox, "Start"));
        } else {
            drawables.add(new ReadyStartButton(hitbox, "Ready"));
        }

    }

    @Override
    public void handle(long now) {
        for (Drawable d : drawables) {
            d.draw(gc, now);
        }
    }
}
