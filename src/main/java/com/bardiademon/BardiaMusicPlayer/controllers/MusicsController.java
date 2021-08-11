package com.bardiademon.BardiaMusicPlayer.controllers;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.ShowMessage;
import com.bardiademon.BardiaMusicPlayer.models.Musics.Musics;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public final class MusicsController implements Initializable
{
    @FXML
    public AnchorPane mainLayout;

    @FXML
    public MFXListView <Musics> musics;

    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {
        new Thread (() ->
        {
            final ObservableList <Musics> musics = Main.getMusicsService ().getMusics ();
            if (musics != null && musics.size () > 0)
                Platform.runLater (() -> MusicsController.this.musics.setItems (musics));
            else
                ShowMessage.Show (Alert.AlertType.ERROR , "Not found" , "Music not found" , "Musics: 0");
        }).start ();
    }

    @FXML
    public void onClickMusics ()
    {
        final int selectedIndex = musics.getSelectionModel ().getSelectedIndex ();
        if (selectedIndex >= 0)
            Main.getPlayerController ().play (musics.getSelectionModel ().getSelectedItem ().getPath ());
    }
}
