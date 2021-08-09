package com.bardiademon.BardiaMusicPlayer.controllers;

import com.bardiademon.BardiaJlayer.javazoom.jl.decoder.JavaLayerException;
import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.MusicPlayer.BardiaPlayer;
import com.bardiademon.MusicPlayer.On;
import com.bardiademon.MusicPlayer.bardiademon.ConvertDuration;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public final class PlayerController implements Initializable
{
    @FXML
    public ImageView btnPlay;

    @FXML
    public Text txtMusicTime;

    @FXML
    public MFXSlider sliderPlayer;

    @FXML
    public MFXButton btnPlayList;

    @FXML
    public MFXButton btnMusics;

    @FXML
    public MFXButton btnFavourites;

    @FXML
    public MFXButton btnSelectedMusic;

    @FXML
    public MFXButton btnPlayedMusic;

    @FXML
    public ImageView musicImage;

    @FXML
    public Text txtName;

    @FXML
    public Text txtArtist;

    @FXML
    public Text txtAlbum;

    @FXML
    public Text txtTitle;

    @FXML
    public Text txtYear;

    @FXML
    public Text txtTime;

    @FXML
    private AnchorPane playerPane;

    @FXML
    public MFXListView <Musics> otherMusic;

    @FXML
    public MFXButton btnPlayer;

    final ObservableList <Musics> musics = FXCollections.observableArrayList ();

    private static final List <String> musicPath = new ArrayList <> ();

    @FXML
    public AnchorPane main;

    private Panes panes;

    private enum Panes
    {
        player, play_list
    }

    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {
        setBtnList (btnPlayList , btnMusics , btnFavourites , btnSelectedMusic , btnPlayedMusic , btnPlayer);
        if (panes == null || !panes.equals (Panes.player))
        {
            loadPlayer ();
            panes = Panes.player;
        }
    }

    private void loadPlayer ()
    {
        Platform.runLater (() ->
        {
            final FXMLLoader fxmlLoader = Main.GetFXMLLoader ("player");
            try
            {
                fxmlLoader.setController (PlayerController.this);
                fxmlLoader.load ();
                main.getChildren ().clear ();
                main.getChildren ().add (playerPane);

                Platform.runLater (() ->
                {
                    final Rectangle clip = new Rectangle (musicImage.getFitWidth () , musicImage.getFitWidth ());
                    clip.setArcWidth (50);
                    clip.setArcHeight (50);
                    musicImage.setClip (clip);
                });
                setOtherMusic ();
            }
            catch (IOException ignored)
            {
            }
        });
    }

    private void setOtherMusic ()
    {
        musics.clear ();
        if (musicPath.size () > 0)
        {
            otherMusic.setOrientation (Orientation.HORIZONTAL);
            otherMusic.setItems (musics);
            otherMusic.setBackground (new Background (new BackgroundFill (Color.BLACK , new CornerRadii (0) , new Insets (0))));
            otherMusic.setCellFactory (otherMusicItemListView -> new OtherMusicItemController ());
        }
    }

    private void setBtnList (final MFXButton... btns)
    {
        for (final MFXButton btn : btns)
        {
            Platform.runLater (() ->
            {
                btn.setTextFill (Color.WHITE);
                btn.setGraphicTextGap (10);
                btn.setBackground (new Background (new BackgroundFill (Color.valueOf ("#1b1c22") , new CornerRadii (100 , true) , new Insets (100 , 10 , 10 , 10))));
            });
        }
    }

    public static void Launch ()
    {
        Main.Launch ("main" , "Player" , (Main.Controller <PlayerController>) (controller , stage) ->
        {

        });
    }

    @FXML
    public void onClickBtnPlay ()
    {
        Platform.runLater (() ->
        {
            final BardiaPlayer player = new BardiaPlayer ();

            player.SetMusic ("E:\\Saman - Dige Rafte_(Music-o-Movie.ir).mp3" , new On ()
            {
                @Override
                public void OnProgress (int i)
                {
                    Platform.runLater (() -> sliderPlayer.setValue (i));
                }

                @Override
                public void OnMusicTime (ConvertDuration.Time time)
                {
                    Platform.runLater (() -> txtMusicTime.setText (time.toString ()));
                }

                @Override
                public void OnTime (ConvertDuration.Time time)
                {
                    Platform.runLater (() -> txtTime.setText (time.toString ()));
                }

                @Override
                public void OnStart ()
                {

                }

                @Override
                public void OnStop ()
                {

                }

                @Override
                public void OnPause ()
                {

                }

                @Override
                public void OnCompleted ()
                {

                }

                @Override
                public void OnError (Exception e)
                {

                }

                @Override
                public void OnJavaLayerException (JavaLayerException e)
                {

                }

                @Override
                public void OnDie ()
                {

                }

                @Override
                public void OnName (String s)
                {
                    Platform.runLater (() -> txtName.setText (s));
                }

                @Override
                public void OnMetadata (ID3v1 id3v1)
                {
                    Platform.runLater (() ->
                    {
                        txtAlbum.setText (id3v1.getAlbum ());
                        txtTitle.setText (id3v1.getTitle ());
                        txtYear.setText (id3v1.getYear ());
                        txtArtist.setText (id3v1.getArtist ());
                    });
                }

                @Override
                public void OnMetadata (ID3v2 id3v2)
                {
                    Platform.runLater (() ->
                    {
                        txtAlbum.setText (id3v2.getAlbum ());
                        txtTitle.setText (id3v2.getTitle ());
                        txtYear.setText (id3v2.getYear ());
                        txtArtist.setText (id3v2.getArtist ());
                    });
                }

                @Override
                public void OnMetadataError ()
                {

                }

                @Override
                public void OnAlbumImage (InputStream inputStream)
                {
                    musicImage.setImage (new Image (inputStream));
                }

                @Override
                public void OnAlbumImageError ()
                {

                }

                @Override
                public void OnCompleteInfo ()
                {

                }
            });

            player.Start ();
        });
    }

    @FXML
    public void onClickBtnPre ()
    {

    }

    @FXML
    public void onClickBtnNext ()
    {

    }

    @FXML
    public void onClickBtnPlayList ()
    {
        panes = Panes.play_list;
        new Thread (() -> Platform.runLater (() ->
        {
            playerPane.getChildren ().clear ();
            final FXMLLoader playList = Main.GetFXMLLoader ("PlayList/play_list");
            try
            {
                playList.load ();
                final PlayListController playListController = playList.getController ();
                playerPane.getChildren ().add (playListController.mainLayout);
            }
            catch (IOException ignored)
            {
            }
        })).start ();

    }

    @FXML
    public void onClickBtnMusics ()
    {

    }

    @FXML
    public void onClickBtnFavourites ()
    {

    }

    @FXML
    public void onClickBtnSelectedMusic ()
    {

    }

    @FXML
    public void onClickBtnPlayedMusic ()
    {

    }

    @FXML
    public void onClickBtnPlayer ()
    {

    }

    private double otherMouseX;

    @FXML
    public void onClickMouseDraggedOtherMusic (final MouseEvent mouseEvent)
    {
        if (otherMusic.getItems ().size () > 0 && !otherMusic.isHideScrollBars ())
        {
            if (otherMouseX > mouseEvent.getX ()) otherMusic.getFocusModel ().focusPrevious ();
            else if (otherMouseX < mouseEvent.getX ()) otherMusic.getFocusModel ().focusNext ();

            otherMouseX = mouseEvent.getX ();

            Platform.runLater (() -> otherMusic.scrollTo (otherMusic.getFocusModel ().getFocusedIndex ()));
        }
    }

    @FXML
    public void onClickMousePressedOtherMusic (final MouseEvent mouseEvent)
    {
        if (otherMusic.getItems ().size () > 0 && !otherMusic.isHideScrollBars ())
            otherMouseX = mouseEvent.getX ();
    }

}
