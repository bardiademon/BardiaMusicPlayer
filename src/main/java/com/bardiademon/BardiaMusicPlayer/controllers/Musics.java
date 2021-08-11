package com.bardiademon.BardiaMusicPlayer.controllers;

import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.MusicPlayer.BardiaPlayer;
import com.bardiademon.MusicPlayer.OnInfo;
import com.bardiademon.MusicPlayer.bardiademon.ConvertDuration;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

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
