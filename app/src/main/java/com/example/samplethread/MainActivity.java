package com.example.samplethread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button button;
    ValueHandler handler = new ValueHandler();
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                BackgroundThread thread = new BackgroundThread();
//                thread.start();

                new Thread(new Runnable() {
                    boolean running = false;
                    int value = 0;

                    @Override
                    public void run() {
                        running = true;
                        while (running) {
                            value += 1;

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("현재 값 : " + String.valueOf(value));
                                }
                            });

                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {}
                        }
                    }
                }).start();
            }
        });

    }

    class BackgroundThread extends Thread {
        boolean running = false;
        int value = 0;

        @Override
        public void run() {
            super.run();
            running = true;
            while (running) {
                value += 1;
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("value", value);
                msg.setData(bundle);
                handler.sendMessage(msg);

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int value = bundle.getInt("value");
            textView.setText("현재 값 : " + String.valueOf(value));
        }
    }
}
