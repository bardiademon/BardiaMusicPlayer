package com.bardiademon.BardiaMusicPlayer.controllers;

import org.apache.commons.io.FilenameUtils;

public class Musics
{
    private long id;

    private String title, album;

    private byte[] albumImage;

    private String path;

    public Musics (final String MusicPath)
    {
        this.path = MusicPath;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getAlbum ()
    {
        return album;
    }

    public void setAlbum (String album)
    {
        this.album = album;
    }

    public byte[] getAlbumImage ()
    {
        return albumImage;
    }

    public void setAlbumImage (byte[] albumImage)
    {
        this.albumImage = albumImage;
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

    @Override
    public String toString ()
    {
        return FilenameUtils.getBaseName (getPath ());
    }
}
