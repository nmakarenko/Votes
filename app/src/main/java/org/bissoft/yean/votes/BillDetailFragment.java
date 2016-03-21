package org.bissoft.yean.votes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.bissoft.yean.votes.util.Bill;

/**
 * A fragment representing a single bill detail screen.
 * @author      Nataliia Makarenko
 */
public class BillDetailFragment extends Fragment {
    /** Button to return to the list of bills. */
    Button btBack;
    /** Title of the Fragment. */
    TextView tvBillTitle;

    /**
     * Constructs a new instance of {@code BillDetailFragment}.
     */
    public BillDetailFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_bill_detail, container, false);
        if (getArguments().getInt("is_voted", Bill.IN_QUEUE) == Bill.IN_QUEUE) {
            if (!getArguments().getBoolean("is_head", false)) {
                BillInfoFragment fragment = new BillInfoFragment();
                fragment.setArguments(getArguments());
                getFragmentManager().beginTransaction()
                        .replace(R.id.containerInfo, fragment, "bill_info_fragm")
                        .commit();
            } else {
                BillInfoHeadFragment fragment = new BillInfoHeadFragment();
                fragment.setArguments(getArguments());
                getFragmentManager().beginTransaction()
                        .replace(R.id.containerInfo, fragment, "bill_detail_fragm_head")
                        .commit();
            }
        } else {
            BillInfoVotedFragment fragment = new BillInfoVotedFragment();
            fragment.setArguments(getArguments());
            getFragmentManager().beginTransaction()
                    .replace(R.id.containerInfo, fragment, "bill_detail_fragm_voted")
                    .commit();
        }
        //    fragment.setArguments(arguments);


        btBack = (Button) rootView.findViewById(R.id.btBack);

        tvBillTitle = (TextView) rootView.findViewById(R.id.tvBillTitle);
        //  if (getArguments().has("bill_num")) {
        tvBillTitle.setText(String.format("Проект №%d", getArguments().getInt("bill_num", 0)));
        //     }
//
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Context context = v.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);*/

                if (!HeadPollFragment.isVoting
                        && !BillInfoDiscussFragment.isSpeaking) {
                    Bundle args = new Bundle();
                    args.putBoolean("is_head", getActivity().getIntent().getBooleanExtra("is_head", false));
                    ((MainActivity) getActivity()).changeFragment(new BillListFragment(), args, "bill_list_fragm", "");
                }
            }
        });

        return rootView;
    }
}

