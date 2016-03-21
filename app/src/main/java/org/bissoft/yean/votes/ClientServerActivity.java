package org.bissoft.yean.votes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientServerActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private TextView text;
    private EditText input;
    private Button send;
    private Socket socket;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;
    private String DeviceName = "Device";

    private boolean isServer;

    private float x;
    private float y;
    private ImageView image;

    Bitmap infBitmap;
    Canvas canvas4;
    Paint paint4;

    boolean meLine = false;
    boolean youLine = false;

    private float xMePrev;
    private float yMePrev;

    private float xYouPrev;
    private float yYouPrev;

    private boolean searchNetwork() {
        log("Connecting");
        //   String range = "10.10.0.";
        String ip = "10.10.0.209";
        //       for (int i = 1; i <= 255; i++) {
        //   int i = 54;
        //      String ip = range + "54";//i;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, 9000), 1000);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            DeviceName += "1";
            Log.i("Server", DeviceName);
            log("Connected");
            isServer = false;
            return true;
        } catch (Exception e) {
        }
        //    }
        return false;

    }

    private void runNewChatServer() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9000);
            isServer = true;
            log("Waiting for client...");
            socket = serverSocket.accept();
            DeviceName += "2";
            log("a new client Connected");
        } catch (IOException e) {
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_server);
        text = (TextView) findViewById(R.id.text);
        //  input = (EditText) findViewById(R.id.input);
        send = (Button) findViewById(R.id.send);
        image = (ImageView) findViewById(R.id.image);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (!searchNetwork()) {
                        runNewChatServer();

                    }

                    outputStream = new DataOutputStream(
                            socket.getOutputStream());
                    inputStream = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                    while (true) {

                        String Message = inputStream.readLine();
                        if (Message != null) {
                            log(Message);
                            //        if (DeviceName.equals("Device2")) {
                            final String msg = Message;
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        JSONObject jObj = new JSONObject(msg);
                                        if (jObj.has("coords")) {
                                            String[] coords = jObj.optString("coords").split(",");
                                            painting(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]), false, 1);

                                        } else if (jObj.has("actionUp")) {
                                            youLine = false;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            //  }
                        }
                    }
                } catch (IOException e) {
                    log("Error: IO Exception");
                    e.printStackTrace();
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meLine = false;
                youLine = false;
                if (infBitmap != null) {
                    infBitmap = Bitmap.createBitmap(
                            image.getWidth(),
                            image.getHeight(), Bitmap.Config.ARGB_8888);


                    canvas4 = new Canvas(infBitmap);
                    paint4 = new Paint();
                    paint4.setAntiAlias(true);
                    paint4.setColor(Color.BLUE);


                    image.setImageBitmap(infBitmap);
                }
            }
        });

        thread.start();
        //     if (isServer) thread2.start();

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getX() / image.getWidth() * 100;
                y = event.getY() / image.getHeight() * 100;


                painting(x, y, true, 0);
                JSONObject jObj = new JSONObject();
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    try {
                        jObj.put("coords", "" + x + "," + y);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    meLine = false;
                    try {
                        jObj.put("actionUp", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //      input.setText(jObj.toString());
                if (outputStream == null) {
                    return true;
                }
                try {
                    String Message = jObj.toString() + "\n";//input.getText().toString() + "\n";
                    outputStream.write(Message.getBytes());
                    //    log2(input.getText().toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //      input.setText("");


                return true;
            }
        });

    }

    private void log(final String message) {
        handler.post(new Runnable() {
            String DeviceName2 = "";

            @Override
            public void run() {
                if (DeviceName.equals("Device1")) {
                    DeviceName2 = "Device2";
                } else if (DeviceName.equals("Device2")) {
                    DeviceName2 = "Device1";
                } else {
                    DeviceName2 = "UnknowDevice";
                }

                text.setText(text.getText() + "\n" + DeviceName2 + " :"
                        + message);

            }
        });
    }
    private void log2(final String message) {
        handler.post(new Runnable() {

            @Override
            public void run() {


                text.setText(text.getText() + "\n" + "you" + " :"
                        + message);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private void painting(float xPercent, float yPercent, boolean me, int device) {
        int x = (int)((float) image.getWidth() / 100 * xPercent);
        int y = (int)((float) image.getHeight() / 100 * yPercent);


        if (infBitmap == null ) {
            infBitmap = Bitmap.createBitmap(
                    image.getWidth(),
                    image.getHeight(), Bitmap.Config.ARGB_8888);


            canvas4 = new Canvas(infBitmap);
            paint4 = new Paint();
            paint4.setAntiAlias(true);
            if (me) {
                paint4.setColor(Color.BLUE);
            } else {
                if (device == 1) paint4.setColor(Color.RED);
                else paint4.setColor(Color.GREEN);
            }

            canvas4.drawPoint(x, y, paint4);

            image.setImageBitmap(infBitmap);

        } else {
            if (me) {
                paint4.setColor(Color.BLUE);
                if(meLine) {
                    canvas4.drawLine(xMePrev, yMePrev, x, y, paint4);
                } else {
                    canvas4.drawPoint(x, y, paint4);
                }
            } else if (device == 1){
                paint4.setColor(Color.RED);
                if(youLine) {
                    canvas4.drawLine(xYouPrev, yYouPrev, x, y, paint4);
                } else {
                    canvas4.drawPoint(x, y, paint4);
                }
            } else {
                paint4.setColor(Color.GREEN);
                canvas4.drawPoint(x, y, paint4);
            }
            if (me) {
                xMePrev = x;
                yMePrev = y;
            } else if (device == 1){
                xYouPrev = x;
                yYouPrev = y;
            }
            if (!meLine && me) {
                meLine = true;
            }
            if (!youLine && !me && device == 1) {
                youLine = true;
            }
            image.invalidate();
        }
    }
}

