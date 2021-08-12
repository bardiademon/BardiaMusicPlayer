package com.bardiademon.BardiaMusicPlayer.controllers;

import com.bardiademon.BardiaJlayer.javazoom.jl.decoder.JavaLayerException;
import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Path;
import com.bardiademon.BardiaMusicPlayer.controllers.List.FavouriteController;
import com.bardiademon.BardiaMusicPlayer.controllers.List.ListController;
import com.bardiademon.BardiaMusicPlayer.controllers.List.MusicsController;
import com.bardiademon.BardiaMusicPlayer.controllers.List.PlayedMusicController;
import com.bardiademon.BardiaMusicPlayer.models.Favourites.FavouritesService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public final class PlayerController implements Initializable, On
{

    @FXML
    public ImageView btnRepeat;

    @FXML
    public ImageView btnFavourites;

    private Image imgNoRepeat, imgOneRepeat, imgListRepeat;

    private static Repeat repeat = Repeat.list_repeat;

    private boolean sliderPlayerBlocked;

    private boolean playMusic;

    @FXML
    public MFXButton btnControlNext;

    @FXML
    public ImageView imgBtnControlPlay;

    @FXML
    public MFXButton btnControlPlay;

    @FXML
    public MFXSlider sliderVolume;

    @FXML
    public MFXButton btnControlPre;

    private BardiaPlayer player;

    private int indexPlay;

    @FXML
    public Text txtMusicTime;

    @FXML
    public MFXSlider sliderPlayer;

    @FXML
    public MFXButton btnPlayList;

    @FXML
    public MFXButton btnMusics;

    @FXML
    public MFXButton btnLstFavourites;

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
    public MFXListView <PathMusic> otherMusic;

    @FXML
    public MFXButton btnPlayer;

    final ObservableList <PathMusic> musicPath = FXCollections.observableArrayList ();

    private Image imgPlay, imgPause;

    @FXML
    public AnchorPane main, playerPane;

    private Panes panes;

    private String musicPlaying;

    @Override
    public void onProgress (int i)
    {
        if (!sliderPlayerBlocked) Platform.runLater (() -> sliderPlayer.setValue (i));
    }

    @Override
    public void onMusicTime (ConvertDuration.Time time)
    {
        Platform.runLater (() -> txtMusicTime.setText (time.toString ()));
    }

    @Override
    public void onName (String s)
    {
        Platform.runLater (() -> txtName.setText (s));
    }

    @Override
    public void onMetadata (ID3v1 id3v1)
    {
        setInfoMusic (id3v1.getArtist () , id3v1.getTitle () , id3v1.getAlbum () , id3v1.getYear ());
    }

    @Override
    public void onMetadata (ID3v2 id3v2)
    {
        setInfoMusic (id3v2.getArtist () , id3v2.getTitle () , id3v2.getAlbum () , id3v2.getYear ());
    }

    private void setInfoMusic (final String artist , final String title , final String album , final String year)
    {
        Platform.runLater (() ->
        {
            txtArtist.setText (artist);
            txtTitle.setText (title);
            txtAlbum.setText (album);
            txtYear.setText (year);
        });
    }

    @Override
    public void onMetadataError ()
    {
        setInfoMusic ("" , "" , "" , "");
    }

    @Override
    public void onAlbumImage (final InputStream inputStream)
    {
        Platform.runLater (() ->
        {
            try
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream (inputStream.available ());
                outputStream.write (inputStream.readAllBytes ());

                musicImage.setImage (new Image (new ByteArrayInputStream (outputStream.toByteArray ())));

                Platform.runLater (() ->
                {
                    Main.getStage ().getIcons ().clear ();
                    Main.getStage ().getIcons ().add (new Image (new ByteArrayInputStream (outputStream.toByteArray ())));
                });
            }
            catch (final IOException e)
            {
                Log.N (e);
            }
        });
    }

    @Override
    public void onAlbumImageError ()
    {
        final URL url = Main.GetResource (Path.R_MUSIC);
        if (url != null) Platform.runLater (() ->
        {
            try
            {
                musicImage.setImage (new Image (url.openStream ()));
            }
            catch (final IOException e)
            {
                Log.N (e);
            }
        });
    }

    @Override
    public void onCompleteInfo ()
    {

    }

    @Override
    public void onTime (ConvertDuration.Time time)
    {
        Platform.runLater (() -> txtTime.setText (time.toString ()));
    }

    @Override
    public void onStart ()
    {
        playMusic = true;

        player.setVolume ((float) sliderVolume.getValue ());

        Platform.runLater (() ->
        {
            imgBtnControlPlay.setImage (imgPlay);
            btnControlPlay.setText ("Pause");
        });

        setImageBtnFavouritesOrNot ();

        Main.getPlayedMusicService ().add (musicPlaying);
    }

    @Override
    public void onStop ()
    {
        playMusic = false;
        setBtnPause ();
    }

    @Override
    public void onPause ()
    {
        playMusic = false;
        setBtnPause ();
    }

    private void setBtnPause ()
    {
        Platform.runLater (() ->
        {
            imgBtnControlPlay.setImage (imgPause);
            btnControlPlay.setText ("Play");
        });
    }

    @Override
    public void onCompleted ()
    {
        Platform.runLater (() ->
        {
            sliderPlayer.setValue (0);
            txtTime.setText ("00:00:00");
        });

        if (repeat.equals (Repeat.list_repeat)) next ();
        else if (repeat.equals (Repeat.one_repeat)) play ();
    }

    @Override
    public void onError (final Exception e)
    {
        Log.N (e);
    }

    @Override
    public void onJavaLayerException (final JavaLayerException e)
    {
        next ();
    }

    @Override
    public void onDie ()
    {
        playMusic = false;
    }

    @FXML
    public void onClickBtnControlNext ()
    {
        next ();
    }

    @FXML
    public void onClickBtnControlPlay ()
    {
        if (player != null) player.pause ();
    }

    @FXML
    public void onClickBtnControlPre ()
    {
        pre ();
    }

    @FXML
    public void onMouseReleasedVolume ()
    {
        if (player != null)
            player.setVolume ((float) sliderVolume.getValue ());
    }

    @FXML
    public void onPressedSliderPlayer ()
    {
        sliderPlayerBlocked = true;
    }

    @FXML
    public void onClickReleasedPlayer ()
    {
        if (player != null)
            player.setProgress ((int) sliderPlayer.getValue ());

        sliderPlayerBlocked = false;
    }

    @FXML
    public void onClickBtnRepeat ()
    {
        if (repeat.equals (Repeat.list_repeat))
        {
            repeat = Repeat.one_repeat;
            if (imgOneRepeat == null) imgOneRepeat = getImage (Path.R_PR_ONE_REPEAT);
            if (imgOneRepeat != null) btnRepeat.setImage (imgOneRepeat);
        }
        else if (repeat.equals (Repeat.one_repeat))
        {
            repeat = Repeat.no_repeat;
            if (imgNoRepeat == null) imgNoRepeat = getImage (Path.R_PR_NO_REPEAT);
            if (imgNoRepeat != null) btnRepeat.setImage (imgNoRepeat);
        }
        else if (repeat.equals (Repeat.no_repeat))
        {
            repeat = Repeat.list_repeat;
            if (imgListRepeat == null) imgListRepeat = getImage (Path.R_PR_LIST_REPEAT);
            if (imgListRepeat != null) btnRepeat.setImage (imgListRepeat);
        }
    }

    private Image getImage (final String path)
    {
        final URL urlOneRepeat = Main.GetResource (path);
        if (urlOneRepeat != null)
        {
            try
            {
                return new Image (urlOneRepeat.openStream ());
            }
            catch (final IOException e)
            {
                Log.N (e);
            }
        }

        return null;
    }

    @FXML
    public void onClickBtnFavourites ()
    {
        final FavouritesService.ResultAddOrRemove resultAddOrRemove = Main.getFavouritesService ().addOrRemove (musicPlaying);
        if (resultAddOrRemove != null)
        {
            if (resultAddOrRemove.equals (FavouritesService.ResultAddOrRemove.add)) setImageBtnFavourites ();
            else setImageBtnNotFavourites ();
        }
    }

    private enum Panes
    {
        player, play_list, musics, favourites, played_music
    }

    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {

        final URL urlPlayerPlay = Main.GetResource (Path.R_PLAYER_PLAY);
        if (urlPlayerPlay != null)
        {
            try
            {
                imgPlay = new Image (urlPlayerPlay.openStream ());
            }
            catch (final IOException e)
            {
                Log.N (e);
            }
        }
        final URL urlPlayerPause = Main.GetResource (Path.R_PLAYER_PAUSE);
        if (urlPlayerPause != null)
        {
            try
            {
                imgPause = new Image (urlPlayerPause.openStream ());
            }
            catch (final IOException e)
            {
                Log.N (e);
            }
        }


        setBtnList (btnPlayList , btnMusics , btnLstFavourites , btnSelectedMusic , btnPlayedMusic , btnPlayer);
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

                otherMusic.setBackground (new Background (new BackgroundFill (Color.BLACK , new CornerRadii (0) , new Insets (0))));

                main.getChildren ().clear ();
                main.getChildren ().add (playerPane);

                Platform.runLater (() ->
                {
                    final Rectangle clip = new Rectangle (musicImage.getFitWidth () , musicImage.getFitWidth ());
                    clip.setArcWidth (50);
                    clip.setArcHeight (50);
                    musicImage.setClip (clip);
                });
                if (player != null) player.getMetadata ();
                setOtherMusic ();
            }
            catch (IOException ignored)
            {
            }
        });
    }

    private void setOtherMusic ()
    {
        if (musicPath.size () > 0)
            Platform.runLater (() -> otherMusic.setItems (musicPath));
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
        Main.Launch ("main" , "Player" , (Main.Controller <PlayerController>) (controller , stage) -> Main.setPlayerController (controller));
    }

    private void next ()
    {
        indexPlay++;
        if (indexPlay >= musicPath.size ()) indexPlay = 0;
        play ();
    }

    private void pre ()
    {
        indexPlay--;
        if (indexPlay >= 0) play ();
    }

    private void play ()
    {
        if (musicPath.size () > 0 && (indexPlay >= 0 && indexPlay < musicPath.size ()))
            play (musicPath.get (indexPlay).path);
    }

    public void play (final String path)
    {
        if (player != null) player.die ();
        player = new BardiaPlayer ();

        player.setMusic (path , this);

        musicPlaying = path;

        player.start ();

        setPlay ();
    }

    private void setPlay ()
    {
        if (otherMusic.getItems ().size () > 0)
        {
            Platform.runLater (() ->
            {
                otherMusic.getSelectionModel ().select (indexPlay);
                otherMusic.scrollTo (indexPlay);
            });
        }
    }

    @FXML
    public void onClickBtnPlayList ()
    {
        panes = Panes.play_list;
        new Thread (() -> Platform.runLater (() ->
        {
            main.getChildren ().clear ();
            final FXMLLoader playList = Main.GetFXMLLoader ("PlayList/play_list");
            try
            {
                playList.load ();
                final PlayListController playListController = playList.getController ();
                main.getChildren ().add (playListController.mainLayout);
            }
            catch (IOException ignored)
            {
            }
        })).start ();
    }

    @FXML
    public void onClickBtnMusics ()
    {
        loadListPane (Panes.musics , new MusicsController ());
    }

    @FXML
    public void onClickBtnLstFavourites ()
    {
        loadListPane (Panes.favourites , new FavouriteController ());
    }

    private void loadListPane (final Panes panes , final ListController controller)
    {
        if (!this.panes.equals (panes))
        {
            this.panes = panes;
            Platform.runLater (() ->
            {
                final FXMLLoader musics = Main.GetFXMLLoader ("List");
                try
                {
                    musics.setController (controller);

                    musics.load ();

                    main.getChildren ().clear ();
                    main.getChildren ().add (controller.mainLayout);
                }
                catch (final IOException e)
                {
                    Log.N (e);
                }
            });
        }

    }

    private void setImageBtnFavouritesOrNot ()
    {
        if (Main.getFavouritesService ().has (musicPlaying)) setImageBtnFavourites ();
        else setImageBtnNotFavourites ();
    }

    private void setImageBtnNotFavourites ()
    {
        Platform.runLater (() -> btnFavourites.setImage (getImage (Path.R_PF_NOT_LIKE)));
    }

    private void setImageBtnFavourites ()
    {
        Platform.runLater (() -> btnFavourites.setImage (getImage (Path.R_PF_LIKE)));
    }
    
    @FXML
    public void onClickBtnPlayedMusic ()
    {
        loadListPane (Panes.played_music , new PlayedMusicController ());
    }

    @FXML
    public void onClickBtnPlayer ()
    {
        loadPlayer ();
    }

    public void setMusicPath (final List <PathMusic> path)
    {
        if (path != null && path.size () > 0)
        {
            musicPath.clear ();
            musicPath.addAll (path);
            setMusic ();
        }
    }

    private void setMusic ()
    {
        indexPlay = 0;
        play ();
        setOtherMusic ();
    }

    public final static class PathMusic extends Musics
    {
        public final long id;
        public final String path;

        public PathMusic (final String MusicPath , final long id , final String path)
        {
            super (MusicPath);
            this.id = id;
            this.path = path;
        }

        public String getPath ()
        {
            return path;
        }


        @Override
        public String toString ()
        {
            return FilenameUtils.getBaseName (path);
        }
    }


    @FXML
    public void onClickOtherMusics ()
    {
        if (otherMusic.getItems ().size () > 0)
        {
            final int selectedIndex = otherMusic.getSelectionModel ().getSelectedIndex ();
            if (selectedIndex >= 0)
            {
                indexPlay = selectedIndex;
                play ();
            }
        }
    }

    private enum Repeat
    {
        no_repeat, one_repeat, list_repeat
    }
}
