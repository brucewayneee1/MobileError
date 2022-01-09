package com.example.mobiledictionary;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;

import com.example.mobiledictionary.English.EngViet;
import com.example.mobiledictionary.English.EnglishWord;
import com.example.mobiledictionary.EnglishController.WordController;
import com.example.mobiledictionary.Highlight.MyWords;
import com.example.mobiledictionary.Notification.Notification;
import com.example.mobiledictionary.Notification.Receiver;
import com.example.mobiledictionary.Vietnamese.VietEng;
import com.example.mobiledictionary.WordHelper.WordHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    protected int id = 1;
    protected EditText search;
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    public WordController wordController = new WordController();
    public Calendar rightNow = Calendar.getInstance();
    private WordHelper highlightWordHelper = new WordHelper(this,
            "TuDienSqlite", null, 1);
//    public List<EnglishWord> highLightWord = highlightWordHelper.getHighlightList(
//            "NoiDung", "VietEngDemo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        View.OnClickListener handler = new View.OnClickListener(){
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.bSearch:
                        openEngViet();
                        break;

                    case R.id.bTuCuaBan:
                        openHighlight();
                        break;

                    case R.id.bVietAnh:
                        openVietAnh();
                        break;
                }
            }
        };

        findViewById(R.id.bSearch).setOnClickListener(handler);
        findViewById(R.id.bTuCuaBan).setOnClickListener(handler);
        findViewById(R.id.bVietAnh).setOnClickListener(handler);
        //noi dung va tieu de notification
        List<EnglishWord> mListHighlight =
                highlightWordHelper.getHighlightList("NoiDung","VietEngDemo");
        EnglishWord randomHighlightWord = wordController.getRandomWord(mListHighlight);

        //notification
        createNotificationChannel();
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        10 * 1000, pendingIntent);


    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    //chuyển sang cửa sổ tra từ Anh-Việt
    public void openEngViet() {
        search = (EditText) findViewById(R.id.edittext_main_search);
        String text = search.getText().toString();

        Intent intent = new Intent(this, EngViet.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }
    //chuyển sang cửa sổ highlight
    public void openHighlight() {
        Intent intent = new Intent(this, MyWords.class);
        startActivity(intent);
    }

    //chuyển sang cửa sổ tra việt anh
    public void openVietAnh() {
        Intent intent = new Intent(this, VietEng.class);
        startActivity(intent);
    }

    //tạo channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "studentChannel";
            String description = "Channel for student notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("lemubitA", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}