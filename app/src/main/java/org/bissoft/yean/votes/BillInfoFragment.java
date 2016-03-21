package org.bissoft.yean.votes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.bissoft.yean.votes.util.Document;
import org.bissoft.yean.votes.util.Reporter;
import org.bissoft.yean.votes.util.ReportersArrayAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * A fragment representing a single bill screen
 * when the deputy is logged in and the bill was not voted for.
 * @author      Nataliia Makarenko
 */
public class BillInfoFragment extends Fragment implements ReportDialogFragment.ReportDialogListener {
    /** Name of SharedPreferences. */
    public static final String SP_NAME = "VOTES_SP";
    /** Name of data in SharedPreferences. */
    public static final String USER_DATA = "USER_DATA";

    /** Instance of SharedPreferences. */
    SharedPreferences sp;

    /** Button to enroll in list of reporters. */
    Button btEnroll;
    /** Button to start voting for the bill. */
    Button btStartPoll;
    /** The title of the bill. */
    TextView tvBillTitleDetail;
    /** The corrects of the bill. */
    TextView tvBillCorrDetail;
    /** The ListView of reporters. */
    ListView lvReporters;
    /** The adapter for the list of reporters. */
    ReportersArrayAdapter reportersAdapter;
    /** The list of reporters. */
    ArrayList<Reporter> reporters;
    /** The list of documents. */
    ArrayList<Document> documents;

    /** The left column of documents. */
    LinearLayout llDocs1;
    /** The right column of documents. */
    LinearLayout llDocs2;

    /** Whether or not the current deputy is in list of reporters. */
    boolean meReport;

