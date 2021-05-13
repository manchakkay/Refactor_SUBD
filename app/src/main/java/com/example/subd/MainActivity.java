package com.example.subd;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv; // не забудьте привязать переменную (findViewById)
    SimpleCursorAdapter adapter; // объявлен в классе, чтобы был доступен вл всех методах

    TextView title;
    TextView author;
    TextView year;
    TextView duration;
    TextView count;
    TextView wholeDuration;

    Button add;

    Button sortTitle;
    Button sortAuthor;
    Button sortYear;
    Button sortDuration;

    String titleStr = "title";
    String authorStr = "artist";
    String yearStr = "year";
    String durationStr = "duration";
    String table = "playlist";

    int total;
    
    DBHelperWithLoader helper1;
    SQLiteDatabase musicDB;

    Boolean checkerTitle;
    Boolean checkerAuthor;
    Boolean checkerYear;
    Boolean checkerDuration;

    Cursor tunes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        helper1 = new DBHelperWithLoader(this);
        musicDB = helper1.getWritableDatabase();

        tunes = musicDB.rawQuery("SELECT * FROM playlist", null);
        String[] playlist_fields = tunes.getColumnNames();

        count.setText(String.valueOf(tunes.getCount()));


        Cursor cursor = musicDB.rawQuery("SELECT SUM(duration) as Total FROM playlist", null);
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        wholeDuration.setText(durationFormat(total));

        int[] views = { R.id.itemId, R.id.itemAuthor, R.id.itemTitle, R.id.itemYear, R.id.itemDuration };

        adapter = new SimpleCursorAdapter(this, R.layout.playlist_item, tunes, playlist_fields, views, 0 );
        lv.setAdapter(adapter);
    }

    public String durationFormat(Integer duration) {
        String str = "";
        int min = duration / 60;
        int sec = duration - min * 60;

        str = "" + min + ":" + sec;

        return str;
    }

    public void onClick(View v) {
        ContentValues values = new ContentValues();
        values.put(titleStr, title.getText().toString());
        values.put(authorStr, author.getText().toString());
        values.put(yearStr, year.getText().toString());
        values.put(durationStr, duration.getText().toString());
        musicDB.insert(table, null, values);

        tunes = musicDB.rawQuery("SELECT * FROM playlist", null);

        count.setText(String.valueOf(tunes.getCount()));

        Cursor cursor = musicDB.rawQuery("SELECT SUM(duration) as Total FROM playlist", null);
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        wholeDuration.setText(durationFormat(total));

        adapter.swapCursor(tunes);
        adapter.notifyDataSetChanged();
    }

    public void showAdd(View view) {

        if (title.getVisibility() == View.GONE) {
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }

        if (author.getVisibility() == View.GONE) {
            author.setVisibility(View.VISIBLE);
        } else {
            author.setVisibility(View.GONE);
        }

        if (year.getVisibility() == View.GONE) {
            year.setVisibility(View.VISIBLE);
        } else {
            year.setVisibility(View.GONE);
        }

        if (duration.getVisibility() == View.GONE) {
            duration.setVisibility(View.VISIBLE);
        } else {
            duration.setVisibility(View.GONE);
        }

        if (add.getVisibility() == View.GONE) {
            add.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.GONE);
        }
    }

    public void showSort(View view) {

        if (sortYear.getVisibility() == View.GONE) {
            sortYear.setVisibility(View.VISIBLE);
        } else {
            sortYear.setVisibility(View.GONE);
        }

        if (sortTitle.getVisibility() == View.GONE) {
            sortTitle.setVisibility(View.VISIBLE);
        } else {
            sortTitle.setVisibility(View.GONE);
        }

        if (sortDuration.getVisibility() == View.GONE) {
            sortDuration.setVisibility(View.VISIBLE);
        } else {
            sortDuration.setVisibility(View.GONE);
        }

        if (sortAuthor.getVisibility() == View.GONE) {
            sortAuthor.setVisibility(View.VISIBLE);
        } else {
            sortAuthor.setVisibility(View.GONE);
        }
    }

    public void sortByAuthor(View view) {

        if (checkerAuthor) {
            tunes = musicDB.rawQuery("SELECT * FROM playlist ORDER BY artist DESC", null);
        } else {
            tunes = musicDB.rawQuery("SELECT * FROM playlist ORDER BY artist ASC", null);
        }
        adapter.swapCursor(tunes);
        checkerAuthor = !checkerAuthor;
    }

    public void sortByTitle(View view) {

        if (checkerTitle) {
            tunes = musicDB.rawQuery("SELECT * FROM playlist ORDER BY title DESC", null);
        } else {
            tunes = musicDB.rawQuery("SELECT * FROM playlist ORDER BY title ASC", null);
        }
        adapter.swapCursor(tunes);
        checkerTitle = !checkerTitle;
    }

    public void sortByYear(View view) {

        if (checkerYear) {
            tunes = musicDB.rawQuery("SELECT * FROM playlist ORDER BY year DESC", null);
        } else {
            tunes = musicDB.rawQuery("SELECT * FROM playlist ORDER BY year ASC", null);
        }
        adapter.swapCursor(tunes);
        checkerYear = !checkerYear;
    }

    public void sortByDuration(View view) {

        if (checkerDuration) {
            tunes = musicDB.rawQuery("SELECT * FROM playlist ORDER BY duration DESC", null);
        } else {
            tunes = musicDB.rawQuery("SELECT * FROM playlist ORDER BY duration ASC", null);
        }
        adapter.swapCursor(tunes);
        checkerDuration = !checkerDuration;
    }

    public void init() {
        lv = findViewById(R.id.mainPlayList);

        title = findViewById(R.id.mainTitle);
        author = findViewById(R.id.mainAuthor);
        year = findViewById(R.id.mainYear);
        duration = findViewById(R.id.mainDuration);

        add = findViewById(R.id.mainAdd);

        sortAuthor = findViewById(R.id.sortAuthopr);
        sortDuration = findViewById(R.id.sortDuration);
        sortTitle = findViewById(R.id.sortTitle);
        sortYear = findViewById(R.id.sortYear);

        checkerAuthor = true;
        checkerTitle = true;
        checkerYear = true;
        checkerDuration = true;

        count = findViewById(R.id.mainCount);
        wholeDuration = findViewById(R.id.mainWholeDuration);
    }
}