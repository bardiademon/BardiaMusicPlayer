package com.bardiademon.BardiaMusicPlayer.models.PlayList;

import java.time.LocalDateTime;
import java.util.List;

public final class PlayListNames
{
    private long id;

    private List <PlayList> playLists;

    private String name;

    private LocalDateTime createdAt;

    public PlayListNames ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public List <PlayList> getPlayLists ()
    {
        return playLists;
    }

    public void setPlayLists (List <PlayList> playLists)
    {
        this.playLists = playLists;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
