package com.jhzy.receptionevaluation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.dispensingdrug.DispenseActivity;
import com.jhzy.receptionevaluation.ui.dispensingdrug.DispenseMainActivity;
import com.jhzy.receptionevaluation.utils.CONTACT;

/**
 * A simple {@link Fragment} subclass.
 * 是菜单选择界面，菜单项是配药，发药，胰岛素。
 */
public class DrugFragment extends Fragment {
    private View btnDispense;
    private View btnDelivery;
    private View btnInsulin;


    public static DrugFragment newInstance() {
        DrugFragment fragment = new DrugFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drug, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDispense = view.findViewById(R.id.btnDispense);
        btnDelivery = view.findViewById(R.id.btnDelivery);
        btnInsulin = view.findViewById(R.id.btnInsulin);
        btnDispense.setOnClickListener(onClickListener);
        btnDelivery.setOnClickListener(onClickListener);
        btnInsulin.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.btnDispense:
                    bundle.putInt(CONTACT.DispenseDrugContact.TYPE, 1);
                    intent.setClass(getContext(),DispenseActivity.class);
                    break;
                case R.id.btnDelivery:
                    bundle.putInt(CONTACT.DispenseDrugContact.TYPE, 2);
                    intent.setClass(getContext(),DispenseMainActivity.class);
                    break;
                case R.id.btnInsulin:
                    bundle.putInt(CONTACT.DispenseDrugContact.TYPE, 3);
                    intent.setClass(getContext(),DispenseMainActivity.class);
                    break;
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
}
