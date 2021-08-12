package com.bardiademon.BardiaMusicPlayer.controllers.List;

import com.bardiademon.BardiaMusicPlayer.Main;

import java.net.URL;
import java.util.ResourceBundle;

public final class PlayedMusicController extends ListController
{
    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {
        setList (Main.getPlayedMusicService ().getAll ());
    }
}
