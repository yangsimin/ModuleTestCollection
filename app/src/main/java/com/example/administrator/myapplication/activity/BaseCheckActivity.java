package com.example.administrator.myapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.fragment.BatteryTestFragment;
import com.example.administrator.myapplication.fragment.BlueToothTestFragment;
import com.example.administrator.myapplication.fragment.CheckTimeFragment;
import com.example.administrator.myapplication.fragment.GPRSTestFragment;
import com.example.administrator.myapplication.fragment.GPSTestFragment;
import com.example.administrator.myapplication.fragment.KeyPressTestFragment;
import com.example.administrator.myapplication.fragment.MultiTouchFragment;
import com.example.administrator.myapplication.fragment.NFCTestFragment;
import com.example.administrator.myapplication.fragment.OnlyTextViewFragment;
import com.example.administrator.myapplication.fragment.SDCardTestFragment;
import com.example.administrator.myapplication.fragment.SimpleInFrared;
import com.example.administrator.myapplication.fragment.SimpleLaserInFrared;
import com.example.administrator.myapplication.fragment.SimpleSafeUnit;
import com.example.administrator.myapplication.fragment.SleepTestFragment;
import com.example.administrator.myapplication.fragment.SoundTestFragment;
import com.example.administrator.myapplication.fragment.TouchTestFragment;
import com.example.administrator.myapplication.fragment.VibratorTestFragment;
import com.example.administrator.myapplication.utils.Constants;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class BaseCheckActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button btnWell, btnBad;

    private LinearLayout btnLayout;

    private Fragment fragment;
//    private MonitoHomeReceiver receiver;
    //    private View contentView;

    private boolean fullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        int position = getIntent().getIntExtra("position", 0);
        switch (position)
        {
            case 14:
            case 15:
                fullScreen = true;
                break;
            default:
                fullScreen = false;
        }
        if (fullScreen)
        {
            //隐藏虚拟按键
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
            //设置成全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_base_check);

        initBtn();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (position)
        {
            case 0:
                fragment = new CheckTimeFragment();
                break;
            case 1:
                fragment = new BatteryTestFragment();
                break;
            case 2:
                fragment = new VibratorTestFragment();
                break;
            case 3:
                fragment = new SDCardTestFragment();
                btnLayout.setVisibility(View.GONE);
                break;
            case 4:
                fragment = new BlueToothTestFragment();
                btnLayout.setVisibility(View.GONE);
                break;
            case 5:
                fragment = new GPRSTestFragment();
                btnLayout.setVisibility(View.GONE);
                break;
            case 6:
                fragment = new GPSTestFragment();
                btnLayout.setVisibility(View.GONE);
                break;
            case 7:
                fragment = new KeyPressTestFragment();
                btnLayout.setVisibility(View.GONE);
                break;
            case 8:
                fragment = new SoundTestFragment();
                break;
            case 9:
                fragment = new SleepTestFragment();
                break;
            case 10:
//                fragment = new LaserInfraredTestFragment();
//                btnLayout.setVisibility(View.GONE);
                fragment = new SimpleLaserInFrared();
                btnLayout.setVisibility(View.GONE);
                break;
            case 11:
//                fragment = new InfraredTestFragment();
//                btnLayout.setVisibility(View.GONE);
                fragment = new SimpleInFrared();
                btnLayout.setVisibility(View.GONE);
                break;
            case 12:
//                fragment = new SafeUnitTestFragment();
//                btnLayout.setVisibility(View.GONE);
                fragment = new SimpleSafeUnit();
                btnLayout.setVisibility(View.GONE);
                break;
            case 13:
                fragment = new NFCTestFragment();
                btnLayout.setVisibility(View.GONE);
                break;
            case 14:
                fragment = new MultiTouchFragment();
                btnLayout.setVisibility(View.GONE);
                break;
            case 15:
                fragment = new TouchTestFragment();
                btnLayout.setVisibility(View.GONE);
                break;
//            case 16:
//                fragment = new PMCCheckFragment();
//                btnLayout.setVisibility(View.GONE);
//                break;
            default:
                fragment = new OnlyTextViewFragment();
                Bundle b9 = new Bundle();
                b9.putString("content", "功能开发中...");
                fragment.setArguments(b9);
                btnLayout.setVisibility(View.GONE);
        }
        fragmentTransaction.replace(R.id.base_content_fl, fragment);
        fragmentTransaction.commit();
    }

    private void initBtn()
    {
        btnLayout = (LinearLayout) findViewById(R.id.base_btnLayout);

        btnWell = (Button) findViewById(R.id.base_well_btn);

        btnBad = (Button) findViewById(R.id.base_bad_btn);

        btnWell.setOnClickListener(this);

        btnBad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.base_well_btn:
                setResult(Constants.RESULT_WELL);
                finish();
                break;
            case R.id.base_bad_btn:
                setResult(Constants.RESULT_BAD);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (fragment instanceof KeyPressTestFragment)
        {
            ((KeyPressTestFragment) fragment).onKeyDown(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        finish();
//    }


//    @Override
//    protected void onUserLeaveHint()
//    {
//        super.onUserLeaveHint();
//        if (fragment instanceof KeyPressTestFragment)
//        {
//            Intent intent = new Intent(this,BaseCheckActivity.class);
//            intent.putExtra("position", 7);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            this.startActivity(intent);
//        }
//    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
//        if (receiver != null)
//        {
//            unregisterReceiver(receiver);
//            receiver = null;
//        }
    }
}
