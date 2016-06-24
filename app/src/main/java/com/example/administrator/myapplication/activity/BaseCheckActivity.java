package com.example.administrator.myapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.fragment.BatteryTestFragment;
import com.example.administrator.myapplication.fragment.BlueToothTestFragment;
import com.example.administrator.myapplication.fragment.CheckTimeFragment;
import com.example.administrator.myapplication.fragment.GPRSTestFragment;
import com.example.administrator.myapplication.fragment.GPSTestFragment;
import com.example.administrator.myapplication.fragment.KeyPressTestFragment;
import com.example.administrator.myapplication.fragment.OnlyTextViewFragment;
import com.example.administrator.myapplication.fragment.SDCardTestFragment;
import com.example.administrator.myapplication.fragment.SleepTestFragment;
import com.example.administrator.myapplication.fragment.SoundTestFragment;
import com.example.administrator.myapplication.fragment.VibratorTestFragment;
import com.example.administrator.myapplication.utils.Contants;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(Contants.FLAG_HOMEKEY_DISPATCHED, Contants.FLAG_HOMEKEY_DISPATCHED);
//        getWindow().addFlags(Contants.FLAG_HOMEKEY_DISPATCHED);
        setContentView(R.layout.activity_base_check);

        //广播
//        receiver = new MonitoHomeReceiver();
//        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        registerReceiver(receiver, homeFilter);

        initBtn();

        int position = getIntent().getIntExtra("position", 0);
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
                setResult(Contants.RESULT_WELL);
                finish();
                break;
            case R.id.base_bad_btn:
                setResult(Contants.RESULT_BAD);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (fragment instanceof KeyPressTestFragment)
        {
            ((KeyPressTestFragment)fragment).onKeyDown(keyCode, event);
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
