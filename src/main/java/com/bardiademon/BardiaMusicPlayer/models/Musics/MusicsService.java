package com.bardiademon.BardiaMusicPlayer.models.Musics;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public final class MusicsService
{
    private static final String TBNAME = "musics";

    private enum ColumnsNames
    {
        id, path, added_at
    }

    public MusicsService ()
    {
    }

    public Musics getMusic (final long musicId)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryGetMusic () , ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY))
            {
                statement.setLong (1 , musicId);
                try (final ResultSet resultSet = statement.executeQuery ())
                {
                    if (resultSet.next ())
                    {
                        final Musics musics = new Musics ();
                        musics.setId (musicId);
                        musics.setPath (resultSet.getString (ColumnsNames.path.name ()));
                        musics.setAddedAt (resultSet.getTimestamp (ColumnsNames.added_at.name ()).toLocalDateTime ());

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

    public long addMusic (final String path)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryAddMusic () , Statement.RETURN_GENERATED_KEYS))
            {
                statement.setString (1 , path);
                statement.setTimestamp (2 , Timestamp.valueOf (LocalDateTime.now ()));
                if (statement.executeUpdate () > 0)
                {
                    try (final ResultSet resultSet = statement.getGeneratedKeys ())
                    {
                        return resultSet.getLong (1);
                    }
                }
            }
            catch (final SQLException e)
            {
                Log.N (e);
            }
        }
        else Log.N (Log.DATABASE_NOT_CONNECTED);

        return 0;
    }

    public boolean delMusic (final long id)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryDeleteMusic ()))
            {
                statement.setLong (1 , id);
                return (statement.executeUpdate () > 0) && Main.getPlayListService ().delPlayList (id);
            }
            catch (final SQLException e)
            {
                Log.N (e);
            }
        }
        else Log.N (Log.DATABASE_NOT_CONNECTED);

        return false;
    }

    private String makeQueryDeleteMusic ()
    {
        return (String.format ("delete from \"%s\" where \"%s\" = ?" , TBNAME , ColumnsNames.id));
    }

    private String makeQueryGetMusic ()
    {
        return (String.format ("select * from \"%s\" where \"%s\" = ?" , TBNAME , ColumnsNames.id));
    }

    private String makeQueryAddMusic ()
    {
        return (String.format ("insert into \"%s\" values (null,?,?)" , TBNAME));
    }
}
