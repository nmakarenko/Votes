package org.bissoft.yean.votes;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

/**
 * An activity that represents all content after logging in.
 * @author      Nataliia Makarenko
 */
public class MainActivity extends AppCompatActivity {
    /** Time to register. */
    public final static int REGISTR_TIME = 5;
    /** Time to poll. */
    public final static int POLL_TIME = 5;

    /** Button to test Api */
    Button btTestApi;

    /** Whether or not we can not go on previous page. */
    boolean onMainFragm = true;
    /** Whether or not quorum is reached.*/
    boolean quorum = false;

    /** Tag of the previous fragment. */
    String fragmPrev;
    /** Arguments or previous fragment. */
    Bundle argsPrev;

    /** Height of screen. */
    public static int height;
    /** Width of screen. */
    public static int width;
    /** Density of screen. */
    public static float density;

    /** TextView that represents name of department. */
    TextView tvDeptsTitle;
    /** TextView that represents whether or not the quorum is reached. */
    TextView tvQuorumTitle;
    /** TextView that represents tha date. */
    TextView tvMenuDate;

    TextView tvTitle;

    private Handler handler = new Handler();
    private Socket socket;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;

    /** Whether or not the device is server. */
    boolean isServer;

    /**
     * Searches a ServerSocket in local network and connects to it.
     * @return Whether the ServerSocket was found.
     */
    private boolean searchNetwork() {
        //   String range = "10.10.0.";
        String ip = "10.10.0.123";
        //       for (int i = 1; i <= 255; i++) {
        //   int i = 54;
        //      String ip = range + "54";//i;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, 9000), 1000);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            isServer = false;
            Log.d("votes33", "Connected");
            //      Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
        }
        //    }
        return false;

    }

    /**
     * Runs a ServerSocket.
     */
    private void runNewChatServer() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9000);
            Log.d("votes33", "Waiting for client...");
            socket = serverSocket.accept();
            isServer = true;
