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

import static android.view.View.*;

public class MainActivity extends AppCompatActivity {

    ListView lv; // не забудьте привязать переменную (findViewById)
    SimpleCursorAdapter adapter; // объявлен в классе, чтобы был доступен вл всех методах
    View addViews[];
    View sortViews[];

    TextView title, author, year, duration, count, wholeDuration;

    Button add;
    Button sortTitle, sortAuthor, sortYear, sortDuration;

    final String table = "playlist";
    final String titleStr = "title";
    final String authorStr = "artist";
    final String yearStr = "year";
    final String durationStr = "duration";

    int total;

    SQLiteDatabase musicDB;
    Cursor tunes;

    Boolean checkerTitle,checkerAuthor,checkerYear,checkerDuration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        musicDB = new DBHelperWithLoader(this).getWritableDatabase();

        tunes = musicDB.rawQuery(
                String.format("SELECT * FROM %s", table),
                null
        );
        Cursor cursor = musicDB.rawQuery(
                String.format("SELECT SUM(%s) as Total FROM %s", durationStr, table),
                null
        );

        String[] playlist_fields = tunes.getColumnNames();

        if (cursor.moveToFirst()) {
            total = cursor.getColumnIndex("Total");
        }

        count.setText(String.valueOf(tunes.getCount()));
        wholeDuration.setText(durationFormat(total));

        int[] views = {R.id.itemId, R.id.itemAuthor, R.id.itemTitle, R.id.itemYear, R.id.itemDuration};

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.playlist_item,
                tunes,
                playlist_fields,
                views,
                0
        );
        lv.setAdapter(adapter);
    }

    public String durationFormat(Integer duration) {
        int min = duration / 60;
        int sec = duration - min * 60;

        return min + ":" + sec;
    }

    public void onClick(View v) {
        ContentValues values = new ContentValues();
        values.put(titleStr, (String) title.getText());
        values.put(authorStr, (String) author.getText());
        values.put(yearStr, (String) year.getText());
        values.put(durationStr, (String) duration.getText());

        musicDB.insert(table, null, values);

        tunes = musicDB.rawQuery(
                String.format(
                        "SELECT * FROM %s",table),
                null
        );

        count.setText(String.valueOf(tunes.getCount()));

        Cursor cursor = musicDB.rawQuery(
                String.format("SELECT SUM(%s) as Total FROM %s", durationStr, title),
                null
        );

        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }
        wholeDuration.setText(durationFormat(total));

        adapter.swapCursor(tunes);
        adapter.notifyDataSetChanged();
    }

    public void showAdd(View view) {
        for (View v: addViews) {
            if (v.getVisibility() == GONE) {
                v.setVisibility(VISIBLE);
            } else {
                v.setVisibility(GONE);
            }
        }
    }

    public void showSort(View view) {
        for (View v: sortViews) {
            if (v.getVisibility() == GONE) {
                v.setVisibility(VISIBLE);
            } else {
                v.setVisibility(GONE);
            }
        }
    }

    public void sortByAuthor(View view) {

        if (checkerAuthor) {
            tunes = musicDB.rawQuery(String.format("SELECT * FROM %s ORDER BY %s DESC",title, authorStr), null);
        } else {
            tunes = musicDB.rawQuery(String.format("SELECT * FROM %s ORDER BY %s ASC",title, authorStr), null);
        }
        adapter.swapCursor(tunes);
        checkerAuthor = !checkerAuthor;
    }

    public void sortByTitle(View view) {

        if (checkerTitle) {
            tunes = musicDB.rawQuery(String.format("SELECT * FROM %s ORDER BY %s DESC",title, titleStr), null);
        } else {
            tunes = musicDB.rawQuery(String.format("SELECT * FROM %s ORDER BY %s ASC",title, titleStr), null);
        }
        adapter.swapCursor(tunes);
        checkerTitle = !checkerTitle;
    }

    public void sortByYear(View view) {

        if (checkerYear) {
            tunes = musicDB.rawQuery(String.format("SELECT * FROM %s ORDER BY %s DESC",title, yearStr), null);
        } else {
            tunes = musicDB.rawQuery(String.format("SELECT * FROM %s ORDER BY %s ASC",title, yearStr), null);
        }
        adapter.swapCursor(tunes);
        checkerYear = !checkerYear;
    }

    public void sortByDuration(View view) {

        if (checkerDuration) {
            tunes = musicDB.rawQuery(String.format("SELECT * FROM %s ORDER BY %s DESC",title, durationStr), null);
        } else {
            tunes = musicDB.rawQuery(String.format("SELECT * FROM %s ORDER BY %s ASC",title, durationStr), null);
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

        sortAuthor = findViewById(R.id.sortAuthor);
        sortDuration = findViewById(R.id.sortDuration);
        sortTitle = findViewById(R.id.sortTitle);
        sortYear = findViewById(R.id.sortYear);

        checkerAuthor = true;
        checkerTitle = true;
        checkerYear = true;
        checkerDuration = true;

        count = findViewById(R.id.mainCount);
        wholeDuration = findViewById(R.id.mainWholeDuration);

        addViews = new View[]{title, author, year, duration, add};
        sortViews = new View[]{sortTitle, sortAuthor, sortYear, sortDuration};
    }
}