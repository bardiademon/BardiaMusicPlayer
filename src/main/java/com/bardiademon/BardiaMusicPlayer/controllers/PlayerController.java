package com.bardiademon.BardiaMusicPlayer.controllers;

import com.bardiademon.BardiaMusicPlayer.Main;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public final class PlayerController implements Initializable
{
    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {

    }

    public static void Launch ()
    {
        Main.Launch ("player" , "Player" , (Main.Controller <PlayerController>) (controller , stage) ->
        {

        });
    }
}
