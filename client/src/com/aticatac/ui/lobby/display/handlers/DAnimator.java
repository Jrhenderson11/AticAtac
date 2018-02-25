package com.aticatac.ui.lobby.display.handlers;

import java.util.ArrayList;
import java.util.HashSet;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.LobbyServer;
import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.lobby.display.Displayer;
import com.aticatac.ui.lobby.display.utils.BackButton;
import com.aticatac.ui.lobby.display.utils.ClientInfoBrick;
import com.aticatac.ui.lobby.display.utils.LobbyHeader;
import com.aticatac.ui.lobby.display.utils.ReadyStartButton;
import com.aticatac.ui.utils.Drawable;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DAnimator extends AnimationTimer {
    private final GraphicsContext gc;
    private final int selected;
    private final LobbyServer server;
    private boolean isLead;
    private Browser parent;
    private Stage stage;
    
    private int width;
    private int height;
    
    public DAnimator(GraphicsContext gc, int selected, LobbyServer server, Browser newParent, Stage newStage) {
        this.gc = gc;
        this.selected = selected;
        this.server = server;
        this.isLead = false;
        this.parent = newParent;
        this.stage = newStage;
        
        Rectangle hitbox;

        this.width = SystemSettings.getNativeWidth();
        this.height = SystemSettings.getNativeHeight();

        // TODO: might have to move this to handle V if its not updating

        Displayer.setDrawables(new HashSet<>());
        Displayer.setButtons(new HashSet<>());

        Lobby lobby = server.updateLobby(selected);
        //don't be greedy: let the server take some time to get it's lobby object
        while (lobby == null) {
            System.out.println("waiting for lobby obj");
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
            
            lobby = server.updateLobby(selected);
        }
        System.out.println("finished waiting");
        
        ClientInfo leader = lobby.getLobbyLeader();
        if (leader.equals(server.myInfo())) isLead = true;

    }

    @Override
    public void handle(long now) {
    	Lobby lobby = server.updateLobby(selected);
    	ClientInfo leader = lobby.getLobbyLeader();
        ArrayList<ClientInfo> peasants = lobby.getPeasants();
        
        Displayer.setDrawables(new HashSet<>());
        Displayer.setButtons(new HashSet<>());
        Rectangle hitbox;
        //draw leader
        Displayer.getDrawables().add(new ClientInfoBrick(leader, 0, isLead, server));
        for (int i = 0; i < peasants.size(); i++) {
            ClientInfo c = peasants.get(i);

	        hitbox = new Rectangle(0, 9 * height / 10, width / 10, height / 10);
            boolean led = false;
            if (leader.equals(c)) led = true;
            Displayer.getDrawables().add(new ClientInfoBrick(c, i + 1, isLead && !led,server));
        }

        hitbox = new Rectangle(0, 9 * height / 10, width / 10, height / 10);
        BackButton backButton = new BackButton(hitbox, server, parent, stage);
        Displayer.getButtons().add(backButton);
        Displayer.getDrawables().add(backButton);

        Displayer.getDrawables().add(new LobbyHeader(lobby));
        hitbox = new Rectangle(0.9 * width, 9 * height / 10, width / 10, height / 10);

        ReadyStartButton sbutton;
        if (isLead) {
            sbutton = new ReadyStartButton(hitbox, "Start", server);
        } else {
            sbutton = new ReadyStartButton(hitbox, "Ready", server);
        }

        Displayer.getDrawables().add(sbutton);
        Displayer.getButtons().add(sbutton);
        UIDrawer.background(gc, Color.gray(0.3));
        for (Drawable d : Displayer.getDrawables()) {
            d.draw(gc, now);
        }
    }
}
