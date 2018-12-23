package com.wuchangi.dailymood.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wuchangi.dailymood.R;
import com.wuchangi.dailymood.bean.Mood;
import com.wuchangi.dailymood.constants.Constants;
import com.wuchangi.dailymood.db.MoodTableDao;
import com.wuchangi.dailymood.utils.PictureUriUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 同时充当心情卡片创建界面和编辑界面
 *
 */
public class CreateMoodActivity extends AppCompatActivity {

    @BindView(R.id.btn_select_date)
    Button mBtnSelectDate;

    @BindView(R.id.tv_year)
    TextView mTvYear;

    @BindView(R.id.tv_month)
    TextView mTvMonth;

    @BindView(R.id.tv_day)
    TextView mTvDay;

    @BindView(R.id.et_description)
    EditText mEtDescription;

    @BindView(R.id.btn_select_picture)
    Button mBtnSelectPicture;

    @BindView(R.id.iv_picture)
    ImageView mIvPicture;

    @BindView(R.id.btn_save)
    Button mBtnSave;

    private boolean isEditCard = false;

    private MoodTableDao mMoodTableDao;

    private static int mYear;
    private static int mMonth;
    private static int mDay;

    private boolean isSelectDate = false;
    private boolean isSelectPicture = false;

    private String mDate;
    private String mDescription;
    private String mPicturePath;

    static {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

        mMoodTableDao = new MoodTableDao(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            String date = getIntent().getStringExtra("date");

            // 进入编辑界面
            if (date != null) {
                isEditCard = true;

                actionBar.setTitle(getResources().getString(R.string.edit_card));

                Mood mood = mMoodTableDao.findMoodByDate(date);

                String[] dateArray = date.split("/");

                mTvYear.setText(dateArray[0]);
                mTvMonth.setText(dateArray[1]);
                mTvDay.setText(dateArray[2]);

                mEtDescription.setText(mood.getDescription());
                mPicturePath = mood.getPicturePath();

                Glide.with(this).load(mood.getPicturePath())
                        .into(mIvPicture);

            }
            // 进入创建界面
            else {
                actionBar.setTitle(getResources().getString(R.string.create_card));
            }
        }
    }


    @OnClick({R.id.btn_select_date, R.id.btn_select_picture, R.id.btn_save})
    public void handleAllClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_date:
                selectDate();
                break;

            case R.id.btn_select_picture:
                selectPicture();
                break;

            case R.id.btn_save:
                saveMoodCard();
                break;

            default:
                break;
        }
    }


    private void selectDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                isSelectDate = true;

                mTvYear.setText("" + mYear);
                mTvMonth.setText("" + (mMonth + 1));
                mTvDay.setText("" + mDay);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }


    private void selectPicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.SELECT_PICTURE);
        } else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.OPEN_ALBUM);
    }

    private void saveMoodCard() {
        // 当前为心情卡片创建界面
        if (!isEditCard) {
            if (!isSelectDate) {
                Toast.makeText(this, getResources().getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isSelectPicture) {
                Toast.makeText(this, getResources().getString(R.string.please_select_picture), Toast.LENGTH_SHORT).show();
                return;
            }

            mDate = mYear + "/" + (mMonth + 1) + "/" + mDay;
            mDescription = mEtDescription.getText().toString();

            final Mood mood = new Mood(mDate, mDescription, mPicturePath);

            // 日期 mDate 已有数据
            if (mMoodTableDao.findMoodByDate(mDate) != null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(mDate + " " + getResources().getString(R.string.is_override));
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMoodTableDao.addMood(mood);
                        MainActivity.mMoodListAdapter.replaceMood(mood);
                        Toast.makeText(CreateMoodActivity.this, getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CreateMoodActivity.this, getResources().getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            } else {
                mMoodTableDao.addMood(mood);
                MainActivity.mMoodListAdapter.addMood(mood);
                EventBus.getDefault().post(getResources().getString(R.string.create_card));
                Toast.makeText(this, getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();

                finish();
            }
        }
        // 当前为心情卡片编辑界面
        else {
            String date = mTvYear.getText() + "/" + mTvMonth.getText() + "/" + mTvDay.getText();
            String description = mEtDescription.getText().toString();
            Mood mood = new Mood(date, description, mPicturePath);

            // 日期 date 已有数据
            if (mMoodTableDao.findMoodByDate(date) != null && !date.equals(getIntent().getStringExtra("date"))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(date + " " + getResources().getString(R.string.existed_data));
                builder.setPositiveButton(getResources().getString(R.string.ok), null);
                builder.show();
            } else {
                mMoodTableDao.updateMood(getIntent().getStringExtra("date"), mood);
                MainActivity.mMoodListAdapter.updateMood(getIntent().getStringExtra("date"), mood);
                Toast.makeText(CreateMoodActivity.this, getResources().getString(R.string.modify_success), Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.SELECT_PICTURE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getResources().getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.hint), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.OPEN_ALBUM:
                if (data != null) {
                    Uri uri = data.getData();
                    mPicturePath = PictureUriUtil.getRealPathFromUri(this, uri);
                    mIvPicture.setImageURI(uri);
                    isSelectPicture = true;
                }
                break;

            default:
                break;
        }
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
        context.startActivity(new Intent(context, CreateMoodActivity.class));
    }

    public static void actionEditStart(Context context, String date) {
        Intent intent = new Intent(context, CreateMoodActivity.class);
        intent.putExtra("date", date);
        context.startActivity(intent);
    }
}
