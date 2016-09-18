package org.fireking.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eku.library.AppOrmHelper;
import com.eku.library.AppSqLiteOpenHelper;

import org.fireking.db.bean.Message;

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

        //增删改查
        crud();

        //数据库操作
        database();

    }

    private void database() {
        findViewById(R.id.create_table).setOnClickListener(view -> {
            Log.e("info", "->" + AppSqLiteOpenHelper.getInstance());
            AppSqLiteOpenHelper.getInstance().createTable("tb_message", Message.class);
        });

        findViewById(R.id.open_database).setOnClickListener(view -> {

        });

        findViewById(R.id.drop_table).setOnClickListener(view -> {
            AppSqLiteOpenHelper.getInstance().dropTable("tb_message");
        });
    }

    private void crud() {
        findViewById(R.id.insertData).setOnClickListener(view -> {
            new Thread(() -> {
                Message message = new Message();
                message.setTitle("上海");
                message.setMessage("我是上海");
                long count = AppOrmHelper.getInstance().insertByOrm("tb_message", message);
                Log.e("info", "插入:" + count);
            }).start();
        });

        findViewById(R.id.insertDataByBitch).setOnClickListener(view -> {
            new Thread(() -> {
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
            }).start();
        });

        findViewById(R.id.sql_search).setOnClickListener(view -> {
            new Thread(() -> {
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
            }).start();
        });

        findViewById(R.id.query).setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "就是常规的sqlite query操作，这里不做说明!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.updateData).setOnClickListener(view -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Message.COL_TITLE, "SHANGHAI");
            AppOrmHelper.getInstance().updateByOrm("tb_message", contentValues, "_id=?", new String[]{"1"});
        });

        findViewById(R.id.updateDataBitch).setOnClickListener(view -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Message.COL_TITLE, "I SHANGHAI");
            AppOrmHelper.getInstance().updateByOrm("tb_message", contentValues, "_id > ? and _id < ?", new String[]{"1", "8"});
        });

        findViewById(R.id.deleteData).setOnClickListener(view -> {
            AppOrmHelper.getInstance().deleteByOrm("tb_message", "title = ?", new String[]{"SHANGHAI"});
        });

        findViewById(R.id.deleteDataBitch).setOnClickListener(view -> {
            AppOrmHelper.getInstance().deleteByOrm("tb_message", "title = ?", new String[]{"I SHANGHAI"});
        });

        findViewById(R.id.insertDataByMultiThread).setOnClickListener(view -> {
            new Thread(() -> {
                List<Message> messages = new ArrayList<Message>();
                for (int i = 0; i < 1000; i++) {
                    messages.add(new Message("test0" + i % 4, "message" + i));
                }
                while (true) {
                    AppOrmHelper.getInstance().insertBitchByOrm("tb_message", messages);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(() -> {
                List<Message> messages = new ArrayList<Message>();
                for (int i = 0; i < 1000; i++) {
                    messages.add(new Message("test0" + i % 4, "message" + i));
                }
                while (true) {
                    AppOrmHelper.getInstance().insertBitchByOrm("tb_message", messages);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(() -> {
                List<Message> messages = new ArrayList<Message>();
                for (int i = 0; i < 1000; i++) {
                    messages.add(new Message("test0" + i % 4, "message" + i));
                }
                while (true) {
                    AppOrmHelper.getInstance().insertBitchByOrm("tb_message", messages);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(() -> {
                List<Message> messages = new ArrayList<Message>();
                for (int i = 0; i < 1000; i++) {
                    messages.add(new Message("test0" + i % 4, "message" + i));
                }
                while (true) {
                    AppOrmHelper.getInstance().insertBitchByOrm("tb_message", messages);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(() -> {
                List<Message> messages = new ArrayList<>();
                for (int i = 0; i < 1000; i++) {
                    messages.add(new Message("test0" + i % 4, "message" + i));
                }
                while (true) {
                    AppOrmHelper.getInstance().insertBitchByOrm("tb_message", messages);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });

        findViewById(R.id.deleteDataByMultiThread).setOnClickListener(view -> {
            new Thread(() -> {
                while (true) {
                    for (int i = 0; i < 1000; i++) {
                        AppOrmHelper.getInstance().deleteByOrm("tb_message", "title = ?", new String[]{"test01"});
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }).start();
            new Thread(() -> {
                while (true) {
                    for (int i = 0; i < 1000; i++) {
                        AppOrmHelper.getInstance().deleteByOrm("tb_message", "title = ?", new String[]{"test02"});
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }).start();
            new Thread(() -> {
                while (true) {
                    for (int i = 0; i < 1000; i++) {
                        AppOrmHelper.getInstance().deleteByOrm("tb_message", "title = ?", new String[]{"test03"});
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }).start();
            new Thread(() -> {
                while (true) {
                    for (int i = 0; i < 1000; i++) {
                        AppOrmHelper.getInstance().deleteByOrm("tb_message", "title = ?", new String[]{"test04"});
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }).start();
            new Thread(() -> {
                while (true) {
                    for (int i = 0; i < 1000; i++) {
                        AppOrmHelper.getInstance().deleteByOrm("tb_message", "title = ?", new String[]{"test05"});
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }).start();
        });

        findViewById(R.id.query_all_count).setOnClickListener(view -> {
            new Thread(() -> {
                while (true) {
                    Cursor cursor = AppOrmHelper.getInstance().getDataInfosByOrm("select * from tb_message", new String[]{});
                    Log.e("info", "数据总量: " + cursor.getCount());
                    cursor.close();
                    try {
                        Thread.sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
    }
}
