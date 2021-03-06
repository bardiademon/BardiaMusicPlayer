package com.bardiademon.BardiaMusicPlayer;

import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.BardiaMusicPlayer.controllers.PlayerController;
import com.bardiademon.BardiaMusicPlayer.models.Database;
import com.bardiademon.BardiaMusicPlayer.models.Favourites.FavouritesService;
import com.bardiademon.BardiaMusicPlayer.models.Musics.MusicsService;
import com.bardiademon.BardiaMusicPlayer.models.PlayList.PlayListService;
import com.bardiademon.BardiaMusicPlayer.models.PlayedMusicService;
import com.bardiademon.MusicPlayer.Default;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public final class Main extends Application
{

    private static Database DATABASE;

    private static final MusicsService MUSICS_SERVICE = new MusicsService ();
    private static final PlayListService PLAY_LIST_SERVICE = new PlayListService ();
    private static final FavouritesService FAVOURITES_SERVICE = new FavouritesService ();
    private static final PlayedMusicService PLAYED_MUSIC_SERVICE = new PlayedMusicService ();

    private static PlayerController playerController;

    private static Stage stage;

    @Override
    public void start (final Stage stage) throws Exception
    {
        final Parent root = FXMLLoader.load (getResourceView ("splash"));
        stage.initStyle (StageStyle.UNDECORATED);
        stage.setTitle ("Hello World");
        stage.setScene (new Scene (root));
        stage.show ();

        setStage (stage);

        new Thread (() ->
        {
            try
            {
                Thread.sleep (1000);
            }
            catch (final InterruptedException e)
            {
                Log.N (e);
            }
            Platform.runLater (() ->
            {
                stage.close ();
                System.gc ();
                PlayerController.Launch ();
            });
        }).start ();
    }

    public static URL getResourceView (final String path)
    {
        final URL url = GetResource (Main.class , "/view/" + path + ".fxml");
        if (url != null) return url;
        else
        {
            die ("Not found view!");
            return null;
        }
    }

    public static void die (final String message)
    {
        new Thread (() ->
        {
            try
            {
                final PlayerController playerController = getPlayerController ();
                if (playerController != null) playerController.diePlayer ();
            }
            catch (final Exception ignored)
            {
            }
            Platform.runLater (Platform::exit);
            System.gc ();
            System.exit (0);
            System.out.println (message);
        }).start ();

    }

    public static <T> T Launch (final String FXMLFilename , final String Title , final Controller <T> _Controller)
    {
        final URL resource = getResourceView (FXMLFilename);

        final var objController = new Object ()
        {
            T controller = null;
        };

        if (resource != null)
        {
            final FXMLLoader fxmlLoader = new FXMLLoader (resource);
            final Stage stage = new Stage ();
            stage.setResizable (false);

            stage.setTitle (Title);
            try
            {
//                Platform.runLater (() ->
//                {
//                    try
//                    {
//                        stage.getIcons ().clear ();
//                        stage.getIcons ().add (IC_IDM);
//                    }
//                    catch (final Exception e)
//                    {
//                        Log.N (e);
//                    }
//                });
                stage.setScene (new Scene (fxmlLoader.load ()));

                if (_Controller != null)
                {
                    objController.controller = fxmlLoader.getController ();
                    _Controller.GetController (objController.controller , stage);
                }

                stage.setOnCloseRequest (windowEvent -> Main.die (Default.POWERED_BY + "\nClosed the Bardia Music\n"));

                stage.show ();
            }
            catch (final IOException e)
            {
                Log.N (e);
            }
        }
        else Log.N (new Exception ("Resource is null."));

        return objController.controller;
    }

    public static void setStage (final Scene scene)
    {
        stage = new Stage ();
        stage.setScene (scene);
    }

    public static FXMLLoader GetFXMLLoader (final String fxmlName)
    {
        return (new FXMLLoader (getResourceView (fxmlName)));
    }

    public static <T> URL GetResource (final Class <T> t , final String Path)
    {
        try
        {
            final URL resource = t.getResource (Path);
            if (resource == null) throw new IOException ("Path not found <" + Path + ">");
            else return resource;
        }
        catch (final Exception e)
        {
            Log.N (e);
        }

        return null;
    }

    public interface Controller <T>
    {
        void GetController (final T t , final Stage stage);
    }

    public static void main (final String[] args) throws ClassNotFoundException
    {
        Class.forName ("org.sqlite.JDBC");
        DATABASE = new Database ();
        launch (args);
    }

    public static PlayerController getPlayerController ()
    {
        return playerController;
    }

    public static void setStage (final Stage stage)
    {
        Main.stage = stage;
    }

    public static void setPlayerController (final PlayerController playerController)
    {
        Main.playerController = playerController;
    }

    public static Database getDatabase ()
    {
        return DATABASE;
    }

    public static PlayListService getPlayListService ()
    {
        return PLAY_LIST_SERVICE;
    }

    public static MusicsService getMusicsService ()
    {
        return MUSICS_SERVICE;
    }

    public static Stage getStage ()
    {
        return stage;
    }

    public static FavouritesService getFavouritesService ()
    {
        return FAVOURITES_SERVICE;
    }

    public static PlayedMusicService getPlayedMusicService ()
    {
        return PLAYED_MUSIC_SERVICE;
    }
}
