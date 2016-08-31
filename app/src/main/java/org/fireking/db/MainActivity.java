package org.fireking.db;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.fireking.db.bean.Message;
import org.fireking.db.db.AppOrmHelper;
import org.fireking.db.db.AppSqLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kima.wang on 2016/8/30.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.create_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("info", "->" + AppSqLiteOpenHelper.getInstance());
                AppSqLiteOpenHelper.getInstance().createTable("tb_message", Message.class);
            }
        });

        findViewById(R.id.insertData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.setTitle("上海");
                        message.setMessage("我是上海");
                        long count = AppOrmHelper.getInstance().insertByOrm("tb_message", message);
                        Log.e("info", "插入:" + count);
                    }
                }).start();
            }
        });

        findViewById(R.id.insertDataByBitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Message> messageList = new ArrayList();
                        for (int i = 0; i < 1000; i++) {
                            Message message = new Message();
                            message.setTitle("上海" + i);
                            message.setMessage("我是上海" + i);
                            messageList.add(message);
                        }
                        long startTract = System.currentTimeMillis();
                        AppOrmHelper.getInstance().insertBitchByOrm("tb_message", messageList);
                        Log.e("info", "操作耗时:" + (System.currentTimeMillis() - startTract));
                    }
                }).start();
            }
        });

        findViewById(R.id.sql_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String sql = "select * from tb_message";
                        Cursor cursor = AppOrmHelper.getInstance().getDataInfosByOrm(sql, new String[]{});
                        if (cursor != null) {
                            List<Message> messagesList = new ArrayList<Message>();
                            while (cursor.moveToNext()) {
                                String title = cursor.getString(cursor.getColumnIndex("title"));
                                String message = cursor.getString(cursor.getColumnIndex("message"));
                                Message msg = new Message();
                                msg.setTitle(title);
                                msg.setMessage(message);
                                messagesList.add(msg);
                            }
                            cursor.close();
                            Log.e("info", "总共:" + messagesList.size() + ", " + messagesList.toString());
                        }
                    }
                }).start();
            }
        });

        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "就是常规的sqlite query操作，这里不做说明!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.drop_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
