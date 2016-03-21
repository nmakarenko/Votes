package org.bissoft.yean.votes;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.bissoft.yean.votes.util.SpeakersArrayAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A fragment representing a single bill discussion screen
 * when the head is logged in.
 * @author      Nataliia Makarenko
 */
public class BillInfoDiscussFragment extends Fragment {
    /** List of invited persons. */
    ListView lvInvited;
    /** List of speakers. */
    ListView lvSpeakers;

    /** Time of speech. */
    TextView tvTimeSpeaking;
    /** Progress of time of speech. */
    ProgressBar pbSpeaking;

    /** A layout with speech progress. */
    LinearLayout llPbSpeaking;

    /** An adapter for list of speakers. */
    SpeakersArrayAdapter speakersAdapter;

    /** Height of layout with speech progress. */
    int targetHeight;

    static boolean isSpeaking = false;

    Runnable runnable;
    Thread thread;
    Handler h;
    volatile boolean stopThread = false;

    int seconds;

    /**
     * Constructs a new instance of {@code BillInfoDiscussFragment}.
     */
    public BillInfoDiscussFragment() {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater the LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself,
     *                  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     *
     * @return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bill_info_disc, container, false);

        lvInvited = (ListView) rootView.findViewById(R.id.lvInvited);
        lvSpeakers = (ListView) rootView.findViewById(R.id.lvSpeakers);
        tvTimeSpeaking = (TextView) rootView.findViewById(R.id.tvTimeSpeaking);
        pbSpeaking = (ProgressBar) rootView.findViewById(R.id.pbSpeaking);
        llPbSpeaking = (LinearLayout) rootView.findViewById(R.id.llPbSpeaking);

        targetHeight = 100;
        //   collapse(llPbSpeaking);

        String[] invited = new String[] {
                "1. Петров",
                "2. Сидоров",
                "3. Краснов",
                "4. Романько",
                "5. Терещенко",
                "6. Кузьменко",
                "7. Михайленко"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, invited);
        lvInvited.setAdapter(adapter);

        ArrayList<String> speakers = new ArrayList<>();
        speakers.add("Петренко Петро Петрович");
        speakers.add("Тесторов Тест Тестович");
        speakers.add("Адмін Адмін Адмін");
        speakers.add("Гололобов Сергій Вадимович");
        speakers.add("Микитенко Андрій Юрійович");
        speakers.add("Черненко Петро Владиславович");
        speakers.add("Микитенко Степан Степанович");
        speakers.add("Вернигора Антон Ігорович");
        speakers.add("Сергієнко Максим Васильович");

        speakersAdapter = new SpeakersArrayAdapter(getActivity(), speakers, this);
        lvSpeakers.setAdapter(speakersAdapter);

        h = new ValidHandler(this);
        runnable = new Runnable() {
            @Override
            public void run() {

                if(!Thread.interrupted()) {
                    int progressStatus = 0;
                    int time = seconds;
                    pbSpeaking.setMax(seconds);
                    for (; pbSpeaking.getProgress() <= seconds;) {
           //             Log.d("stopped", "" + Thread.currentThread().interrupted());


                        if (!stopThread) {


                            try {
                                if (pbSpeaking.getProgress() == seconds) {
                                    Message msg2 = new Message();
                                    Bundle data2 = new Bundle();
                                    data2.putBoolean("threadEnd", true);
                                    msg2.setData(data2);
                                    h.sendMessage(msg2);
                                } else {
                                    Message msg = new Message();
                                    Bundle data = new Bundle();
                                    data.putInt("timer", seconds - pbSpeaking.getProgress());
                                    msg.setData(data);
                                    h.sendMessage(msg);
                                    Thread.sleep(1000);
                                    pbSpeaking.setProgress(pbSpeaking.getProgress() + 1);
                                }
                            } catch (InterruptedException e) {

                            }
                        }
                    }
                }
            }

        };

        return rootView;
    }

    static class ValidHandler extends Handler {
        WeakReference<BillInfoDiscussFragment> wrFragment;

        public ValidHandler(BillInfoDiscussFragment fragment) {
            wrFragment = new WeakReference<BillInfoDiscussFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            BillInfoDiscussFragment fragment = wrFragment.get();
            if (fragment != null) {
                Bundle b = msg.getData();
                int timer = b.getInt("timer");
                fragment.tvTimeSpeaking.setText(String.format("%02d:%02d", timer / 60, timer % 60));
                if (b.getBoolean("threadEnd") && !fragment.stopThread) {
                    //    stopThread = true;

                    Log.d("threadEndtrue", "true" + b.getInt("timer"));
                    fragment.stopSpeaking();
                }
            }
        }
    }

    private void startThread(){
        pbSpeaking.setProgress(0);
        if (thread == null) {
            thread = new Thread(runnable);
            thread.start();
        }
    }

    /**
     * Sets the time of speaking.
     * Sets the layout with speaking progress in visible mode.
     *
     * @param minutes number of minutes for the speech.
     */
    public void startSpeaking(int minutes) {
        seconds = minutes * 60;
        pbSpeaking.setMax(seconds);
        isSpeaking = true;
        tvTimeSpeaking.setVisibility(View.VISIBLE);
        pbSpeaking.setVisibility(View.VISIBLE);
        tvTimeSpeaking.setText(String.format("%02d:00", minutes));
        //  expand(llPbSpeaking);
        stopThread = false;
        startThread();
    }

    /**
     * Sets the layout with speaking progress in invisible mode.
     */
    public void stopSpeaking() {
        //   collapse(llPbSpeaking);
        tvTimeSpeaking.setVisibility(View.INVISIBLE);
        pbSpeaking.setVisibility(View.INVISIBLE);
        isSpeaking = false;
        stopThread = true;
      //  thread.interrupt();
        Log.d("stopped interr", "" + Thread.interrupted());
        SpeakersArrayAdapter.speakerChosen = false;
        SpeakersArrayAdapter.reportChosen = false;
        speakersAdapter.data.remove(speakersAdapter.speakerPosition);
        speakersAdapter.notifyDataSetChanged();
    }

    /**
     * Expands the view.
     *
     * @param v View that we want to expand.
     */
    public void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        v.getLayoutParams().height = 1;
        //    v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? targetHeight : (int) (targetHeight * interpolatedTime);//interpolatedTime == 1 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(300);
        v.startAnimation(a);
    }

    /**
     * Collapses the view.
     *
     * @param v View that we want to collapse.
     */
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    //         v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(300);
        v.startAnimation(a);
    }
}

