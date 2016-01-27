package net.justudio.acjunk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.justudio.acjunk.R;
import net.justudio.acjunk.model.AcItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/29 0029.
 */
public class AcListAdapter extends BaseAdapter {
    private ViewHolder holder;
    private LayoutInflater layoutInflater;
    private Context context;
    private List<AcItem> list;

    public AcListAdapter(Context context) {
        super();
        this.context=context;
        layoutInflater = LayoutInflater.from(context);
        list=new ArrayList<AcItem>();
    }

    public void setList(List<AcItem> list) {
        this.list = list;
    }

    public void addList(List<AcItem> list) {
        this.list.addAll(list);
    }

    public void clearList() {
        this.list.clear();
    }

    public List<AcItem> getList() {
        return list;
    }

    public void removeItem(int position) {
        if (list.size() > 0) {
            list.remove(position);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.commentNum = (TextView) convertView.findViewById(R.id.comment_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        AcItem item = list.get(position);

        if (item!=null) {
            holder.title.setText(item.getTitle());
            holder.content.setText(item.getContent());
            holder.date.setText(item.getDate());
            holder.commentNum.setText(item.getCommentNum());

        }
        return convertView;
    }
    private class ViewHolder {
        TextView id;
        TextView date;
        TextView title;
        TextView commentNum;
        TextView content;
    }
}