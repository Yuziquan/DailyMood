package com.wuchangi.dailymood.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wuchangi.dailymood.R;
import com.wuchangi.dailymood.service.BGMService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.rb_bgm_on)
    RadioButton mRbBGMOn;

    @BindView(R.id.rb_bgm_off)
    RadioButton mRbBGMOff;

    @BindView(R.id.rg_bgm_switch)
    RadioGroup mRgBGMSwitch;

    @BindView(R.id.sb_music_volume)
    SeekBar mSbMusicVolume;

    @BindView(R.id.tv_min_volume)
    TextView mTvMinVolume;

    @BindView(R.id.tv_cur_volume)
    TextView mTvCurVolume;

    @BindView(R.id.tv_max_volume)
    TextView mTvMaxVolume;

    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mCurVolume;
    private SharedPreferences mSPRadioButtonsState = null;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }


    private void initView() {
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.settings));
        }

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(mAudioManager.STREAM_MUSIC);
        mTvMaxVolume.setText(mMaxVolume + "");

        // 音量控制进度条的最大值设置为系统音量最大值
        mSbMusicVolume.setMax(mMaxVolume);

        mCurVolume = mAudioManager.getStreamVolume(mAudioManager.STREAM_MUSIC);
        mTvCurVolume.setText(mCurVolume + "");
        // 设置音量控制进度条当前值为系统音量当前值
        mSbMusicVolume.setProgress(mCurVolume);

        // 设置界面中的单选按钮被选中的状态
        mSPRadioButtonsState = getSharedPreferences("radio_buttons_state", MODE_PRIVATE);
        mEditor = mSPRadioButtonsState.edit();

        if (mSPRadioButtonsState.getString("music_state", "on").equals("on")) {
            mRbBGMOn.setChecked(true);
        } else {
            mRbBGMOff.setChecked(true);
        }

        mRgBGMSwitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent = new Intent(SettingsActivity.this, BGMService.class);

                switch (i){
                    case R.id.rb_bgm_on:
                        startService(intent);
                        mEditor.putString("music_state", "on");
                        break;

                    case R.id.rb_bgm_off:
                        stopService(intent);
                        mEditor.putString("music_state", "off");
                        break;
                }

                mEditor.commit();
            }
        });


        mSbMusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 数值的改变
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 如果是用户拖动
                if(fromUser){
                    mAudioManager.setStreamVolume(mAudioManager.STREAM_MUSIC, progress, 0);
                }

                mTvCurVolume.setText(progress + "");
            }

            // 开始拖动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // 停止拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }
}
