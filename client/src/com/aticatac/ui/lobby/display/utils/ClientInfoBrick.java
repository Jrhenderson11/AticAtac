package com.aticatac.ui.lobby.display.utils;

import com.aticatac.Utils;
import com.aticatac.lobby.ClientInfo;
import com.aticatac.ui.lobby.display.Displayer;
import com.aticatac.ui.utils.Drawable;
import com.aticatac.utils.SystemSettings;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

import static java.lang.StrictMath.sin;

public class ClientInfoBrick implements Drawable {

    private final int offset;
    private final Optional<KickButton> kbutton;
    private ClientInfo mine;

    public ClientInfoBrick(ClientInfo info, int i, boolean isLead) {
        Optional<KickButton> kbutton1;
        this.mine = info;
        this.offset = i;
        kbutton1 = Optional.empty();
        if (isLead) {
            KickButton kick = new KickButton(offset);
            Displayer.getButtons().add(kick);
            Displayer.getDrawables().add(kick);
            kbutton1 = Optional.of(kick);

            int width = SystemSettings.getNativeWidth();
            int height = SystemSettings.getNativeHeight();

            int x = width / 10;
            int y = (1 + offset) * height / 6;
            int w = 8 * width / 10;
            int h = height / 6;
            kick.setHitbox(new Rectangle(x + w * 1.1, y + h * 0.2, w * 0.2, h * 0.6));
        }
        this.kbutton = kbutton1;
    }

    @Override
    public void draw(GraphicsContext gc, long now) {

        int width = SystemSettings.getNativeWidth();
        int height = SystemSettings.getNativeHeight();
        double animation = (double) now / 500000000;

        // Container
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.gray(0.1));
        int x = width / 10;
        int y = (1 + offset) * height / 6;
        int w = 8 * width / 10;
        int h = height / 6;
        gc.strokeRect(x, y, w, h);
        gc.fillRect(x, y, w, h);

        // info

        //      username
        String username = mine.getID();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.strokeText(username, x + w * 0.1, y + h * 0.55);
        gc.fillText(username, x + w * 0.1, y + h * 0.55);

        //      color
        int color = mine.getColour();
        Color colorp = Utils.intToColor(color);
        gc.setFill(colorp);
        gc.setStroke(Color.BLACK);
        gc.fillOval(x + w * 0.4, y + h * 0.15, h * 0.7, h * 0.7);
        gc.strokeOval(x + w * 0.4, y + h * 0.15, h * 0.7, h * 0.7);

        //      ready
        boolean ready = mine.isReady();

        Color readyC = Color.RED;
        if (ready) {
            readyC = Color.rgb(0, (int) (200 + 50 * sin(animation)), 0);
        }

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);
        gc.strokeText("Ready:", x + w * 0.63, y + h * 0.55);
        gc.fillText("Ready:", x + w * 0.63, y + h * 0.55);

        gc.setFill(readyC);
        gc.setStroke(Color.BLACK);
        gc.fillOval(x + w * 0.8, y + h * 0.3, h * 0.4, h * 0.4);
        gc.strokeOval(x + w * 0.8, y + h * 0.3, h * 0.4, h * 0.4);

        // Kick

        // WOAH JAVA 8!!!1!!11!
        kbutton.ifPresent(kickButton -> {
            kickButton.draw(gc, now);
        });

    }
}
