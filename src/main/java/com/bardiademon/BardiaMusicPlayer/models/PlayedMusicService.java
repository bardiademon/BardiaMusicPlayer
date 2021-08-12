package com.bardiademon.BardiaMusicPlayer.models;

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

public final class PlayedMusicService
{

    private static final String TNAME = "played_music";

    private enum ColumnsNames
    {
        id, music_id, played_at
    }

    public boolean add (final String path)
    {
        if (Main.getDatabase ().connected ())
        {
            final Musics music = Main.getMusicsService ().getMusic (path);

            final long musicId;
            if (music == null) musicId = Main.getMusicsService ().addMusic (path);
            else musicId = music.getId ();

            try (final PreparedStatement statement = ((Main.getDatabase ()).getConnection ()).prepareStatement (makeQueryAdd ()))
            {
                statement.setLong (1 , musicId);
                statement.setTimestamp (2 , Timestamp.valueOf (LocalDateTime.now ()));
                return (statement.executeUpdate () > 0);
            }
            catch (final SQLException e)
            {
                e.printStackTrace ();
                Log.N (e);
            }
        }
        else Log.N (Log.DATABASE_NOT_CONNECTED);

        return false;
    }

    public ObservableList <Musics> getAll ()
    {
        if (Main.getDatabase ().connected ())
        {
            try (final Statement statement = ((Main.getDatabase ()).getConnection ()).createStatement ())
            {
                try (final ResultSet resultSet = statement.executeQuery (makeQueryGetAll ()))
                {
                    if (resultSet.next ())
                    {
                        final ObservableList <Musics> musics = FXCollections.observableArrayList ();
                        do
                        {
                            musics.add (Main.getMusicsService ().getMusic (resultSet));
                        }
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

    private String makeQueryAdd ()
    {
        return (String.format ("insert into \"%s\" values (null,?,?)" , TNAME));
    }

    private String makeQueryGetAll ()
    {
        return (String.format ("select * from \"%s\" where \"%s\" in (select \"%s\" from \"%s\")" , MusicsService.TBNAME , MusicsService.ColumnsNames.id , ColumnsNames.music_id , TNAME));
    }
}
