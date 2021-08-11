package com.bardiademon.BardiaMusicPlayer.bardiademon;

import java.io.File;

public final class Path
{
    public static final String DATA = Get (System.getProperty ("user.dir") , "data"),
            DB = Get (DATA , "db.sqlite"),


    IMAGES = Get (DATA , "images"),


    R_IMAGES = "\\images",
            R_MUSIC = Get (R_IMAGES , "splash-background.jpg"),

    R_PLAY_LIST = Get (R_IMAGES , "play_list"),
            R_PLAYER = Get (R_IMAGES , "player"),
            R_PLAYER_PLAY = Get (R_PLAYER , "player-play.png"),
            R_PLAYER_PAUSE = Get (R_PLAYER , "player-pause.png"),
            R_NEW_PLAY_LIST = Get (R_IMAGES , "NewPlayList"),

    // R => Resource , PR => Player Repeat
    R_P_REPEAT = Get (R_PLAYER , "Repeat"),
            R_PR_NO_REPEAT = Get (R_P_REPEAT , "no-repeat.png"),
            R_PR_ONE_REPEAT = Get (R_P_REPEAT , "one-repeat.png"),
            R_PR_LIST_REPEAT = Get (R_P_REPEAT , "list-repeat.png"),

    // R => Resource , PF => Player favourites
    R_PF = Get (R_PLAYER , "Favourites"),
            R_PF_LIKE = Get (R_PF , "favourites.png"),
            R_PF_NOT_LIKE = Get (R_PF , "not-favourites.png"),

    // NPL => New Play List
    NPL_WAITING = Get (R_NEW_PLAY_LIST , "Waiting.gif"), NPL_COMPLETED = Get (R_NEW_PLAY_LIST , "Completed.png"),

    // PLI => Play List Image
    PLI_ADD = Get (R_PLAY_LIST , "add.png"),
            PLI_ADD_LIST = Get (R_PLAY_LIST , "add-list.png"),
            PLI_REMOVE_LIST = Get (R_PLAY_LIST , "remove_list.png"),
            PLI_SELECTED_REMOVE = Get (R_PLAY_LIST , "selected_remove.png"),


    // P=> Player
    P_IC_PLAY_WHITE = Get (R_PLAYER , "ic-play-white.png");


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
