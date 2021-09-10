package com.bardiademon.BardiaMusicPlayer.controllers.PlayList;

import bardiademon.FindAllFile;
import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Path;
import com.bardiademon.BardiaMusicPlayer.bardiademon.ShowMessage;
import com.bardiademon.BardiaMusicPlayer.models.PlayList.PlayList;
import com.bardiademon.BardiaMusicPlayer.models.PlayList.PlayListNames;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXTreeItem;
import io.github.palexdev.materialfx.controls.MFXTreeView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class NewPlayListController implements Initializable
{
    private long counterFindFile;

    @FXML
    public MFXButton btnAdd;

    @FXML
    public ImageView imtBtnAdd;

    @FXML
    public MFXButton btnCancel;

    @FXML
    public MFXButton btnDirectoryChoose;

    @FXML
    public MFXButton btnFileChoose;

    @FXML
    public Text txtResult;

    private int index;
    private List <Long> musicsIds;

    private final List <File> files = new ArrayList <> ();

    private File lastDirChoose, lastFileChoose;

    private Stage stage;

    private String listName;

    @FXML
    public MFXTextField txtListName;

    @FXML
    public MFXCheckbox chkSelectToRemove;

    @FXML
    public MFXTreeView <String> explorer;

    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {
        explorer.setRoot (new MFXTreeItem <> ("Explorer"));
    }

    public static void Launch ()
    {
        Platform.runLater (() -> Main.Launch ("PlayList/NewPlayList" , "New play list" , (Main.Controller <NewPlayListController>) (controller , stage) -> controller.stage = stage));
    }

    @FXML
    public void onClickBtnDirectoryChoose ()
    {
        Platform.runLater (() ->
        {
            final DirectoryChooser directoryChooser = new DirectoryChooser ();
            if (lastDirChoose != null) directoryChooser.setInitialDirectory (lastDirChoose);
            directoryChooser.setTitle ("Select music directory");
            final File selectedDir = directoryChooser.showDialog (null);
            if (selectedDir != null)
            {
                lastDirChoose = selectedDir;
                files.add (selectedDir);
                explorer.getRoot ().getItems ().add (new MFXTreeItem <> (selectedDir.getName ()));
            }
        });
    }

    @FXML
    public void onClickBtnFileChoose ()
    {
        Platform.runLater (() ->
        {
            final FileChooser fileChooser = new FileChooser ();
            if (lastFileChoose != null) fileChooser.setInitialDirectory (lastFileChoose);
            fileChooser.setTitle ("Select music file");
            fileChooser.getExtensionFilters ().add (new FileChooser.ExtensionFilter ("Mp3 file" , "*.mp3"));
            final List <File> selectedFile = fileChooser.showOpenMultipleDialog (null);
            if (selectedFile != null && selectedFile.size () > 0)
            {
                lastFileChoose = selectedFile.get (0).getParentFile ();
                for (final File file : selectedFile)
                {
                    if (!file.isDirectory ())
                    {
                        explorer.getRoot ().getItems ().add (new MFXTreeItem <> (file.getName ()));
                        files.add (file);
                    }
                }
            }
        });
    }

    @FXML
    public void onClickBtnAdd ()
    {
        listName = txtListName.getText ();
        if (listName != null && !listName.isEmpty ())
        {
            if (files.size () > 0)
            {
                counterFindFile = 0;
                changeBtnAdd ("Adding..." , Path.NPL_WAITING , true);

                musicsIds = new ArrayList <> ();

                for (final File file : files)
                {
                    if (file.isDirectory ())
                    {
                        new Thread (() -> new FindAllFile (new FindAllFile.CallBack ()
                        {
                            @Override
                            public void FindTimeFile (long NumberOfFileFind , FindAllFile.Find FileFind)
                            {
                                if (checkMusic (FileFind.file.getAbsolutePath ()))
                                {
                                    final long id = Main.getMusicsService ().addMusic (FileFind.file.getAbsolutePath ());
                                    if (id > 0)
                                    {
                                        setCounterFindFile ();
                                        musicsIds.add (id);
                                    }
                                }
                            }

                            @Override
                            public void FindTimeDir (long NumberOfDirFind , File DirFind)
                            {

                            }

                            @Override
                            public void FindTimeFileOrDir (long NumberOfFileFind , long NumberOfDirFind , String FindFileOrDir , FindAllFile.Find FileFind , File DirFind)
                            {

                            }

                            @Override
                            public void AfterFindFile (long NumberOfFileFind , List <FindAllFile.Find> FindList)
                            {
                                index++;
                                finish ();
                            }

                            @Override
                            public void AfterFindDir (long NumberOfDirFind , List <File> FindList)
                            {

                            }

                            @Override
                            public boolean Error (String ErrorType)
                            {
                                return false;
                            }
                        } , file)).start ();
                    }
                    else
                    {
                        if (checkMusic (file.getAbsolutePath ()))
                        {
                            final long id = Main.getMusicsService ().addMusic (file.getAbsolutePath ());
                            if (id > 0)
                            {
                                setCounterFindFile ();
                                musicsIds.add (id);
                            }
                        }
                        index++;
                    }
                }
                finish ();
            }
            else ShowMessage.Show (Alert.AlertType.ERROR , "Not selected" , "Files not selected" , "Not selected");
        }
        else ShowMessage.Show (Alert.AlertType.ERROR , "List name" , "List name is empty" , "Empty");
    }

    private boolean checkMusic (final String path)
    {
        final String extension = FilenameUtils.getExtension (path);
        return (extension != null && !extension.isEmpty () && extension.toLowerCase (Locale.ROOT).equals ("mp3"));
    }

    public void setCounterFindFile ()
    {
        counterFindFile++;
        Platform.runLater (() -> txtResult.setText (String.format ("Founded: %d" , counterFindFile)));
    }

    public void setResultAdded (final long id)
    {
        Platform.runLater (() -> txtResult.setText (String.format ("Added... Id: %s" , id)));
    }

    private void finish ()
    {
        if (index >= files.size ())
        {
            final PlayListNames playListName = new PlayListNames ();
            playListName.setCreatedAt (LocalDateTime.now ());
            playListName.setName (listName);

            playListName.setId (Main.getPlayListService ().addListName (playListName));

            if (playListName.getId () > 0)
            {
                changeBtnAdd ("Added play list..." , Path.NPL_WAITING , true);

                counterFindFile = 0;

                for (final long musicsId : musicsIds)
                {
                    final PlayList playList = new PlayList ();
                    playList.setCreatedAt (LocalDateTime.now ());
                    playList.setMusicId (musicsId);
                    playList.setNameId (playListName.getId ());
                    setResultAdded (Main.getPlayListService ().addPlayList (playList));
                }
                ShowMessage.Show (Alert.AlertType.INFORMATION , "Play list" , "Play list added" , "Added");
                changeBtnAdd ("New play list" , Path.NPL_COMPLETED , false);
                musicsIds.clear ();
                files.clear ();

                onClickBtnCancel ();
            }
            else
            {
                changeBtnAdd ("New play list" , Path.NPL_COMPLETED , false);
                ShowMessage.Show (Alert.AlertType.ERROR , "Error" , "Error added list name" , "Error added");
            }
        }
    }

    private void changeBtnAdd (final String text , final String imagePath , final boolean disable)
    {
        Platform.runLater (() ->
        {
            btnAdd.setText (text);
            btnAdd.setDisable (disable);

            btnCancel.setDisable (disable);
            btnDirectoryChoose.setDisable (disable);
            btnFileChoose.setDisable (disable);
            chkSelectToRemove.setDisable (disable);
            txtListName.setDisable (disable);

            if (!disable) txtResult.setText ("");

            try
            {
                final URL urlWaiting = Main.GetResource (NewPlayListController.class , imagePath);
                if (urlWaiting != null) imtBtnAdd.setImage (new Image (urlWaiting.openStream ()));
            }
            catch (final IOException e)
            {
                Log.N (e);
            }
        });
    }

    @FXML
    public void onClickBtnCancel ()
    {
        files.clear ();
        Platform.runLater (() ->
        {
            stage.close ();
            stage.hide ();
            System.gc ();
        });
    }

    @FXML
    public void onClickExplorer ()
    {
        if (explorer.getRoot ().getItems ().size () > 0 && !chkSelectToRemove.isDisabled ())
        {
            try
            {
                final int index = (int) explorer.getSelectionModel ().getSelectedItem ().getIndex ();
                if (index > 0)
                {

                    final String name = files.get (index).getName ();
                    files.remove (index);
                    explorer.getRoot ().getItems ().remove (index);
                    ShowMessage.Show (Alert.AlertType.INFORMATION , "Deleted" , "Delete from list" , "Deleted: " + name);
                }
            }
            catch (final Exception e)
            {
                Log.N (e);
            }
            finally
            {
                System.gc ();
            }
        }
    }
}
