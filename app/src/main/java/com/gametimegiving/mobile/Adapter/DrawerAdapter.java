package com.gametimegiving.mobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gametimegiving.mobile.R;


public class DrawerAdapter extends BaseAdapter {
    public int imageArra[] = {R.drawable.homes, R.drawable.teams,
            R.drawable.homes, R.drawable.user, R.drawable.user, R.drawable.aboutus, R.drawable.setting, R.drawable.homes};
    Context context;
    String[] datas;
    LayoutInflater layoutInflater;

    public DrawerAdapter(Context context, String[] data) {
        this.context = context;
        this.datas = data;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public Object getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.drawer_adapter, null);
            viewHolder.name =
                    (TextView) convertView.findViewById(R.id.menu_item_name);
            viewHolder.img_menu =
                    (ImageView) convertView.findViewById(R.id.menu_item_image);

            convertView.setTag(viewHolder);

        } else {
            viewHolder =
                    (ViewHolder) convertView.getTag();


        }


        viewHolder.name.setText(datas[position]);

        viewHolder.img_menu.setBackgroundResource(imageArra[position]);


        return convertView;
    }


    private static class ViewHolder {
        TextView name;
        ImageView img_menu;

    }

}
