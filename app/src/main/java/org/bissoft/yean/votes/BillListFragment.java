package org.bissoft.yean.votes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import org.bissoft.yean.votes.util.Bill;
import org.bissoft.yean.votes.util.BillsArrayAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


/**
 * A fragment representing a list of bills.
 * @author      Nataliia Makarenko
 */
public class BillListFragment extends Fragment {
    /** Name of SharedPreferences. */
    public static final String SP_NAME = "VOTES_SP";
    /** Name of data in SharedPreferences. */
    public static final String USER_DATA = "USER_DATA";
    //  public static final String ACTION_STARTED = "com.gnsit.votes.SERVICE_STARTED";
    //   public static final String ACTION_STOPPED = "com.gnsit.votes.SERVICE_STOPPED";

    /** Instance of SharedPreferences. */
    SharedPreferences sp;
    /** Current activity. */
    Activity activity;

    /** ListView of bills. */
    ListView lvBills;
    /** An adapter for list of bills. */
    BillsArrayAdapter billsAdapter;
    /** List of bills. */
    ArrayList<Bill> bills;

    /** Whether the deputy is in reporters list. */
    boolean meReport;

    /** Button to start service. */
    Button btStartService;
    /** Button to start ClientServer activity. */
    Button btClientServer;
    /** Button to start service. */
    Button btStopService;
    /** Button to close current session. */
    Button btCloseSession;

    /** Whether or not the head is logged in. */
    CheckBox cbHead;

