package com.bardiademon.BardiaMusicPlayer.models.Musics;

import org.apache.commons.io.FilenameUtils;

import java.time.LocalDateTime;

public final class Musics
{
    private long id;
    private String path;
    private LocalDateTime addedAt;

    public Musics ()
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

    public String getPath ()
    {
        return path;
    }

    public void setPath (String path)
    {
        this.path = path;
    }

    public LocalDateTime getAddedAt ()
    {
        return addedAt;
    }

    public void setAddedAt (LocalDateTime addedAt)
    {
        this.addedAt = addedAt;
    }

    @Override
    public String toString ()
    {
        return FilenameUtils.getBaseName (getPath ());
    }
}
