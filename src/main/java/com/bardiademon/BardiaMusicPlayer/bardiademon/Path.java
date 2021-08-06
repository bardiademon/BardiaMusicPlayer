package com.bardiademon.BardiaMusicPlayer.bardiademon;

import java.io.File;

public final class Path
{
    public static final String DATA = Get (System.getProperty ("user.dir") , "data"),
            DB = Get (DATA , "db.sqlite"),


    IMAGES = Get (DATA , "images");
//            IC_APP = Get (IMAGES , "ic_idm.png");

    public static String Get (String... paths)
    {
        final StringBuilder finalPath = new StringBuilder ();
        for (int i = 0, len = paths.length; i < len; i++)
        {
            finalPath.append (paths[i]);
            if ((i + 1) < len) finalPath.append (File.separator);
        }
        return finalPath.toString ();
    }

    private Path ()
    {
    }
}
