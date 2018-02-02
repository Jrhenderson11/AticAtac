package com.aticatac.ui.lobbymenu.utils;

//TODO: ClientInfo, Lobby, LobbyInfo all need to be shared by the server project @James

import java.util.ArrayList;

public class Lobby {

    private ClientInfo lobbyLeader;
    // TODO: need a better name for the people who aren't lobby leader
    private ArrayList<ClientInfo> peasants;

    public Lobby(ClientInfo lobbyLeader) {
        this.lobbyLeader = lobbyLeader;
        peasants = new ArrayList<>();
    }

    public ClientInfo getLobbyLeader() {
        return lobbyLeader;
    }

    public ArrayList<ClientInfo> getPeasants() {
        return peasants;
    }

    // Make sure not full
    public boolean addClient(ClientInfo client){
        if (peasants.size() == 3) {
            return false;
        } else {
            peasants.add(client);
            return true;
        }
    }
}
