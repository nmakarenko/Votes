package org.bissoft.yean.votes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.bissoft.yean.votes.util.API;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An activity that represents logging in.
 *
 * @author Nataliia Makarenko
 */
public class StartActivity extends AppCompatActivity {
    /**
     * Button to submit logging in.
     */
    Button btEnter;

    /**
     * EditText to enter login.
     */
    EditText etLogin;
    /**
     * EditText to enter password.
     */
    EditText etPass;

    ProgressBar pbLogin;

    TextView tvTitle;
    ImageView ivEmblem;

    String title = "";
    String emblemUrl = "";

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized
     *                           after previously being shut down then this
     *                           Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        btEnter = (Button) findViewById(R.id.btEnter);

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPass = (EditText) findViewById(R.id.etPass);

        pbLogin = (ProgressBar) findViewById(R.id.pbLogin);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivEmblem = (ImageView) findViewById(R.id.ivEmblem);

        if (isConnected()) {
            GetAsyncTask task = new GetAsyncTask();
            String[] params = new String[]{
                    API.info()
            };
            task.execute(params);
        }

        btEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Context context = v.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                if (etLogin.getText().toString().equals("q")
                        && etPass.getText().toString().equals("q")) {
                    intent.putExtra("is_head", true);
                } else {
                    intent.putExtra("is_head", false);
                }
                context.startActivity(intent);*/

                if (isConnected()) {
              /*      if (mTimer != null) {
                        mTimer.cancel();
                    }
                    mTimer = new Timer();
                    mTimerTask = new MyTimerTask();
                    mTimer.schedule(mTimerTask, 1000);*/

                    btEnter.setClickable(false);
                    pbLogin.setVisibility(View.VISIBLE);
                    btEnter.setText("Входимо");
                    String login = etLogin.getText().toString();
                    String password = etPass.getText().toString();
                    String udid = Settings.Secure.getString(
                            getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                    if (login.equals("member1") && password.equals("qwerty")) udid = "1234";
                    if (login.equals("chief") && password.equals("qwerty")) udid = "qwerty1234";
                    ConnectionTask task = new ConnectionTask();
                    String[] params = new String[]{
                            API.login(),
                            login,
                            password,
                            udid
                    };
                    //    params[0] = API.login();
                    task.execute(params);
                } else {
                    Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private class GetAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
            //    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            //    Log.d("waiting for...", result);
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(result);
                    if (jsonResponse.has("name")) {
                        tvTitle.setText(jsonResponse.optString("name"));
                        title = jsonResponse.optString("name");
                    }
                    if (jsonResponse.has("emblem_url")) {
                        emblemUrl = jsonResponse.optString("emblem_url");
                        new DownloadImageTask(ivEmblem).execute(emblemUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {


            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            //    urlConnection.setRequestProperty("Accept-Charset", charset);
            inputStream = urlConnection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, "windows-1251"));
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = inputStream.read()) != -1) {
                sb.append((char) ch);
            }
            rd.close();
            return sb.toString();

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private class ConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String[] params = new String[]{urls[1], urls[2], urls[3]};
            return POST(urls[0], params);
        }

        @Override
        protected void onPostExecute(String result) {
            // result is what you got from your connection
            //      Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            if (result != null) {
                try {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name", title);
                    intent.putExtra("emblem_url", emblemUrl);
                    JSONObject jsonResponse = new JSONObject(result);
                    if (jsonResponse.has("total")) {
                        intent.putExtra("total", jsonResponse.optString("total"));
                    }
                    if (jsonResponse.has("plenary_session")) {
                        JSONObject jObjInner = jsonResponse.optJSONObject("plenary_session");
                        intent.putExtra("date", jObjInner.optString("date"));
                    }
                    if (jsonResponse.has("data")) {
                        JSONArray jsonData = jsonResponse.optJSONArray("data");
                        JSONObject jObjInner = jsonData.optJSONObject(0);
                        if (jObjInner != null && jObjInner.has("role")) {
                            String role = jObjInner.optString("role");

                            intent.putExtra("last_name", jObjInner.optString("lastName"));
                            intent.putExtra("first_name", jObjInner.optString("firstName"));
                            intent.putExtra("second_name", jObjInner.optString("secondName"));

                            if (role.equals("chief")) {
                                intent.putExtra("is_head", true);
                                getApplicationContext().startActivity(intent);
                            } else if (role.equals("member")) {
                                intent.putExtra("is_head", false);
                                getApplicationContext().startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect login / pass.", Toast.LENGTH_LONG).show();
                                btEnter.setText("Вхід");
                                pbLogin.setVisibility(View.GONE);
                                btEnter.setClickable(true);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Incorrect login / pass.", Toast.LENGTH_LONG).show();
                            btEnter.setText("Вхід");
                            pbLogin.setVisibility(View.GONE);
                            btEnter.setClickable(true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Incorrect login / pass.", Toast.LENGTH_LONG).show();
                btEnter.setText("Вхід");
                pbLogin.setVisibility(View.GONE);
                btEnter.setClickable(true);
            }

        }

    }

    /**
     * Sends POST request.
     *
     * @param url URL to send request
     * @param params Parameters of request.
     * @return Response of request.
     */
    public static String POST(String url, String[] params) {
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("login", params[0])
                    .appendQueryParameter("password", params[1])
                    .appendQueryParameter("udid", params[2]);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();


            //Get Response
            is = conn.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }
            rd.close();
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.i("ERROR", "Error closing InputStream");
                }
            }
        }
        //  return returnVal;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) bmImage.setImageBitmap(result);
        }
    }
}


