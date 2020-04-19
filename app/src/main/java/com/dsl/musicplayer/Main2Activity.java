package com.dsl.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    ListView listSongs;
    ArrayList<Song> songs;
    EditText editTitle;
    File musicFolder;
    LinearLayout songsView;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // search for songs on restore
        searchSongs(null);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main2);

        // get the list of files and place them in ListView
        listSongs = (ListView) this.findViewById(R.id.listSongs);
        editTitle = (EditText) this.findViewById(R.id.editTitle);
        songsView = (LinearLayout) this.findViewById(R.id.songsView);
        songsView.setVisibility(View.INVISIBLE);

        musicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        listSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View item,
                                    int position, long id) {

                // Play song using  built-in audio player
                String filename = songs.get(position).getFilename();
                Uri audio = Uri.parse("file://" + musicFolder + "/" + filename);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(audio, "audio/*");
                startActivity(intent);
            }
        });
    }

    public void searchSongs(View v) {
        bindSongsToListView(musicFolder, editTitle.getText().toString());
        if (songs.size() > 0)
            songsView.setVisibility(View.VISIBLE);
        else
            songsView.setVisibility(View.INVISIBLE);

    }

    private void bindSongsToListView(File musicFolder, String title) {
        songs = new ArrayList<Song>();

        ArrayList<Map<String, String>> songsMap = new ArrayList<Map<String, String>>();

        for (File f : musicFolder.listFiles()) {
            // get MediaMetaData
            MediaMetadataRetriever md = new MediaMetadataRetriever();
            md.setDataSource(musicFolder + "/" + f.getName());
            int secs = Integer.parseInt(md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
            int mins = secs / 60;
            secs = secs % 60;
            String singer = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if (singer == null || singer.equals(""))
                singer = "Unknown";
            String songtitle = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (songtitle == null)
                songtitle = f.getName();
            String duration = String.format("%02d:%02d", mins, secs);

            if (songtitle.toUpperCase().contains(title.toUpperCase())) {
                Song s = new Song();
                s.setTitle(songtitle);
                s.setFilename(f.getName());
                s.setDuration(duration);
                s.setSinger(singer);
                songs.add(s);

                Map<String, String> mapobject = convertSongToMap(s);
                songsMap.add(mapobject);
            }

        }

        SimpleAdapter adapter = new SimpleAdapter(this, songsMap, R.layout.song,
                new String[]{"title", "duration", "singer"},
                new int[]{R.id.textTitle, R.id.textDuration, R.id.textSinger});

        listSongs.setAdapter(adapter);
    }

    public Map<String, String> convertSongToMap(Song s) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title", s.getTitle());
        map.put("duration", s.getDuration());
        map.put("singer", s.getSinger());
        return map;

    }
}
