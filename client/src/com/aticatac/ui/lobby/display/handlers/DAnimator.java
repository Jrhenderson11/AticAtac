package com.aticatac.ui.lobby.display.handlers;

import java.util.ArrayList;
import java.util.HashSet;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.Lobby.ai;
import com.aticatac.lobby.LobbyServer;
import com.aticatac.networking.client.UDPClient;
import com.aticatac.networking.globals.Globals;
import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.lobby.display.Displayer;
import com.aticatac.ui.lobby.display.utils.AIInfoBrick;
import com.aticatac.ui.lobby.display.utils.AddAIButton;
import com.aticatac.ui.lobby.display.utils.BackButton;
import com.aticatac.ui.lobby.display.utils.ClientInfoBrick;
import com.aticatac.ui.lobby.display.utils.LobbyHeader;
import com.aticatac.ui.lobby.display.utils.ReadyStartButton;
import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.ui.tutorial.MultiPlayer;
import com.aticatac.ui.utils.Drawable;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DAnimator extends AnimationTimer {

    private final GraphicsContext gc;
    private final int selected;
    private final LobbyServer server;
    private final LobbyHeader lobbyHeader;
    private boolean isLead;
    private Browser parent;
    private Stage stage;
    private Scene mainMenu;
    
    
    private BackButton backButton;
    private ReadyStartButton sbutton;
    private AddAIButton aiButton;

    private int width;
    private int height;

    public DAnimator(GraphicsContext gc, int selected, LobbyServer server, Browser newParent, Stage newStage, Scene mainMenu) {
        this.gc = gc;
        this.selected = selected;
        this.server = server;
        this.isLead = false;
        this.parent = newParent;
        this.stage = newStage;
        this.mainMenu = mainMenu;
        System.out.println("SAGE===============================");
        System.out.println(stage==null);
        
        this.width = SystemSettings.getScreenWidth();
        this.height = SystemSettings.getScreenHeight();

        // TODO: might have to move this to handle V if its not updating

        Displayer.setDrawables(new HashSet<>());
        Displayer.setButtons(new HashSet<>());

        Lobby lobby = server.updateLobby(selected);
        // don't be greedy: let the server take some time to get it's lobby object
        while (lobby == null) {
            System.out.println("waiting for lobby obj");
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }

            lobby = server.updateLobby(selected);
        }
        System.out.println("finished waiting");


        ClientInfo leader = lobby.getLobbyLeader();
        if (leader.equals(server.myInfo()))
            isLead = true;

        // create back and ready boxes
        Rectangle hitbox;
        hitbox = new Rectangle(0, 9 * height / 10, width / 10, height / 10);
        backButton = new BackButton(hitbox, server, parent, stage);
        Displayer.getButtons().add(backButton);
        Displayer.getDrawables().add(backButton);

        lobbyHeader = new LobbyHeader(lobby);

        Displayer.getDrawables().add(lobbyHeader);

        hitbox = new Rectangle(0.9 * width, 9 * height / 10, width / 10, height / 10);

        if (isLead) {
            sbutton = new ReadyStartButton(hitbox, "Start", server, stage);
        } else {
            sbutton = new ReadyStartButton(hitbox, "Ready", server, stage);
        }

        Displayer.getDrawables().add(sbutton);
        Displayer.getButtons().add(sbutton);

        if (server.myInfo().getID().equals(leader.getID())) {
            hitbox = new Rectangle(0.35 * width, 0.9 * height, (width / 5), height / 10);
            aiButton = new AddAIButton(hitbox, server);
            Displayer.getDrawables().add(aiButton);
            Displayer.getButtons().add(aiButton);
        }
    }

    
    @Override
    public void handle(long now) {
        // System.out.println("im still going");
        Lobby lobby = server.updateLobby(selected);
        ClientInfo leader = lobby.getLobbyLeader();
        ArrayList<ClientInfo> peasants = lobby.getPeasants();
        ArrayList<ai> bots = server.updateLobby(0).getBots();
        Displayer.setDrawables(new HashSet<>());
        Rectangle hitbox;
        // draw leader
        // System.out.println("drawing " + leader.getID());
        Displayer.getDrawables().add(new ClientInfoBrick(leader, 0, isLead, server));
        for (int i = 0; i < peasants.size(); i++) {
            ClientInfo c = peasants.get(i);
            // System.out.println("drawing " + c.getID());
            hitbox = new Rectangle(0, 9 * height / 10, width / 10, height / 10);
            boolean led = false;
            if (leader.equals(c))
                led = true;
            Displayer.getDrawables().add(new ClientInfoBrick(c, i + 1, isLead && !led, server));
        }
        //TODO: replace placeholder 0
        for (int i = 0; i < bots.size(); i++) {
            // System.out.println("drawing " + c.getID());
            hitbox = new Rectangle(0, 9 * height / 10, width / 10, height / 10);
            boolean led = false;
            Displayer.getDrawables().add(new AIInfoBrick(bots.get(i), i + peasants.size() + 1, isLead && !led, server));
        }

        // add ready and back buttons
        Displayer.getDrawables().add(sbutton);
        Displayer.getDrawables().add(backButton);
        if (server.myInfo().getID().equals(leader.getID())) {
            Displayer.getDrawables().add(aiButton);
        }
        // if leader draw add AI button

        // if game has been started go to game screen
        if (lobby.getStarted() || this.server.getStatus() == Globals.IN_GAME) {
            
            System.out.println("starting!!!");
            
            stage.setScene(new MultiPlayer(new Group(), (UDPClient) server, stage, mainMenu));
            this.stop();

        }

        // If I am kicked go to previous menu
        if (this.server.myInfo() == null) {
            this.stop();
            this.server.setStatus(Globals.IN_LIMBO);
            stage.setScene(parent);
        }

        Displayer.getDrawables().add(lobbyHeader);

        UIDrawer.background(gc, Color.gray(0.3));
        for (Drawable d : Displayer.getDrawables()) {
            d.draw(gc, now);
        }
    }

}
