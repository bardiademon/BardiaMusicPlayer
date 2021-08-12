package com.bardiademon.BardiaMusicPlayer.controllers.List;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.ShowMessage;
import com.bardiademon.BardiaMusicPlayer.models.Musics.Musics;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ListController implements Initializable
{
    @FXML
    public AnchorPane mainLayout;

    @FXML
    public MFXListView <Musics> list;

    @Override
    public void initialize (final URL url , final ResourceBundle resourceBundle)
    {

    }

    protected void setList (final ObservableList <Musics> observableList)
    {
        if (observableList != null && observableList.size () > 0)
            Platform.runLater (() -> list.setItems (observableList));
        else
            ShowMessage.Show (Alert.AlertType.ERROR , "Not found" , "List is empty" , "List: 0");
    }

    @FXML
    public void onClickList (final MouseEvent mouseEvent)
    {
        final int selectedIndex = list.getSelectionModel ().getSelectedIndex ();
        if (selectedIndex >= 0)
            Main.getPlayerController ().play (list.getSelectionModel ().getSelectedItem ().getPath ());
    }
}
