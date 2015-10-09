package com.sch.dragdelete;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置连接线的颜色
//        ((DemoTextView)findViewById(R.id.text)).setConnectedColor(Color.GREEN);

        datas = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            datas.add("item" + i);
        }

        ((ListView) findViewById(R.id.list)).setAdapter(new MyAdapter(this));

    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                holder.textView = (TextView) convertView.findViewById(R.id.title);
                holder.ddTextView = (DragDeleteTextView) convertView.findViewById(R.id.ddtv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(datas.get(position));
            holder.ddTextView.setText(String.valueOf(position));
            return convertView;
        }

        class ViewHolder {
            TextView textView;
            DragDeleteTextView ddTextView;
        }

    }

}
