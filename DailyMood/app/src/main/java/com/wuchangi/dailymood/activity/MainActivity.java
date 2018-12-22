package com.wuchangi.dailymood.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.wuchangi.dailymood.R;
import com.wuchangi.dailymood.adapter.MoodListAdapter;
import com.wuchangi.dailymood.bean.Mood;
import com.wuchangi.dailymood.db.MoodTableDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_mood_list)
    RecyclerView mRvMoodList;

    public static MoodListAdapter mMoodListAdapter;
    private MoodTableDao mMoodTableDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mMoodTableDao = new MoodTableDao(this);
        initView();
    }

    private void initView() {
        List<Mood> moodList = mMoodTableDao.listMoods();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvMoodList.setLayoutManager(linearLayoutManager);
        mMoodListAdapter = new MoodListAdapter(this, moodList);
        mRvMoodList.setAdapter(mMoodListAdapter);
    }


    @OnClick({R.id.fab_create_card, R.id.fab_clear_all_cards, R.id.fab_about})
    public void handleAllClick(View v) {
        switch (v.getId()) {
            case R.id.fab_create_card:
                CreateMoodActivity.actionStart(this);
                break;

            case R.id.fab_clear_all_cards:
                clearAllCards();
                break;

            case R.id.fab_about:
                AboutActivity.actionStart(this);
                break;

            default:
                break;
        }
    }



    private void clearAllCards() {
        mMoodListAdapter.removeAllMoods();
        mMoodTableDao.removeAllMoods();
        Toast.makeText(this, getResources().getString(R.string.clear_success), Toast.LENGTH_SHORT).show();
    }


}
