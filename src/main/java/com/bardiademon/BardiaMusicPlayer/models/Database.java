package com.bardiademon.BardiaMusicPlayer.models;


import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.BardiaMusicPlayer.bardiademon.Path;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database
{
    private static final String URL_CONNECTION = "jdbc:sqlite:" + Path.DB;
    private Connection connection;

    public Database ()
    {
        connect ();
    }

    private void connect ()
    {
        try
        {
            connection = DriverManager.getConnection (URL_CONNECTION);
        }
        catch (final SQLException e)
        {
            Log.N (e);
        }
    }

    public void reconnect ()
    {
        if (connected ())
        {
            try
            {
                connection.close ();
            }
            catch (final SQLException e)
            {
                Log.N (e);
            }
            System.gc ();
            connect ();
        }
    }

    public Connection getConnection ()
    {
        return connection;
    }

    public boolean connected ()
    {
        return connected (true);
    }

    private boolean connected (final boolean reconnect)
    {
        try
        {
            if (connection != null && !connection.isClosed ()) return true;
            else if (reconnect)
            {
                connect ();
                return connected (false);
            }
            else return false;
        }
        catch (final SQLException e)
        {
            Log.N (e);
            return false;
        }
    }
}