    /**
     * Constructs a new instance of {@code BillListFragment}.
     */
    public BillListFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_bill_list, container, false);

        sp = getActivity().getSharedPreferences(SP_NAME, getActivity().MODE_PRIVATE);

        openData();

        lvBills = (ListView) rootView.findViewById(R.id.lvBills);
        billsAdapter = new BillsArrayAdapter(getActivity(), bills);
        lvBills.setAdapter(billsAdapter);

        btClientServer = (Button) rootView.findViewById(R.id.btClientServer);

        btClientServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ClientServerActivity.class);
                context.startActivity(intent);
            }
        });

        btStartService = (Button) rootView.findViewById(R.id.btStartService);

        btStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(activity, FirstService.class);
                intent.setAction("com.gnsit.Service");
                activity.startService(intent);*/
                Intent myIntent = new Intent(activity, FirstService.class);
                myIntent.setAction("com.gnsit.Service");
                PendingIntent pendingIntent = PendingIntent.getService(activity, 0, myIntent, 0);

                AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, 3); // first time
                long frequency= 5 * 1000; // in ms
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);
            }
        });

        btStopService = (Button) rootView.findViewById(R.id.btStopService);

        btStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(activity, FirstService.class);
                intent.setAction("com.gnsit.Service");
                activity.stopService(intent);*/
                Intent myIntent = new Intent(activity, FirstService.class);
                myIntent.setAction("com.gnsit.Service");
                PendingIntent pendingIntent = PendingIntent.getService(activity, 0, myIntent, 0);

                AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                //  calendar.add(Calendar.SECOND, 3); // first time
                long frequency= 5 * 1000; // in ms
                alarmManager.cancel(pendingIntent);
            }
        });

        cbHead = (CheckBox) rootView.findViewById(R.id.cbHead);
        btCloseSession = (Button) rootView.findViewById(R.id.btCloseSession);

        if (getArguments().getBoolean("is_head", false)) {
            btCloseSession.setVisibility(View.VISIBLE);
            cbHead.setChecked(true);
        }

        cbHead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btCloseSession.setVisibility(View.VISIBLE);
                } else {
                    btCloseSession.setVisibility(View.GONE);
                }
            }
        });

        lvBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*    Context context = view.getContext();
                Intent intent = new Intent(context, BillDetailActivity.class);
                intent.putExtra("bill_title", bills.get(position).getTitle());
                intent.putExtra("bill_num", position + 1);
                intent.putExtra("bill_corr", bills.get(position).getCorrects());
                intent.putExtra("reporters", getReporters(position));
                intent.putExtra("me_report", meReport);
                intent.putExtra("is_head", cbHead.isChecked());
                intent.putExtra("is_voted", bills.get(position).getAccepted());
                intent.putExtra("votes_for", bills.get(position).getVotesFor());
                intent.putExtra("votes_against", bills.get(position).getVotesAgainst());
                intent.putExtra("votes_refr", bills.get(position).getVotesRefrained());
                context.startActivity(intent);*/

                Bundle args = new Bundle();
                args.putString("bill_title", bills.get(position).getTitle());
                args.putInt("bill_num", position + 1);
                args.putString("bill_corr", bills.get(position).getCorrects());
                args.putString("reporters", getReporters(position));
                args.putBoolean("me_report", meReport);
                args.putBoolean("is_head", cbHead.isChecked());
                args.putInt("is_voted", bills.get(position).getAccepted());
                args.putInt("votes_for", bills.get(position).getVotesFor());
                args.putInt("votes_against", bills.get(position).getVotesAgainst());
                args.putInt("votes_refr", bills.get(position).getVotesRefrained());

                //   ((MainActivity) getActivity()).openBillDetail(args);

                ((MainActivity) getActivity()).changeFragment(new BillDetailFragment(), args, "bill_detail_fragm", "bill_list_fragm");
            }
        });

        return rootView;
    }

    /**
     * Gets data from SharedPreferences or creates it
     * if SP does not contain the data.
     */
    private void openData() {
        bills = new ArrayList<>();
        String s;

        if (sp.contains(USER_DATA)) {
            s = sp.getString(USER_DATA, "");
            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.optJSONArray("bills");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.optJSONObject(i);
                    Bill bill = new Bill(
                            jObj.optString("title"),
                            jObj.optString("corrects"),
                            jObj.optInt("choice"),
                            jObj.optInt("accept"),
                            jObj.optInt("votes_for"),
                            jObj.optInt("votes_against"),
                            jObj.optInt("votes_refrained")
                    );
                    bills.add(bill);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject jsonObj = new JSONObject();
                JSONArray jArr = new JSONArray();

                bills.add(new Bill("Про прийняття за основу проекту Закону України про внесення змін до деяких законодавчих актів України щодо удосконалення процедури відведення земельних ділянок при наданні їх для суспільних потреб чи з мотивів суспільної необхідності",
                        "Поправка 1 тестова",
                        Bill.VOTED_FOR, Bill.ACCEPTED, 80, 20, 0));
                bills.add(new Bill("Про визнання такими, що втратили чинність, деяких законів України щодо державного регулювання виробництва і реалізації цукру",
                        "Most developers will not implement this class directly, instead using the aidl tool to describe the desired interface, having it generate the appropriate Binder subclass. You can, however, derive directly from Binder to implement your own custom RPC protocol or simply instantiate a raw Binder object directly to use as a token that can be shared across processes.\n" +
                                "\n" +
                                "This class is just a basic IPC primitive; it has no impact on an application's lifecycle, and is valid only as long as the process that created it continues to run. To use this correctly, you must be doing so within the context of a top-level application component (a Service, Activity, or ContentProvider) that lets the system know your process should remain running." +
                                "\n" +
                                "This class is just a basic IPC primitive; it has no impact on an application's lifecycle, and is valid only as long as the process that created it continues to run. To use this correctly, you must be doing so within the context of a top-level application component (a Service, Activity, or ContentProvider) that lets the system know your process should remain running.",
                        Bill.NOT_VOTED, Bill.IN_QUEUE, 0, 0, 0));
                bills.add(new Bill("Про внесення змін до деяких законодавчих актів України (щодо заборони продажу алкогольних напоїв особам у військовій формі одягу)",
                        "Поправка 3 тестова",
                        Bill.VOTED_FOR, Bill.DECLINED, 20, 80, 0));
                bills.add(new Bill("Про внесення змін до Гірничого закону України (щодо соціальних гарантій працівникам гірничих підприємств під час ліквідації або консервації)",
                        "Поправка 4 тестова",
                        Bill.NOT_VOTED, Bill.IN_QUEUE, 0, 0, 0));
                bills.add(new Bill("Про Економічний експеримент на території міста Одеси",
                        "Поправка 5 тестова",
                        Bill.VOTED_FOR, Bill.ACCEPTED, 78, 22, 0));
                bills.add(new Bill("Про внесення змін до пункту 4 розділу ХХI \"Прикінцеві та перехідні положення\" Митного кодексу України (щодо забезпечення виконання Україною зобов'язань, передбачених міжнародними договорами (угодами) України з питань економічної діяльності",
                        "Поправка 6 тестова",
                        Bill.NOT_VOTED, Bill.IN_QUEUE, 0, 0, 0));
                bills.add(new Bill("Про доручення Кабінету Міністрів України щодо створення Інтернет-ресурсу \"Мій звіт\"",
                        "Поправка 7 тестова",
                        Bill.REFRAINED_FROM_VOTING, Bill.DECLINED, 30, 70, 0));
                bills.add(new Bill("Про внесення змін до Закону України \"Про засади державної мовної політики\" (щодо особливостей вживання мови зовнішніх та публічних написів і топонімів)",
                        "Поправка 8 тестова",
                        Bill.VOTED_FOR, Bill.ACCEPTED, 90, 10, 0));
                bills.add(new Bill("Про внесення змін до деяких законів України щодо функціонування районних рад",
                        "Поправка 9 тестова",
                        Bill.VOTED_AGAINST, Bill.ACCEPTED, 100, 0, 0));
                bills.add(new Bill("Про внесення змін до деяких законодавчих актів України (щодо виключення Держенергоефективності з переліку контролюючих органів)",
                        "Поправка 10 тестова",
                        Bill.NOT_VOTED, Bill.IN_QUEUE, 0, 0, 0));
                bills.add(new Bill("Про внесення змін до Законів України \"Про страховий фонд документації України\" та \"Про Національний архівний фонд та архівні установи\" (щодо окремих понять)",
                        "Поправка 11 тестова",
                        Bill.NOT_VOTED, Bill.IN_QUEUE, 0, 0, 0));
                bills.add(new Bill("Про внесення змін до деяких законів України щодо особливостей управління державними банками",
                        "Поправка 12 тестова",
                        Bill.VOTED_FOR, Bill.ACCEPTED, 95, 2, 3));
                bills.add(new Bill("Про внесення зміни до статті 12 Закону України \"Про Антимонопольний комітет України\" щодо утворення міжрегіональних територіальних відділень Антимонопольного комітету України",
                        "Поправка 13 тестова",
                        Bill.VOTED_AGAINST, Bill.DECLINED, 40, 58, 2));
                bills.add(new Bill("Про доступ до інфраструктури об'єктів будівництва, транспорту, електроенергетики для розвитку телекомунікаційних мереж",
                        "Поправка 14 тестова",
                        Bill.REFRAINED_FROM_VOTING, Bill.ACCEPTED, 88, 12, 0));
                bills.add(new Bill("Про повернення на доопрацювання проекту Закону України про правовий статус і соціальні гарантії учасників добровольчих збройних формувань в Україні",
                        "Поправка 15 тестова",
                        Bill.NOT_VOTED, Bill.IN_QUEUE, 0, 0, 0));

                for (int i = 0; i < bills.size(); i++) {
                    JSONObject jObj = new JSONObject();
                    jObj.put("title", bills.get(i).getTitle());
                    jObj.put("corrects", bills.get(i).getCorrects());
                    jObj.put("choice", bills.get(i).getChoice());
                    jObj.put("accept", bills.get(i).getAccepted());
                    jObj.put("votes_for", bills.get(i).getVotesFor());
                    jObj.put("votes_against", bills.get(i).getVotesAgainst());
                    jObj.put("votes_refrained", bills.get(i).getVotesRefrained());
                    jObj.put("me_report", false);

                    Random rand = new Random();
                    int n = 3 + rand.nextInt((10 - 3) + 1);
                    JSONArray jArrReport = new JSONArray();
                    JSONObject jObj2;

                    jObj2 = new JSONObject();
                    jObj2.put("name", "Василь Петрович");
                    jObj2.put("district", "Test district");
                    jArrReport.put(jObj2);
                    jObj2 = new JSONObject();
                    jObj2.put("name", "Андрій Богданович");
                    jObj2.put("district", "Test district");
                    jArrReport.put(jObj2);
                    jObj2 = new JSONObject();
                    jObj2.put("name", "Михайло Сергійович");
                    jObj2.put("district", "Test district");
                    jArrReport.put(jObj2);
                    for (int j = 3; j < n; j++) {
                        jObj2 = new JSONObject();
                        jObj2.put("name", "Тетяна Іванівна");
                        jObj2.put("district", "Test district");
                        jArrReport.put(jObj2);
                    }
                    jObj.put("reports", jArrReport);
                    jArr.put(jObj);
                }
                jsonObj.put("bills", jArr);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString(USER_DATA, jsonObj.toString());
                //editor.commit();
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets list of reporters of the chosen bill.
     *
     * @param position position of the bill.
     *
     * @return list of reporters.
     */
    private String getReporters(int position) {
        String s;

        if (sp.contains(USER_DATA)) {
            s = sp.getString(USER_DATA, "");
            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.optJSONArray("bills");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.optJSONObject(i);
                    if (jObj.optString("title").equals(bills.get(position).getTitle())) {
                        meReport = jObj.optBoolean("me_report");
                        return jObj.optJSONArray("reports").toString();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return new JSONArray().toString();

    }
}

