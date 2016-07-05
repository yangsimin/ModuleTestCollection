package com.example.administrator.myapplication.fragment;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.utils.Constants;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class KeyPressTestFragment extends BaseFragment
{
    private ImageButton btnUp, btnOk, btnDown, btnLeft;

    private boolean blUp, blOk, blDown, blLeft;

    private Button btnBad;

    @Override
    public int getLayout()
    {
        return R.layout.fragment_keypress_test;
    }

    @Override
    protected void initViews()
    {
        btnUp = (ImageButton) rootView.findViewById(R.id.btn_arrow_up);

        btnOk = (ImageButton) rootView.findViewById(R.id.btn_ok);

        btnDown = (ImageButton) rootView.findViewById(R.id.btn_arrow_down);

        btnLeft = (ImageButton) rootView.findViewById(R.id.btn_arrow_left);

        btnBad = (Button) rootView.findViewById(R.id.btn_bad);
    }

    @Override
    protected void initEvents()
    {
        btnBad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().setResult(Constants.RESULT_BAD);
                getActivity().finish();
            }
        });
    }

    public void onKeyDown(int keyCode, KeyEvent event)
    {
        ImageButton curBtn = null;
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_UP:
                curBtn = btnUp;
                blUp = true;
                break;
            case KeyEvent.KEYCODE_ENTER:
                curBtn = btnOk;
                blOk = true;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                curBtn = btnDown;
                blDown = true;
                break;
            case KeyEvent.KEYCODE_BACK :
                curBtn = btnLeft;
                blLeft = true;
                break;
        }
        if (curBtn != null)
        {
            curBtn.setColorFilter(Color.parseColor("#238E23"));
        }

        if (blUp & blOk & blDown & blLeft)
        {
            getActivity().setResult(Constants.RESULT_WELL);
            getActivity().finish();
        }
    }
}
