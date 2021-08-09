package com.bardiademon.BardiaMusicPlayer.models.PlayList;

import com.bardiademon.BardiaMusicPlayer.Main;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public final class PlayListService
{
    private static final String DBNAME = "play_list", DBNAME_NAMES = "play_list_name";

    private enum ColumnsNames
    {
        id, music_id, name_id, created_at
    }

    private enum ColumnsNames_Names
    {
        id, name, created_at
    }

    public PlayListService ()
    {
    }

    public List <PlayListNames> getAllNames ()
    {
        if (Main.getDatabase ().connected ())
        {
            try (final Statement statement = (Main.getDatabase ().getConnection ()).createStatement (ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY))
            {
                try (final ResultSet resultSet = statement.executeQuery (makeQueryGetAllNames ()))
                {
                    if (resultSet.next ())
                    {
                        final List <PlayListNames> playLists = new ArrayList <> ();
                        do
                        {
                            final PlayListNames playList = new PlayListNames ();
                            playList.setId (resultSet.getLong (ColumnsNames_Names.id.name ()));
                            playList.setName (resultSet.getString (ColumnsNames_Names.name.name ()));
                            playList.setCreatedAt (resultSet.getTimestamp (ColumnsNames_Names.created_at.name ()).toLocalDateTime ());
                            playList.setPlayLists (getAllFromName (playList.getId ()));

                            playLists.add (playList);
                        }
                        while (resultSet.next ());

                        return playLists;
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

    public List <PlayList> getAllFromName (final long nameId)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryGetAllFromName () , ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY))
            {
                statement.setLong (1 , nameId);

                try (final ResultSet resultSet = statement.executeQuery ())
                {
                    if (resultSet.next ())
                    {
                        final List <PlayList> playLists = new ArrayList <> ();
                        do
                        {
                            final PlayList playList = new PlayList ();
                            playList.setId (resultSet.getLong (ColumnsNames.id.name ()));
                            playList.setCreatedAt (resultSet.getTimestamp (ColumnsNames.created_at.name ()).toLocalDateTime ());
                            playList.setMusics (Main.getMusicsService ().getMusic (resultSet.getLong (ColumnsNames.music_id.name ())));
                            playLists.add (playList);
                        }
                        while (resultSet.next ());

                        return playLists;
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

    public long addListName (final PlayListNames playListName)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryAddPlayListName () , Statement.RETURN_GENERATED_KEYS))
            {
                statement.setString (1 , playListName.getName ());
                statement.setTimestamp (2 , Timestamp.valueOf (playListName.getCreatedAt ()));

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

    public long addPlayList (final PlayList playList)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryAddPlayList () , Statement.RETURN_GENERATED_KEYS))
            {
                statement.setLong (1 , playList.getNameId ());
                statement.setLong (2 , playList.getMusicId ());
                statement.setTimestamp (3 , Timestamp.valueOf (playList.getCreatedAt ()));
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

    public boolean delPlayList (final long musicId)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryDelPlayList ()))
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

    public boolean delPlayListName (final long id)
    {
        if (Main.getDatabase ().connected ())
        {
            try (final PreparedStatement statement = (Main.getDatabase ().getConnection ()).prepareStatement (makeQueryDelPlayListName ()))
            {
                statement.setLong (1 , id);
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

    private String makeQueryGetAllNames ()
    {
        return (String.format ("select * from \"%s\"" , DBNAME_NAMES));
    }

    private String makeQueryGetAllFromName ()
    {
        return (String.format ("select * from \"%s\" where \"%s\" = ?" , DBNAME , ColumnsNames.name_id));
    }

    private String makeQueryDelPlayList ()
    {
        return (String.format ("delete from \"%s\" where \"%s\" = ?" , DBNAME , ColumnsNames.music_id));
    }

    private String makeQueryDelPlayListName ()
    {
        return (String.format ("delete from \"%s\" where \"%s\" = ?" , DBNAME_NAMES , ColumnsNames_Names.id));
    }

    private String makeQueryAddPlayListName ()
    {
        return (String.format ("insert into \"%s\" values (null,?,?)" , DBNAME_NAMES));
    }

    private String makeQueryAddPlayList ()
    {
        return (String.format ("insert into \"%s\"(\"%s\",\"%s\",\"%s\") values (?,?,?)" , DBNAME , ColumnsNames.name_id , ColumnsNames.music_id , ColumnsNames.created_at));
    }

}
