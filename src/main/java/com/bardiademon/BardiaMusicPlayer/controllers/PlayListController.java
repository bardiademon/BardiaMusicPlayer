package com.bardiademon.BardiaMusicPlayer.controllers;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Path;
import com.bardiademon.BardiaMusicPlayer.bardiademon.ShowMessage;
import com.bardiademon.BardiaMusicPlayer.controllers.PlayList.MusicsController;
import com.bardiademon.BardiaMusicPlayer.controllers.PlayList.NewPlayListController;
import com.bardiademon.BardiaMusicPlayer.models.PlayList.PlayList;
import com.bardiademon.BardiaMusicPlayer.models.PlayList.PlayListNames;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public final class PlayListController implements Initializable
{

    @FXML
    public MFXButton btnRemovePlayList;

    @FXML
    public MFXButton btnDeleteMusic;

    @FXML
    public MFXButton btnAddMusic;

    private List <PlayListNames> allNames;

    @FXML
    public AnchorPane mainLayout;

    @FXML
    public ImageView imgBtnDelMusic;

    @FXML
    public ImageView imgBtnRemovePlayList;

    @FXML
    public ImageView imgBtnAddMusic;

    @FXML
    public ImageView imgBtnNewPlayList;

    public MFXListView <String> playListName;

    public MFXListView <Musics> listMusics;

    private final List <Musics> musics = new ArrayList <> ();

    private int selectedName;

    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {
        Platform.runLater (() -> listMusics.setCellFactory (musicsListView -> new MusicsController ()));

        playListName.setOnMouseClicked (mouseEvent -> onClickPlayListName ());

        playListRefresh ();
    }

    private void playListRefresh ()
    {
        new Thread (() ->
        {
            Platform.runLater (playListName.getItems ()::clear);
            allNames = Main.getPlayListService ().getAllNames ();
            Platform.runLater (listMusics.getItems ()::clear);

            Platform.runLater (() ->
            {
                btnAddMusic.setDisable (true);
                btnDeleteMusic.setDisable (true);
                btnRemovePlayList.setDisable (true);
            });

            musics.clear ();
            if (allNames != null)
            {
                for (final PlayListNames allName : allNames)
                    Platform.runLater (() -> playListName.getItems ().add (allName.getName ()));
            }
            else Log.N ("Play list name is empty");
        }).start ();
    }

    @FXML
    public void onClickNewPlayList ()
    {
        NewPlayListController.Launch ();
    }

    @FXML
    public void onClickBtnAddMusic ()
    {
        if (selectedName >= 0)
        {
            Platform.runLater (() ->
            {
                final FileChooser chooser = new FileChooser ();
                chooser.setTitle ("Selected music");
                chooser.getExtensionFilters ().add (new FileChooser.ExtensionFilter ("Mp3 file" , "*.mp3"));
                final List <File> files = chooser.showOpenMultipleDialog (null);
                if (files != null && files.size () > 0)
                {
                    for (final File file : files)
                    {
                        final long musicId = Main.getMusicsService ().addMusic (file.getAbsolutePath ());
                        if (musicId > 0)
                        {
                            final PlayList playList = new PlayList ();
                            playList.setMusicId (musicId);
                            playList.setCreatedAt (LocalDateTime.now ());
                            playList.setNameId (allNames.get (selectedName).getId ());
                            Main.getPlayListService ().addPlayList (playList);
                        }
                    }
                    playListRefresh ();
                }
            });
        }
    }

    @FXML
    public void onClickBtnDeletedMusic ()
    {
        final int selectedIndex;
        if (listMusics.getItems ().size () > 0 && (selectedIndex = listMusics.getSelectionModel ().getSelectedIndex ()) >= 0)
            deleteMusic (selectedIndex , musics.get (selectedIndex).getId ());
    }

    private void deleteMusic (final int selectedIndex , final long musicId)
    {
        if (Main.getMusicsService ().delMusic (musicId))
        {
            ShowMessage.Show (Alert.AlertType.INFORMATION , "Delete music" , "Music was deleted" , "Deleted");
            musics.remove (selectedIndex);
            Platform.runLater (() -> listMusics.getItems ().remove (selectedIndex));
            allNames.get (selectedName).getPlayLists ().remove (selectedIndex);
        }
        else
            ShowMessage.Show (Alert.AlertType.ERROR , "Delete music" , "Error Delete Music" , "Error");
    }

    @FXML
    public void onClickBtnRemovePlayList ()
    {
        if (selectedName >= 0)
        {
            final PlayListNames playListNames = allNames.get (selectedName);
            Platform.runLater (() ->
            {
                btnRemovePlayList.setDisable (true);
                final URL url = Main.GetResource (Path.NPL_WAITING);
                if (url != null)
                {
                    try
                    {
                        imgBtnRemovePlayList.setImage (new Image (url.openStream ()));
                    }
                    catch (IOException e)
                    {
                        Log.N (e);
                    }
                }
                btnRemovePlayList.setText ("Removing...");
            });
            for (final PlayList playList : playListNames.getPlayLists ())
                if (playList.getMusics () != null) Main.getMusicsService ().delMusic (playList.getMusics ().getId ());

            Main.getPlayListService ().delPlayListName (allNames.get (selectedName).getId ());

            allNames.remove (selectedName);
            Platform.runLater (() -> playListName.getItems ().remove (selectedName));

            Platform.runLater (() ->
            {
                btnRemovePlayList.setDisable (false);
                final URL url = Main.GetResource (Path.PLI_REMOVE_LIST);
                if (url != null)
                {
                    try
                    {
                        imgBtnRemovePlayList.setImage (new Image (url.openStream ()));
                    }
                    catch (IOException e)
                    {
                        Log.N (e);
                    }
                }
                btnRemovePlayList.setText ("Remove play list");
            });
        }
    }

    @FXML
    public void onClickPlayListName ()
    {
        if (playListName.getItems ().size () > 0 && (selectedName = playListName.getSelectionModel ().getSelectedIndex ()) >= 0)
        {
            Platform.runLater (() -> btnRemovePlayList.setDisable (false));

            final PlayListNames playListNames = allNames.get (selectedName);
            if (playListNames != null) setMusics (playListNames.getPlayLists ());
        }
    }

    private void setMusics (final List <PlayList> playLists)
    {
        Platform.runLater (() ->
        {
            btnAddMusic.setDisable (false);
            btnDeleteMusic.setDisable (false);
        });

        Platform.runLater (listMusics.getItems ()::clear);
        musics.clear ();
        for (final PlayList playList : playLists)
        {
            final Musics music = new Musics (playList.getMusics ().getPath ());
            music.setId (playList.getMusics ().getId ());
            musics.add (music);
            Platform.runLater (() -> listMusics.getItems ().add (music));
        }
    }
}
