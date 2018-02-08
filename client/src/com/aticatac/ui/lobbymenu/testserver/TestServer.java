package com.aticatac.ui.lobbymenu.testserver;

import com.aticatac.lobby.utils.ClientInfo;
import com.aticatac.lobby.utils.Lobby;
import com.aticatac.lobby.utils.LobbyInfo;
import com.aticatac.lobby.utils.LobbyServer;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class TestServer implements LobbyServer {

    private ArrayList<Lobby> lobbies;
    private ClientInfo client;

    public TestServer(ClientInfo client) {

        this.client = client;
        /*
        ClientInfo jeff = new ClientInfo("192.168.0.1", "Jeff123", false, 2);
        ClientInfo bob = new ClientInfo("192.168.0.2", "xX~bob~Xx", false, 3);
        ClientInfo ian = new ClientInfo("192.168.0.3", "1ang0d", false, 4);

        Lobby lobby = new Lobby(jeff);
        lobby.addClient(bob);
        lobby.addClient(ian);
         */
        this.lobbies = new ArrayList<>();
    //    lobbies.add(lobby);

    }

    @Override
    public Lobby joinLobby(int id, String password) {
        lobbies.get(id).addClient(client);
        return lobbies.get(id);
    }

    @Override
    public void readyUp() {
        client.ready();
    }

    @Override
    public void unready() {
        client.unready();
    }

    @Override
    public boolean startGame() {
        return false; // Not lobby leader for the mo
    }

    @Override
    public boolean leaveLobby() {
        lobbies.get(1).removeClient(client);
        return true;
    }

    @Override
    public ArrayList<LobbyInfo> getPublicLobbies() {
        return null;
    }
}
