package com.bardiademon.BardiaMusicPlayer.controllers;

import com.bardiademon.BardiaMusicPlayer.bardiademon.Log;
import com.bardiademon.MusicPlayer.BardiaPlayer;
import com.bardiademon.MusicPlayer.OnInfo;
import com.bardiademon.MusicPlayer.bardiademon.ConvertDuration;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;

import java.io.IOException;
import java.io.InputStream;

public final class Musics
{
    private long id;

    private String title, album;

    private byte[] albumImage;

    public Musics (final String MusicPath)
    {
        (new BardiaPlayer ()).onInfo (MusicPath , new OnInfo ()
        {
            @Override
            public void OnMusicTime (ConvertDuration.Time time)
            {

            }

            @Override
            public void OnName (String s)
            {

            }

            @Override
            public void OnMetadata (ID3v1 id3v1)
            {
                setTitle (id3v1.getTitle ());
                setAlbum (id3v1.getAlbum ());
            }

            @Override
            public void OnMetadata (ID3v2 id3v2)
            {
                setTitle (id3v2.getTitle ());
                setAlbum (id3v2.getAlbum ());
            }

            @Override
            public void OnMetadataError ()
            {

            }

            @Override
            public void OnAlbumImage (final InputStream inputStream)
            {
                try
                {
                    setAlbumImage (inputStream.readAllBytes ());
                }
                catch (final IOException e)
                {
                    Log.N (e);
                }
            }

            @Override
            public void OnAlbumImageError ()
            {

            }

            @Override
            public void OnCompleteInfo ()
            {

            }
        });
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

    @Override
    public String toString ()
    {
        return "";
    }
}
