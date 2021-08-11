package com.bardiademon.BardiaMusicPlayer.models.Favourites;

import com.bardiademon.BardiaMusicPlayer.controllers.Musics;

import java.time.LocalDateTime;

public final class Favourites
{
    private long id;

    private Musics music;

    private LocalDateTime addedAt;

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public Musics getMusic ()
    {
        return music;
    }

    public void setMusic (Musics music)
    {
        this.music = music;
    }

    public LocalDateTime getAddedAt ()
    {
        return addedAt;
    }

    public void setAddedAt (LocalDateTime addedAt)
    {
        this.addedAt = addedAt;
    }
}
