package com.bardiademon.BardiaMusicPlayer.models.PlayList;

import com.bardiademon.BardiaMusicPlayer.models.Musics.Musics;

import java.time.LocalDateTime;

public final class PlayList
{
    private long id;

    private Musics musics;

    // for add
    private long musicId;

    private LocalDateTime createdAt;

    private long nameId;

    public PlayList ()
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

    public Musics getMusics ()
    {
        return musics;
    }

    public void setMusics (final Musics musics)
    {
        this.musics = musics;
    }

    public long getMusicId ()
    {
        return musicId;
    }

    public void setMusicId (long musicId)
    {
        this.musicId = musicId;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public long getNameId ()
    {
        return nameId;
    }

    public void setNameId (long idName)
    {
        this.nameId = idName;
    }
}
