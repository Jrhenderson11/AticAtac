package com.aticatac.ui.credits;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.aticatac.networking.client.UDPClient;
import com.aticatac.ui.lobby.browser.Browser;
import com.aticatac.ui.mainmenu.handlers.MainMenuAnimation;
import com.aticatac.ui.mainmenu.handlers.MainMenuKeyPressed;
import com.aticatac.ui.mainmenu.handlers.MainMenuKeyReleased;
import com.aticatac.ui.mainmenu.handlers.MainMenuMouseClicked;
import com.aticatac.ui.mainmenu.handlers.MainMenuMouseMoved;
import com.aticatac.ui.mainmenu.utils.MenuItem;
import com.aticatac.ui.quit.Quit;
import com.aticatac.ui.settings.Settings;
import com.aticatac.ui.tutorial.AiDemo;
import com.aticatac.ui.tutorial.Tutorial;
import com.aticatac.ui.tutorial.TutorialNetworked;
import com.aticatac.ui.tutorial.SinglePlayer;
import com.aticatac.ui.utils.Placeholder;
import com.aticatac.ui.utils.UIDrawer;
import com.aticatac.utils.SystemSettings;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CreditsMenu extends Scene {
    public CreditsMenu(Group root, Scene mainMenu, Stage primaryStage){
        super(root);
        ArrayList<CreditsItems> names = new ArrayList<>();
        names.add(new CreditsItems("Tom Taylor"));
        names.add(new CreditsItems("James Henderson"));
        names.add(new CreditsItems("Bianca Comanescu"));
        names.add(new CreditsItems("Safira Brilianti"));
        names.add(new CreditsItems("Lucy Griffiths"));
        names.add(new CreditsItems("Dave Jones"));

        int width = SystemSettings.getScreenWidth();
        int height = SystemSettings.getScreenHeight();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);

        CreditsAnimation animation = new CreditsAnimation(canvas, names, System.nanoTime());
        animation.start();

    }

}
