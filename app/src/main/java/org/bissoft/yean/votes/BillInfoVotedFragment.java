package org.bissoft.yean.votes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bissoft.yean.votes.util.Bill;
import org.bissoft.yean.votes.util.CustomDiagram;
import org.bissoft.yean.votes.util.Document;

import java.io.File;
import java.util.ArrayList;

/**
 * A fragment representing a single bill screen
 * when the bill was voted for.
 * @author      Nataliia Makarenko
 */
public class BillInfoVotedFragment extends Fragment {
    /** Button to see the results of voting. */
    Button btResults;
    /** The title of the bill. */
    TextView tvBillTitleDetail;
    /** The corrects of the bill. */
    TextView tvBillCorrDetail;
    /** TextView in which we tell whether
     * the bill was accepted. */
    TextView tvBillAccepted;
    /** The list of documents. */
    ArrayList<Document> documents;

    /** TextView that represents number of deputies. */
    TextView tvAllRes;
    /** TextView that represents number of votes
     * for the bill. */
    TextView tvForRes;
    /** TextView that represents number of votes
     * refrained voting for the bill. */
    TextView tvRefrRes;
    /** TextView that represents number of votes
     * against the bill. */
    TextView tvAgRes;
    /** TextView that represents number of deputies
     * that have not voted for the bill. */
    TextView tvNotVotRes;

    /** View that represents the diagram of results of voting. */
    CustomDiagram customImage_Progress;

    /** The left column of documents. */
    LinearLayout llDocs1;
    /** The right column of documents. */
    LinearLayout llDocs2;

    /** Number of votes for the bill. */
    int votesFor;
    /** Number of votes against the bill. */
    int votesAgainst;
    /** Number of votes refrained voting for the bill. */
    int votesRefr;

    /**
     * Constructs a new instance of {@code BillInfoVotedFragment}.
     */
    public BillInfoVotedFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_bill_info_voted, container, false);

        btResults = (Button) rootView.findViewById(R.id.btResults);

        tvBillTitleDetail = (TextView) rootView.findViewById(R.id.tvBillTitleDetail);

        tvBillTitleDetail.setText(getArguments().getString("bill_title", ""));

        tvBillCorrDetail = (TextView) rootView.findViewById(R.id.tvBillCorrDetail);

        if (!getArguments().getString("bill_corr", "").equals("")) {
            tvBillCorrDetail.setText(getArguments().getString("bill_corr", ""));
        } else {
            tvBillCorrDetail.setVisibility(View.GONE);
            TextView tvBillCorr = (TextView) rootView.findViewById(R.id.tvBillCorr);
            tvBillCorr.setVisibility(View.GONE);
        }

        tvBillAccepted = (TextView) rootView.findViewById(R.id.tvBillInfoAccepted);

        if (getArguments().getInt("is_voted", Bill.DECLINED) == Bill.ACCEPTED) {
            tvBillAccepted.setText("Рішення прийнято");
        } else {
            tvBillAccepted.setText("Рішення не прийнято");
        }

        votesFor = getArguments().getInt("votes_for", 0);
        votesAgainst = getArguments().getInt("votes_against", 0);
        votesRefr = getArguments().getInt("votes_refr", 0);

        customImage_Progress = (CustomDiagram) rootView.findViewById(R.id.customImage_Progress);
        customImage_Progress.setParams(new int[]{votesFor, votesAgainst, votesRefr, 0});

        tvAllRes = (TextView) rootView.findViewById(R.id.tvAllRes);
        tvForRes = (TextView) rootView.findViewById(R.id.tvForRes);
        tvRefrRes = (TextView) rootView.findViewById(R.id.tvRefrRes);
        tvAgRes = (TextView) rootView.findViewById(R.id.tvAgRes);
        tvNotVotRes = (TextView) rootView.findViewById(R.id.tvNotVotRes);

        tvAllRes.setText(String.format("Всі - %d", votesFor + votesAgainst + votesRefr));
        tvForRes.setText(String.format("За - %d", votesFor));
        tvRefrRes.setText(String.format("Утримались - %d", votesRefr));
        tvAgRes.setText(String.format("Проти - %d", votesAgainst));
        tvNotVotRes.setText(String.format("Не голосували - %d", 0));

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

        btResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = getArguments();
                args.putBoolean("from_bill_info_fragm", true);
                ((MainActivity) getActivity()).changeFragment(new PollResultsFragment(),
                        args, "poll_results_fragm", "bill_info_fragm", args);
            }
        });

        return rootView;
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
}

