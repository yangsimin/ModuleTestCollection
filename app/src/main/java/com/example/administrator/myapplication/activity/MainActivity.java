package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.GridViewAdapter;
import com.example.administrator.myapplication.model.PMCCheck;
import com.example.administrator.myapplication.utils.Contants;
import com.example.administrator.myapplication.utils.PMCCheckUtil;
import com.example.administrator.myapplication.widget.MyGridView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private MyGridView gvMain;

    private GridViewAdapter gridViewAdapter;

    private int index;

    private boolean beginAll;

    private List<Integer> enableItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gvMain = (MyGridView) findViewById(R.id.main_gv);

        testFilter();

        gridViewAdapter = new GridViewAdapter(this, Contants.ITEMS, enableItem, new GridViewAdapter.OnClickListener()
        {
            @Override
            public void onClick(int position, View v)
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
        gvMain.setAdapter(gridViewAdapter);
    }

    private void testFilter()
    {
        enableItem = new ArrayList<>();
        try
        {
            List<PMCCheck.Result> results = PMCCheckUtil.newInstance(this).getResults();
            for (int i = 0; i < results.size(); i++)
            {
                String name = results.get(i).getItemName();
                for (int j = 0; j < Contants.ITEMS.length; j++)
                {
                    if (Contants.ITEMS[j].equals(name)) enableItem.add(j);
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
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
