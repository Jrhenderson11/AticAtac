package com.aticatac.ui.lobby.testserver;

import com.aticatac.lobby.ClientInfo;
import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.LobbyInfo;
import com.aticatac.lobby.LobbyServer;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class TestServer implements LobbyServer {

    private ArrayList<Lobby> lobbies;
    private ClientInfo client;

    public TestServer(ClientInfo client) {

        this.client = client;

        // Some other clients that have connected
        ClientInfo jeff = new ClientInfo("192.168.0.1", "Jeff123", false, Color.YELLOW);
        ClientInfo bob = new ClientInfo("192.168.0.2", "xX~bob~Xx", false, Color.RED);
        ClientInfo ian = new ClientInfo("192.168.0.3", "1ang0d", false, Color.BLUE);

        Lobby lobby = new Lobby(jeff);
        lobby.addClient(bob);
        lobby.addClient(ian);

        this.lobbies = new ArrayList<>();
        lobbies.add(lobby);

    }

    @Override
    public Lobby updateLobby(int id) {
        return lobbies.get(id);
    }

    @Override
    public boolean joinLobby(int id, String password) {
        lobbies.get(id).addClient(client);
        return true;
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
    public boolean changeColor(Color color) {
        return false;
    }

    @Override
    public ClientInfo myInfo() {
        return null;
    }

    @Override
    public ArrayList<LobbyInfo> getPublicLobbies() {

        ArrayList<LobbyInfo> lobbyInfos = new ArrayList<>();
        for (int i = 0; i < lobbies.size(); i++) {
            lobbyInfos.add(new LobbyInfo(4, lobbies.get(i).getPeasants().size() + 1, i, "Testing " + i));
        }

        return lobbyInfos;
    }
}