    /**
     * Constructs a new instance of {@code BillInfoFragment}.
     */
    public BillInfoFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_bill_info, container, false);

        sp = getActivity().getSharedPreferences(SP_NAME, getActivity().MODE_PRIVATE);

        btEnroll = (Button) rootView.findViewById(R.id.btEnroll);
        btStartPoll = (Button) rootView.findViewById(R.id.btStartPoll);

        tvBillTitleDetail = (TextView) rootView.findViewById(R.id.tvBillTitleDetail);

        tvBillTitleDetail.setText(getArguments().getString("bill_title", ""));

        tvBillCorrDetail = (TextView) rootView.findViewById(R.id.tvBillCorrDetail);
        if (!getArguments().getString("bill_corr", "").equals("")) {
            tvBillCorrDetail.setText(getArguments().getString("bill_corr"));
        } else {
            tvBillCorrDetail.setVisibility(View.GONE);
            TextView tvBillCorr = (TextView) rootView.findViewById(R.id.tvBillCorr);
            tvBillCorr.setVisibility(View.GONE);
        }

        meReport = getArguments().getBoolean("me_report", false);

        lvReporters = (ListView) rootView.findViewById(R.id.lvReporters);

        setReporters(getArguments().getString("reporters", ""),
                getArguments().getBoolean("me_report", false));

        if (meReport) {
            btEnroll.setText("Відмовитися");
        } else {
            btEnroll.setText("Записатися");
        }

        reportersAdapter = new ReportersArrayAdapter(getActivity(), reporters);
        lvReporters.setAdapter(reportersAdapter);

        documents = new ArrayList<>();

        documents.add(new Document("Проект рішення.docx", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Votes/Проект рішення.docx"));
        documents.add(new Document("Аналіз бюджету.xlsx", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Votes/Аналіз бюджету.xlsx"));
        documents.add(new Document("Проектні документи.pdf", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Votes/Проектні документи.pdf"));
        documents.add(new Document("Фото будинку.png", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Votes/Фото будинку.png"));

        llDocs1 = (LinearLayout) rootView.findViewById(R.id.llDocs1);
        llDocs2 = (LinearLayout) rootView.findViewById(R.id.llDocs2);

        for (int i = 0; i < documents.size(); i++) {
            addDocument(i);
        }

        btEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btStartPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogStartPoll editNameDialog = DialogStartPoll.newInstance();
                editNameDialog.setCancelable(false);
                editNameDialog.setArguments(getArguments());
                editNameDialog.show(fragmentManager, "dialog_start_poll");
            }
        });

        return rootView;
    }

    /**
     * Shows the dialog to sign / decline the signing
     * the deputy to the list of reporters.
     */
    private void showDialog() {
        Bundle args = new Bundle();
        if (meReport) {
            args.putString("message", "Бажаєте відмінити виступ?");
            args.putBoolean("reporter", true);
        } else {
            args.putString("message", "Бажаєте записатися на доповідь?");
            args.putBoolean("reporter", false);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ReportDialogFragment rdf = new ReportDialogFragment();
        rdf.setArguments(args);
        rdf.setTargetFragment(this, 0);
        rdf.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        rdf.show(ft, "dialogReport");
    }

    /**
     * Final instructions after exiting from dialog,
     * in which the deputy can sign / decline
     * the signing to the list of reporters.
     *
     * @param inReporter position of the item.
     */
    @Override
    public void onFinishReportDialog(boolean inReporter) {
        meReport = inReporter;
        if (meReport) {
            btEnroll.setText("Відмовитися");
            reporters.add(new Reporter("Наталія Макаренко", "Test department"));
        } else {
            btEnroll.setText("Записатися");
            for (int i = 0; i < reporters.size(); i++) {
                if (reporters.get(i).getName().equals("Наталія Макаренко")) {
                    reporters.remove(i);
                    break;
                }
            }
        }
        reportersAdapter.notifyDataSetChanged();

        String s;
        if (sp.contains(USER_DATA)) {
            s = sp.getString(USER_DATA, "");
            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.optJSONArray("bills");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.optJSONObject(i);
                    if (jObj.optString("title").equals(tvBillTitleDetail.getText().toString())) {
                        JSONArray jArrReport = new JSONArray();
                        JSONObject jObj2;

                        for (int j = 0; j < reporters.size(); j++) {
                            jObj2 = new JSONObject();
                            jObj2.put("name", reporters.get(j).getName());
                            jObj2.put("district", reporters.get(j).getPost());
                            jArrReport.put(jObj2);
                        }
                        jObj.put("reports", jArrReport);
                        jObj.put("me_report", meReport);
//                        jsonArray.put(jObj);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(USER_DATA, jsonObj.toString());
                        //editor.commit();
                        editor.apply();
                        return;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds the document in list of documents.
     *
     * @param position position of the document in list.
     */
    private void addDocument(final int position) {
        LinearLayout llDoc = new LinearLayout(getActivity());
        llDoc.setOrientation(LinearLayout.HORIZONTAL);
        ImageView iv = new ImageView(getActivity());
        TextView tv = new TextView(getActivity());

        tv.setPadding(10, 10, 0, 10);

        tv.setText(documents.get(position).getName());

        String filePath = documents.get(position).getPath();
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());

        switch (extension) {
            case "docx":
            case "doc":
                iv.setImageResource(R.drawable.doc);
                break;
            case "xls":
            case "xlsx":
                iv.setImageResource(R.drawable.xls);
                break;
            case "pdf":
                iv.setImageResource(R.drawable.pdf);
                break;
            case "jpg":
                iv.setImageResource(R.drawable.jpg);
                break;
            default:
                iv.setImageResource(R.drawable.jpg);
                break;
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDocument(documents.get(position).getPath());
            }
        });

        llDoc.addView(iv);
        llDoc.addView(tv);
        if (position % 2 == 0) {
            llDocs1.addView(llDoc);
        } else {
            llDocs2.addView(llDoc);
        }
    }

    /**
     * Opens a document.
     *
     * @param name of document that must be opened.
     */
    public void openDocument(String name) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        File file = new File(name);
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            // if there is no extension or there is no definite mimetype, still try to open the file
            intent.setDataAndType(Uri.fromFile(file), "text/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), mimetype);
        }
        // custom message for the intent
        startActivity(Intent.createChooser(intent, "Choose an Application:"));
    }

    /**
     * Sets the list of reporters.
     *
     * @param reportersList the list of reporters.
     * @param meReport Whether or not the deputy is in the list of reporters.
     */
    private void setReporters (String reportersList, boolean meReport) {
        reporters = new ArrayList<>();

        if (reportersList.equals("")) {
            reporters.add(new Reporter("Іванова Тетяна Володимирівна", "Test Department"));
            reporters.add(new Reporter("Микола Степанович", "Test Department"));
            reporters.add(new Reporter("Ірина Василівна", "Test Department"));
            reporters.add(new Reporter("Олександр Ігорович", "Test Department"));
            reporters.add(new Reporter("Євгеній Олександрович", "Test Department"));
            reporters.add(new Reporter("Олексій Михайлович", "Test Department"));
        } else {
            try {
                JSONArray jArr = new JSONArray(reportersList);
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject jObj = jArr.optJSONObject(i);
                    reporters.add(new Reporter(
                            jObj.optString("name"),
                            jObj.optString("district")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

