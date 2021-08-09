package com.bardiademon.BardiaMusicPlayer.controllers;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public final class OtherMusicItemController extends MFXListCell <Musics> implements Initializable
{

    @FXML
    public Text txtTitle;

    @FXML
    public Text txtAlbum;

    private FXMLLoader loader;

    @FXML
    private AnchorPane anchorPane;

    public OtherMusicItemController ()
    {
        setBackground (new Background (new BackgroundFill (Color.valueOf ("#1b1c22") , new CornerRadii (0) , new Insets (0))));
    }

    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {
    }

    @Override
    protected void updateItem (final Musics item , final boolean empty)
    {
        if (!empty || item != null)
        {
            if (this.loader == null)
            {
                Platform.runLater (() ->
                {
                    loader = Main.GetFXMLLoader ("other_music_item");
                    loader.setController (OtherMusicItemController.this);
                    try
                    {
                        loader.load ();
                        setGraphic (anchorPane);
                        txtTitle.setText (item.getTitle ());
                        txtAlbum.setText (item.getAlbum ());
                    }
                    catch (final IOException e)
                    {
                        Log.N (e);
                    }
                });
            }
        }
    }
}
