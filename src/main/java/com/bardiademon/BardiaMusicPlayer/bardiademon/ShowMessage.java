package com.bardiademon.BardiaMusicPlayer.bardiademon;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public final class ShowMessage
{
    private ShowMessage ()
    {
    }

    public static void Show (Alert.AlertType _AlertType , final String Title , final String Header , final String Content)
    {
        Platform.runLater (() ->
        {
            try
            {
                final Alert alert = new Alert (_AlertType);
                alert.setTitle (Title);
                alert.setContentText (Content);
                alert.setHeaderText (Header);
                alert.show ();
                System.gc ();
            }
            catch (final Exception e)
            {
                Log.N (e);
            }
        });
    }
}
