package org.bissoft.yean.votes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bissoft.yean.votes.util.Document;

import java.io.File;
import java.util.ArrayList;

/**
 * A fragment representing a first screen in ViewPager of {@code BillInfoHeadFragment}
 * when the head is logged in and the bill was not voted for.
 * @author      Nataliia Makarenko
 */
public class BillInfoMainFragment extends Fragment {
    /** The title of bill. */
    TextView tvBillTitleDetail;
    /** The corrects of bill. */
    TextView tvBillCorrDetail;
    /** The list of documents. */
    ArrayList<Document> documents;

    /** The first column of documents. */
    LinearLayout llDocs1;
    /** The second column of documents. */
    LinearLayout llDocs2;
    /** The third column of documents. */
    LinearLayout llDocs3;

    /**
     * Constructs a new instance of {@code BillInfoMainFragment}.
     */
    public BillInfoMainFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_bill_info_main, container, false);

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

        documents = new ArrayList<>();

        documents.add(new Document("Проект рішення.docx", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Votes/Проект рішення.docx"));
        documents.add(new Document("Аналіз бюджету.xlsx", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Votes/Аналіз бюджету.xlsx"));
        documents.add(new Document("Проектні документи.pdf", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Votes/Проектні документи.pdf"));
        documents.add(new Document("Фото будинку.jpg", Environment.getExternalStorageDirectory().getAbsolutePath() + "/Votes/Фото будинку.jpg"));

        llDocs1 = (LinearLayout) rootView.findViewById(R.id.llDocs1);
        llDocs2 = (LinearLayout) rootView.findViewById(R.id.llDocs2);
        llDocs3 = (LinearLayout) rootView.findViewById(R.id.llDocs3);

        for (int i = 0; i < documents.size(); i++) {
            addDocument(i);
        }
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
        if (position % 3 == 0) {
            llDocs1.addView(llDoc);
        } else if (position % 3 == 1) {
            llDocs2.addView(llDoc);
        } else {
            llDocs3.addView(llDoc);
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

