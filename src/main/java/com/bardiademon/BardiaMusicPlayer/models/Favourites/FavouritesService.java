package com.bardiademon.BardiaMusicPlayer.models.Favourites;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.BardiaMusicPlayer.models.Musics.Musics;
import com.bardiademon.BardiaMusicPlayer.models.Musics.MusicsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FavouritesService
{
    private static final String TBNAME = "favourites";

    private enum ColumnsNames
    {
        id, music_id, added_at
    }

    public boolean has (final String musicPath)
    {
        final Musics music = Main.getMusicsService ().getMusic (musicPath);
        if (music != null)
        {
            if (Main.getDatabase ().connected ())
            {
                try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryHas () , ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY))
                {
                    statement.setLong (1 , music.getId ());
                    try (final ResultSet resultSet = statement.executeQuery ())
                    {
                        if (resultSet.next ()) return resultSet.getLong (1) > 0;
                    }
                }
                catch (final SQLException e)
                {
                    Log.N (e);
                }
            }
            else Log.N (Log.DATABASE_NOT_CONNECTED);
        }

        return false;
    }

    public ResultAddOrRemove addOrRemove (final String musicPath)
    {
        final Musics music = Main.getMusicsService ().getMusic (musicPath);

        final long musicId;
        if (music == null) musicId = Main.getMusicsService ().addMusic (musicPath);
        else musicId = music.getId ();

        if (musicId > 0)
            return ((has (musicPath)) ? (remove (musicId) ? ResultAddOrRemove.remove : null) : (add (musicId) ? ResultAddOrRemove.add : null));

        return null;
    }

    public ObservableList <Musics> getAll ()
    {
        if (Main.getDatabase ().connected ())
        {
            try (final Statement statement = (Main.getDatabase ().getConnection ()).createStatement (ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY))
            {
                try (final ResultSet resultSet = statement.executeQuery (makeQueryGetAll ()))
                {
                    if (resultSet.next ())
                    {
                        final ObservableList <Musics> musics = FXCollections.observableArrayList ();
                        do musics.add (Main.getMusicsService ().getMusic (resultSet));
                        while (resultSet.next ());

                        return musics;
                    }
                }
            }
            catch (final SQLException e)
            {
                Log.N (e);
            }
        }
        else Log.N (Log.DATABASE_NOT_CONNECTED);

        return null;
    }

    public enum ResultAddOrRemove
    {
        add, remove
    }

    private boolean add (final long musicId)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryAdd ()))
            {
                statement.setLong (1 , musicId);
                statement.setTimestamp (2 , Timestamp.valueOf (LocalDateTime.now ()));
                return (statement.executeUpdate () > 0);
            }
            catch (final SQLException e)
            {
                Log.N (e);
            }
        }
        else Log.N (Log.DATABASE_NOT_CONNECTED);

        return false;
    }

    private boolean remove (final long musicId)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryRemove ()))
            {
                statement.setLong (1 , musicId);
                return (statement.executeUpdate () > 0);
            }
            catch (final SQLException e)
            {
                Log.N (e);
            }
        }
        else Log.N (Log.DATABASE_NOT_CONNECTED);

        return false;
    }

    private String makeQueryRemove ()
    {
        return String.format ("delete from \"%s\" where \"%s\" = ?" , TBNAME , ColumnsNames.music_id);
    }

    private String makeQueryGetAll ()
    {
        return String.format ("select * from \"%s\" where \"%s\" in (select \"%s\" from \"%s\")" ,
                MusicsService.TBNAME , MusicsService.ColumnsNames.id , ColumnsNames.music_id , TBNAME);
    }

    private String makeQueryAdd ()
    {
        return String.format ("insert into \"%s\" values (null,?,?)" , TBNAME);
    }

    private String makeQueryHas ()
    {
        return String.format ("select count(\"%s\") from \"%s\" where \"%s\" = ?" , ColumnsNames.id , TBNAME , ColumnsNames.music_id);
    }
}
