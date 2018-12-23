package com.wuchangi.dailymood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuchangi.dailymood.R;
import com.wuchangi.dailymood.bean.Mood;
import com.wuchangi.dailymood.db.MoodTableDao;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoodDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_date)
    TextView mTvDate;

    @BindView(R.id.iv_picture)
    ImageView mIvPicture;

    @BindView(R.id.tv_description)
    TextView mTvDescription;

    private MoodTableDao mMoodTableDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_detail);

        initView();
    }


    private void initView() {
        ButterKnife.bind(this);

        mMoodTableDao = new MoodTableDao(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.your_mood));
        }

        String date = getIntent().getStringExtra("date");
        Mood mood = mMoodTableDao.findMoodByDate(date);

        String[] dateArray = date.split("/");

        mTvDate.setText(dateArray[0] + "年" + dateArray[1] + "月" + dateArray[2] + "日");
        mTvDescription.setText(mood.getDescription());
        Glide.with(this).load(mood.getPicturePath()).into(mIvPicture);
    }

    public static void actionStart(Context context, String date){
        Intent intent = new Intent(context, MoodDetailActivity.class);
        intent.putExtra("date", date);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
