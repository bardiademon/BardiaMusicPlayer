package com.bardiademon.BardiaMusicPlayer.controllers.List;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.ShowMessage;
import com.bardiademon.BardiaMusicPlayer.models.Favourites.FavouritesService;
import com.bardiademon.BardiaMusicPlayer.models.Musics.Musics;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public final class FavouriteController extends ListController
{
    @Override
    public void initialize (URL url , ResourceBundle resourceBundle)
    {
        setList (Main.getFavouritesService ().getAll ());
    }

    @FXML
    @Override
    public void onClickList (final MouseEvent mouseEvent)
    {
        final int selectedIndex = list.getSelectionModel ().getSelectedIndex ();
        if (selectedIndex >= 0)
        {
            if (mouseEvent.getButton ().equals (MouseButton.SECONDARY))
            {
                if ((Main.getFavouritesService ().addOrRemove (list.getItems ().get (selectedIndex).getPath ())).equals (FavouritesService.ResultAddOrRemove.remove))
                {
                    ShowMessage.Show (Alert.AlertType.INFORMATION , "Remove" , "Remove from favourites" , "Remove favourites");
                    Platform.runLater (() -> list.getItems ().remove (selectedIndex));
                }
            }
            else super.onClickList (mouseEvent);
        }
    }
}