//            Toast.makeText(this, "Client Connected", Toast.LENGTH_SHORT).show();
            Log.d("votes33", "Client connected");
        } catch (IOException e) {
        }

    }

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized
     *                           after previously being shut down then this
     *                           Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels - (int) (dm.densityDpi * 0.14);
        density = dm.density;

        /*    ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        View mContentView = findViewById(R.id.fragment);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/

        //  changeFragment(new BillListFragment(), new Bundle(), "bill_list_fragm");

        tvQuorumTitle = (TextView) findViewById(R.id.tvQuorumTitle);
        tvDeptsTitle = (TextView) findViewById(R.id.tvDeptsTitle);
        tvMenuDate = (TextView) findViewById(R.id.tvMenuDate);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        tvDeptsTitle.setVisibility(View.GONE);
        tvQuorumTitle.setVisibility(View.GONE);

        String[] months = {"січня", "лютого", "березня", "квітня",
                "травня", "червня", "липня", "серпня",
                "вересня", "жовтня", "листопада", "грудня"};
        Calendar calendar = Calendar.getInstance();
     /*   tvMenuDate.setText("" + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR) + " року");
        */
        String[] sdate = new String[3];
        int[] date = new int[3];
        if (getIntent().getStringExtra("date") != null
                && !getIntent().getStringExtra("date").equals("")) {
            try{
                sdate = getIntent().getStringExtra("date").split("-");
                date[2] = Integer.parseInt(sdate[0]);
                date[1] = Integer.parseInt(sdate[1]);
                date[0] = Integer.parseInt(sdate[2]);
            } catch (Exception e) {
                date[0] = calendar.get(Calendar.DAY_OF_MONTH);
                date[1] = calendar.get(Calendar.MONTH);
                date[2] = calendar.get(Calendar.YEAR);
            }
        } else {
            date[0] = calendar.get(Calendar.DAY_OF_MONTH);
            date[1] = calendar.get(Calendar.MONTH);
            date[2] = calendar.get(Calendar.YEAR);
        }

        tvMenuDate.setText(String.format("%d %s %d року", date[0], months[date[1] - 1], date[2]));

        if (getIntent().getStringExtra("name") != null) {
            tvTitle.setText(getIntent().getStringExtra("name"));
        }

        if (getIntent().getBooleanExtra("from_dialog", false)) {
            Bundle args = new Bundle();
            args.putBoolean("is_head", getIntent().getBooleanExtra("is_head", false));
            changeFragment(new BillListFragment(), args, "bill_list_fragm", "");
        } else if (getIntent().getBooleanExtra("from_dialog_to_results", false)) {
            Bundle args = new Bundle();
            args.putBoolean("is_head", getIntent().getBooleanExtra("is_head", false));
            args.putInt("bill_num", getIntent().getIntExtra("bill_num", 0));
            changeFragment(new PollResultsFragment(), args, "poll_results_fragm", "bill_list_fragm");
        } else {
            Bundle args = new Bundle();
            args.putBoolean("is_head", getIntent().getBooleanExtra("is_head", false));
            args.putString("total", getIntent().getStringExtra("total"));
            args.putString("last_name", getIntent().getStringExtra("last_name"));
            args.putString("first_name", getIntent().getStringExtra("first_name"));
            args.putString("second_name", getIntent().getStringExtra("second_name"));
            args.putString("emblem_url", getIntent().getStringExtra("emblem_url"));
            changeFragment(new StartSessionFragment(), args, "start_session_fragm", "");
        }

        btTestApi = (Button) findViewById(R.id.btTestApi);

        btTestApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

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
                        //Log.d("in threading", "cont");
                        String Message = inputStream.readLine();
                        if (Message != null) {
                            //        if (DeviceName.equals("Device2")) {
                            final String msg = Message;
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    if (!isServer) {
                                        if (msg.equals("start_session")) {
                                            Bundle args = new Bundle();
                                            args.putString("last_name", getIntent().getStringExtra("last_name"));
                                            args.putString("first_name", getIntent().getStringExtra("first_name"));
                                            args.putString("second_name", getIntent().getStringExtra("second_name"));
                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                            DialogTimeRegist editNameDialog = DialogTimeRegist.newInstance();
                                            editNameDialog.setArguments(args);
                                            editNameDialog.setCancelable(false);
                                            editNameDialog.show(fragmentManager, "dialog_time_registr");
                                        } else if (msg.equals("start_poll")) {
                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                            DialogStartPoll editNameDialog = DialogStartPoll.newInstance();
                                            editNameDialog.setCancelable(false);
                                            editNameDialog.show(fragmentManager, "dialog_start_poll");
                                        }
                                    }
                                }
                            });
                            //  }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * Sets chosen fragment in FrameLayout of MainActivity.
     *
     * @param fragment a chosen fragment to be set in frame of activity.
     * @param arguments arguments to be set in chosen fragment.
     * @param tag a tag to be set to this fragment.
     * @param tagFragmPrev a tag of previous fragment.
     */
    public void changeFragment(Fragment fragment, Bundle arguments, String tag, String tagFragmPrev) {

        onMainFragm = !tag.equals("bill_detail_fragm");

        fragmPrev = tagFragmPrev;

        if (tag.equals("bill_list_fragm") && !quorum) {
            quorum = true;
            tvDeptsTitle.setVisibility(View.VISIBLE);
        //    tvQuorumTitle.setVisibility(View.VISIBLE);
        }

        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, fragment, tag)
                .commit();
    }

    /**
     * Sets chosen fragment in FrameLayout of MainActivity.
     *
     * @param fragment a chosen fragment to be set in frame of activity.
     * @param arguments arguments to be set in chosen fragment.
     * @param tag a tag to be set to this fragment.
     * @param tagFragmPrev a tag of previous fragment.
     * @param argsPrev arguments of previous fragment.
     */
    public void changeFragment(Fragment fragment, Bundle arguments, String tag, String tagFragmPrev, Bundle argsPrev) {

        onMainFragm = !tag.equals("bill_detail_fragm");

        fragmPrev = tagFragmPrev;
        this.argsPrev = argsPrev;

        if (tag.equals("bill_list_fragm") && !quorum) {
            quorum = true;
            tvDeptsTitle.setVisibility(View.VISIBLE);
       //     tvQuorumTitle.setVisibility(View.VISIBLE);
        }

        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerMain, fragment, tag)
                .commit();
    }

    /**
     * Opens dialog with testing Api.
     */
    public void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_test_api);

        LinearLayout llDial = (LinearLayout) dialog.findViewById(R.id.llApiDialog);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int)(width * 0.8), (int)(height * 0.8));
        llDial.setLayoutParams(layoutParams);

        Button btClear = (Button) dialog.findViewById(R.id.btClear);

        Button btApiLogin = (Button) dialog.findViewById(R.id.btApiLogin);
        final TextView tvApiLogin = (TextView) dialog.findViewById(R.id.tvApiLogin);

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                tvApiLogin.setLayoutParams(layoutParams);
                tvApiLogin.setText("");
            }
        });

        btApiLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvApiLogin.setText("One of the highlights of the Android 2.0 SDK is that you can write custom sync providers to integrate with the system contacts, calendars, etc. The only problem is that there’s very little documentation on how it all fits together. And worse, if you mess up in certain places, the Android system will crash and reboot! Always up for a challenge, I’ve navigated through the sparse documentation, vague mailing list posts, and the Android source code itself to build a sync provider for our Last.fm app. Want to know how to build your own? Read on!\n" +
                        "\n" +
                        "Account Authenticators\n" +
                        "The first piece of the puzzle is called an Account Authenticator, which defines how the user’s account will appear in the “Accounts & Sync” settings. Implementing an Account Authenticator requires 3 pieces: a service that returns a subclass of AbstractAccountAuthenticator from the onBind method, an activity to prompt the user to enter their credentials, and an xml file describing how your account should look when displayed to the user. You’ll also need to add the android.permission.AUTHENTICATE_ACCOUNTS permission to your AndroidManifest.xml.\n" +
                        "\n" +
                        "The Service\n" +
                        "The authenticator service is expected to return a subclass of AbstractAccountAuthenticator from the onBind method — if you don’t, Android will crash and reboot when you try to add a new account to the system. The only method in AbstractAccountAuthenticator we really need to implement is addAccount, which returns an Intent that the system will use to display the login dialog to the user. The implementation below will launch our app’s main launcher activity with an action of “fm.last.android.sync.LOGIN” and an extra containing the AccountAuthenticatorResponse object we use to pass data back to the system after the user has logged in.");

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tvApiLogin.setLayoutParams(layoutParams);
            }
        });

        dialog.show();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        if (!HeadPollFragment.isVoting
                && !BillInfoDiscussFragment.isSpeaking) {
            if (!fragmPrev.equals("") && !fragmPrev.equals("bill_info_fragm")) {
                Bundle args = new Bundle();
                args.putBoolean("is_head", getIntent().getBooleanExtra("is_head", false));
                changeFragment(new BillListFragment(), args, "bill_list_fragm", "");
            } else if (!fragmPrev.equals("")) {
                changeFragment(new BillDetailFragment(), argsPrev, "bill_detail_fragm", "bill_list_fragm");
            } else {
                //    moveTaskToBack(true);
            }
        }
    }

    /**
     * Opens dialog of registration on deputies' devises.
     */
    public void startSession() {
        if (outputStream != null && isServer) {
            try {
                String Message = "start_session" + "\n";
                outputStream.write(Message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens dialog of voting on deputies' devises.
     */
    public void startPoll() {
        if (outputStream != null && isServer) {
            try {
                String Message = "start_poll" + "\n";
                outputStream.write(Message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks whether the device is connected to network.
     * @return Whether the device is connected.
     */
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}

