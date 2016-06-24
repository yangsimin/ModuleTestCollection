package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.GridViewAdapter;
import com.example.administrator.myapplication.utils.Contants;

public class MainActivity extends AppCompatActivity
{

    private GridView gvMain;

    private GridViewAdapter gridViewAdapter;

    private int index;

    private boolean beginAll;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gvMain = (GridView) findViewById(R.id.main_gv);

        gridViewAdapter = new GridViewAdapter(this, Contants.ITEMS);
        gvMain.setAdapter(gridViewAdapter);

        //设置按键监听
        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == Contants.ITEMS.length - 1)
                {
                    beginAll = true;
                    jumpToBaseCheckActivity(0);
                } else
                {
                    jumpToBaseCheckActivity(position);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        TextView tvBtn = (TextView) gvMain.getChildAt(requestCode);
        if (tvBtn == null) return;
        switch (resultCode)
        {
            case Contants.RESULT_WELL:
                tvBtn.setTextColor(Color.parseColor("#238E23"));
                break;
            case Contants.RESULT_BAD:
                tvBtn.setTextColor(Color.parseColor("#FF2400"));
                break;
        }

        if (beginAll & index < Contants.ITEMS.length - 2)
        {
            jumpToBaseCheckActivity(++index);
            if (index == Contants.ITEMS.length - 2)
            {
                beginAll = false;
                index = 0;
            }
        }
    }

    private void jumpToBaseCheckActivity(int position)
    {
        Intent intent = new Intent(MainActivity.this, BaseCheckActivity.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, position);
    }
}
