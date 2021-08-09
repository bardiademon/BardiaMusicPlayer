package com.bardiademon.BardiaMusicPlayer.controllers.PlayList;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.BardiaMusicPlayer.controllers.Musics;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicsController extends MFXListCell <Musics> implements Initializable
{

    @FXML
    public ImageView imageAlbum;

    @FXML
    public Text txtTitle;

    @FXML
    public Text txtAlbum;

    @FXML
    public AnchorPane mainLayout;

    private FXMLLoader loader;

    public MusicsController ()
    {
        setBackground (new Background (new BackgroundFill (Color.valueOf ("#1b1c22") , new CornerRadii (0) , new Insets (0))));
    }

    @Override
    protected void updateItem (final Musics item , final boolean empty)
    {
        super.updateItem (item , empty);
        if (!empty && item != null)
        {
            Platform.runLater (() ->
            {
                loader = Main.GetFXMLLoader ("PlayList/MusicItem");
                loader.setController (MusicsController.this);
                try
                {
                    loader.load ();
                }
                catch (final IOException e)
                {
                    Log.N (e);
                }
                setGraphic (mainLayout);

                txtTitle.setText (item.getTitle ());
                txtAlbum.setText (item.getAlbum ());

                final byte[] albumImage = item.getAlbumImage ();
                if (albumImage != null && albumImage.length > 0)
                    imageAlbum.setImage (new Image (new ByteArrayInputStream (albumImage)));
            });
        }
        else
        {
            setGraphic (null);
            setText (null);
        }
    }

    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {

    }
}
