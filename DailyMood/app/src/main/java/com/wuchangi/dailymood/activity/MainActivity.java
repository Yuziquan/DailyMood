package com.wuchangi.dailymood.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.wuchangi.dailymood.R;
import com.wuchangi.dailymood.adapter.MoodListAdapter;
import com.wuchangi.dailymood.bean.Mood;
import com.wuchangi.dailymood.db.MoodTableDao;
import com.wuchangi.dailymood.service.BGMService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_mood_list)
    RecyclerView mRvMoodList;

    @BindView(R.id.tv_empty_hint)
    TextView mTvEmptyHint;

    @BindView(R.id.fab_menu)
    FloatingActionsMenu mFloatingActionsMenu;

    public static MoodListAdapter mMoodListAdapter;
    private MoodTableDao mMoodTableDao;

    private List<Mood> mMoodList;

    private SharedPreferences mSPRadioButtonsState = null;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData(){
        EventBus.getDefault().register(this);

        mMoodTableDao = new MoodTableDao(this);
        mMoodList = mMoodTableDao.listMoods();
        mMoodListAdapter = new MoodListAdapter(this, mMoodList, mMoodTableDao);
    }

    private void initView() {
        ButterKnife.bind(this);

        if(mMoodList.size() > 0){
            mTvEmptyHint.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvMoodList.setLayoutManager(linearLayoutManager);
        mRvMoodList.setAdapter(mMoodListAdapter);
        mRvMoodList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mFloatingActionsMenu.collapse();
            }
        });


        mSPRadioButtonsState = getSharedPreferences("radio_buttons_state", MODE_PRIVATE);
        mEditor = mSPRadioButtonsState.edit();

        // 第一次使用该应用
        if(!mSPRadioButtonsState.contains("music_state")){
            // 默认背景音乐为开启状态
            mEditor.putString("music_state", "on");
            mEditor.commit();
        }

        // 根据之前保存的状态决定是否播放背景音乐
        if(mSPRadioButtonsState.getString("music_state", "on").equals("on")){
            Intent intent = new Intent(this, BGMService.class);
            startService(intent);
        }
    }


    @OnClick({R.id.fab_create_card, R.id.fab_clear_all_cards})
    public void handleAllClick(View v) {
        switch (v.getId()) {
            case R.id.fab_create_card:
                CreateMoodActivity.actionStart(this);
                mFloatingActionsMenu.collapse();
                break;

            case R.id.fab_clear_all_cards:
                clearAllCards();
                mFloatingActionsMenu.collapse();
                break;

            default:
                break;
        }
    }


    private void clearAllCards() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.is_clear_all));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mMoodListAdapter.removeAllMoods();
                mMoodTableDao.removeAllMoods();
                mTvEmptyHint.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.clear_success), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCardCreated(String createCard){
        mTvEmptyHint.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                SettingsActivity.actionStart(this);
                break;

            case R.id.menu_item_about:
                AboutActivity.actionStart(this);
                break;

            default:
                break;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
