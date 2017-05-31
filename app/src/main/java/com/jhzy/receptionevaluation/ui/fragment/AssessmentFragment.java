package com.jhzy.receptionevaluation.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.AssessActivity;


/**
 * bigyu 2017年2月20日11:24:04
 * 评估
 */
@Deprecated
public class AssessmentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ViewHolder vh;
    private Context mContext;


    public AssessmentFragment() {
    }

    public static AssessmentFragment newInstance(String param1, String param2) {
        AssessmentFragment fragment = new AssessmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment, container, false);
        mContext = getContext();
        initUtils(view);
        initViewData();
        initListener();
        return view;
    }


    private void initUtils(View view) {
        vh = new ViewHolder(view);
    }

    private void initViewData() {


    }

    private void initListener() {
        vh.btn.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                AssessActivity.start(mContext, null);
            }
        });
    }


    class ViewHolder {
        private View btn;
        private GridView gridview;

        ViewHolder(View view) {
            btn = view.findViewById(R.id.list);
            gridview = ((GridView) view.findViewById(R.id.grid_view));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("---------", "setUserVisibleHint: " + isVisibleToUser + mParam2);
    }


}

