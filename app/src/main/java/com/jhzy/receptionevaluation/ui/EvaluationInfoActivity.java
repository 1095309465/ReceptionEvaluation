package com.jhzy.receptionevaluation.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.EvaluationGridViewAdapter;
import com.jhzy.receptionevaluation.ui.adapter.EvaluationInfoAdapter;
import com.jhzy.receptionevaluation.ui.bean.Person;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.utils.PinyinTool;
import com.jhzy.receptionevaluation.widget.MyTextDialog;
import com.jhzy.receptionevaluation.widget.MyView;

import java.util.Comparator;
import java.util.List;
@Deprecated
public class EvaluationInfoActivity extends AppCompatActivity {

    private ListView lv;
    private EditText ed_researsh;
    private ImageView iv_back;
    private PinyinTool tool;
    private MyView view;
    private MyTextDialog textDialog;

    private List<Elder> persons;
    private List<Elder> adapterList;

    private List<List<Elder>> mList;

    private EvaluationInfoAdapter infoAdapter;

    private GridView lv_easy;
    private EvaluationGridViewAdapter gridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_info);
        initView();
//        initData();
    }

    private void initView() {
        lv_easy = (GridView) findViewById(R.id.lv_easy);
        view = (MyView) findViewById(R.id.view);
        textDialog = (MyTextDialog) findViewById(R.id.tv_dialog);
        lv = (ListView) findViewById(R.id.lv);
        ed_researsh = (EditText) findViewById(R.id.ed_researsh);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        view = (MyView) findViewById(R.id.view);
        ed_researsh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                show(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.setOnTouchLetterListener(new MyView.OnTouchLetterListener() {
            @Override
            public void onActionDownAndMove(String c, int percent) {
                lv_easy.setVisibility(View.GONE);
                textDialog.setVisibility(View.VISIBLE);
                textDialog.setTextContent(c);

                for (int i = 0; i < mList.size(); i++) {
                    String letter = mList.get(i).get(0).getFirstLetter();
                    if (c.equals(letter)) {
                        lv.setSelection(i);
                    }
                }
            }

            @Override
            public void onActionUp() {
                textDialog.setVisibility(View.GONE);

            }
        });

    }

//    public void show(String content) {
//        content = String.valueOf(content).toUpperCase();
//        if (TextUtils.isEmpty(content)) {
//            lv_easy.setVisibility(View.GONE);
//            return;
//        }
//        lv_easy.setVisibility(View.VISIBLE);
//        adapterList.clear();
//        for (int i = 0; i < persons.size(); i++) {
//            Person person = persons.get(i);
//            if (person.getName().contains(content)) {
//                adapterList.add(person);
//                continue;
//            } else if (person.getAllLetter().contains(content)) {
//                adapterList.add(person);
//                continue;
//            } else if (person.getSell().contains(content)) {
//                adapterList.add(person);
//                continue;
//            }
//        }
//        gridAdapter.notifyDataSetChanged();
//    }

//    private void initData() {
//        tool = new PinyinTool();
//        persons = new ArrayList<>();
//        mList = new ArrayList<>();
//        adapterList = new ArrayList<>();
//        Person p1 = new Person("耿琦");
//        persons.add(p1);
//        Person p2 = new Person("王宝强");
//        persons.add(p2);
//        Person p3 = new Person("柳岩");
//        persons.add(p3);
//        Person p4 = new Person("文章");
//        persons.add(p4);
//        Person p5 = new Person("马伊琍");
//        persons.add(p5);
//        Person p6 = new Person("李晨");
//        persons.add(p6);
//        Person p7 = new Person("张馨予");
//        persons.add(p7);
//        Person p8 = new Person("韩红");
//        persons.add(p8);
//        Person p9 = new Person("韩寒");
//        persons.add(p9);
//        Person p10 = new Person("丹丹");
//        persons.add(p10);
//        Person p11 = new Person("丹凤眼");
//        persons.add(p11);
//        Person p12 = new Person("哈哈");
//        persons.add(p12);
//        Person p13 = new Person("萌萌");
//        persons.add(p13);
//        Person p14 = new Person("蒙混");
//        persons.add(p14);
//        Person p15 = new Person("烟花");
//        persons.add(p15);
//        Person p16 = new Person("眼黑");
//        persons.add(p16);
//        Person p17 = new Person("许三多");
//        persons.add(p17);
//        Person p18 = new Person("程咬金");
//        persons.add(p18);
//        Person p19 = new Person("程哈哈");
//        persons.add(p19);
//        Person p20 = new Person("爱死你");
//        persons.add(p20);
//        Person p21 = new Person("阿莱");
//        persons.add(p21);
//        Person p22 = new Person("耿琦");
//        persons.add(p22);
//        Person p23 = new Person("王宝强");
//        persons.add(p23);
//        Person p24 = new Person("柳岩");
//        persons.add(p24);
//        Person p25 = new Person("文章");
//        persons.add(p25);
//        Person p26 = new Person("马伊琍");
//        persons.add(p26);
//        Person p27 = new Person("李晨");
//        persons.add(p27);
//        Person p28 = new Person("张馨予");
//        persons.add(p28);
//        Person p29 = new Person("韩红");
//        persons.add(p29);
//        Person p30 = new Person("韩寒");
//        persons.add(p30);
//        Person p31 = new Person("丹丹");
//        persons.add(p31);
//        Person p32 = new Person("丹凤眼");
//        persons.add(p32);
//        Person p33 = new Person("韩寒");
//        persons.add(p33);
//        Person p34 = new Person("丹丹");
//        persons.add(p34);
//        Person p35 = new Person("丹凤眼");
//        persons.add(p35);
//        for (int i = 0; i < persons.size(); i++) {
//            Person person = persons.get(i);
//            String name = person.getName();
//            person.setAllLetter(PinYinUtil.getAllSpell(name, tool));//全拼音
//            person.setFirstLetter(PinYinUtil.getFirstSell(name, tool));//开头字母
//            person.setSell(PinYinUtil.getAllSell(name, tool));//每个文字的首字母
//        }
//        Collections.sort(persons, new MyCollator());//对数据源进行排序
//        adapterList.addAll(persons);
//        gridAdapter = new EvaluationGridViewAdapter(adapterList, this);
//        lv_easy.setAdapter(gridAdapter);
//
//        String letter = persons.get(0).getFirstLetter();
//        List<String> letterList = new ArrayList<>();
//        letterList.add(letter);
//        for (int i = 1; i < persons.size(); i++) {//对数据源进行字母分组
//            Person person = persons.get(i);
//            String nextLetter = person.getFirstLetter();
//            if (letter.equals(nextLetter)) {
//            } else {
//                letterList.add(nextLetter);
//                letter = nextLetter;
//            }
//        }
//        for (int i = 0; i < letterList.size(); i++) {
//            String str = letterList.get(i);
//            List<Person> pList = new ArrayList<>();
//            for (int j = 0; j < persons.size(); j++) {
//                Person person = persons.get(j);
//                String str2 = person.getFirstLetter();
//                if (str.equals(str2)) {
//                    pList.add(person);
//                }
//            }
//            mList.add(pList);
//        }
//        infoAdapter = new EvaluationInfoAdapter(mList, this);
//        lv.setAdapter(infoAdapter);
//        Log.e("123", "letterList=大小" + letterList.size());
//        Log.e("123", "letterList=详细" + letterList.toString());
//        Log.e("123", "  mList.add(pList)=" + mList.toString());
//    }

    public class MyCollator implements Comparator<Person> {
        @Override
        public int compare(Person lhs, Person rhs) {
            return String.valueOf(lhs.getFirstLetter()).compareTo(String.valueOf(rhs.getFirstLetter()));
        }//排序规则

    }


}
