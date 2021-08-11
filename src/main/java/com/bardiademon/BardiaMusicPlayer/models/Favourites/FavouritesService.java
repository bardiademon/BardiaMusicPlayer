package com.bardiademon.BardiaMusicPlayer.models.Favourites;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.BardiaMusicPlayer.models.Musics.Musics;
import com.bardiademon.BardiaMusicPlayer.models.Musics.MusicsService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public enum ResultAddOrRemove
    {
        add, remove
    }

    private boolean add (final long musicPath)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryAdd ()))
            {
                statement.setLong (1 , musicPath);
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

    private boolean remove (final long musicPath)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryRemove ()))
            {
                statement.setLong (1 , musicPath);
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

    private String makeQueryAdd ()
    {
        return String.format ("insert into \"%s\" values (null,?,?)" , TBNAME);
    }

    private String makeQueryHas ()
    {
        return String.format ("select count(\"%s\") from \"%s\" where \"%s\" = ?" , ColumnsNames.id , TBNAME , ColumnsNames.music_id);
    }
}
