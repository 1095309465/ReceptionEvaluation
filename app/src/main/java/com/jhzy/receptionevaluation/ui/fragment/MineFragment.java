package com.jhzy.receptionevaluation.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.AboutActivity;
import com.jhzy.receptionevaluation.ui.dispensingdrug.InsulinActivity;
import com.jhzy.receptionevaluation.ui.login.LoginActivity;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.SPUtils;

/**
 * bigyu  2017年2月20日11:36:26
 * 我的
 */
public class MineFragment extends Fragment {

    private ViewHolder vh;
    private Context mContext;
    private String account;
    private String orgName;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mContext = getContext();
        initParams();
        initUtils(view);
        initListener();
        initView();
        return view;
    }

    private void initParams() {
        account = SPUtils.find(CONTACT.ACCOUNT);
        orgName = SPUtils.find(CONTACT.ORGNAME);
    }

    private void initView() {
        vh.account.setText("账户名 " + account);
        vh.nameView.setText(orgName);
    }


    private void initUtils(View view) {
        vh = new ViewHolder(view);
    }

    private void initListener() {
        vh.nameView.setOnClickListener(noDouble);
        vh.tv_loginout.setOnClickListener(noDouble);
        vh.mineLayout.setOnClickListener(noDouble);
    }

    class ViewHolder {
        TextView nameView;
        TextView tv_loginout;
        private TextView account;
        private View mineLayout;

        public ViewHolder(View view) {
            nameView = ((TextView) view.findViewById(R.id.name));
            tv_loginout = (TextView) view.findViewById(R.id.tv_loginout);
            account = ((TextView) view.findViewById(R.id.type));
            mineLayout = view.findViewById(R.id.mine_layout);
        }
    }

    /**
     * 监听回调
     */
    OnClickListenerNoDouble noDouble = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            switch (view.getId()) {
                case R.id.name:
                    /*Intent intent = new Intent(mContext, ElderInfoActivity.class);
                    startActivity(intent);*/
                    break;

                case R.id.tv_loginout://退出登录
                    SPUtils.updateOrSaveBoolean(CONTACT.LoginContact.AUTOMATIC, false);
                    startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                    getActivity().finish();
                    break;
                case R.id.mine_layout: // 关于
                    AboutActivity.start(mContext, null);
                    break;
            }
        }
    };

}
