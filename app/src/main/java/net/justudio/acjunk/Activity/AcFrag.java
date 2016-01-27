package net.justudio.acjunk.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import net.justudio.acjunk.R;
import net.justudio.acjunk.adapter.AcListAdapter;
import net.justudio.acjunk.model.AcItem;
import net.justudio.acjunk.model.Page;
import net.justudio.acjunk.util.Constants;
import net.justudio.acjunk.util.DB;
import net.justudio.acjunk.util.HttpUtil;
import net.justudio.acjunk.util.JsoupUtil;
import net.justudio.acjunk.util.URLUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2015/12/30 0030.
 */
public class AcFrag extends Fragment implements IXListViewRefreshListener,
        IXListViewLoadMore {
    private XListView acListView;// 博客列表
    private View noAcView; // 无数据时显示
    private AcListAdapter adapter;// 列表适配器

    private boolean isLoad = false; // 是否加载
    private int acType = 0; // 博客类别
    private Page page; // 页面引用

    private DB db; // 数据库引用
    private String refreshDate = ""; // 刷新日期

    public AcFrag(int acType) {
        this.acType = acType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initComponent();
        if (isLoad == false) {
            isLoad = true;
            refreshDate = getDate();
            acListView.setRefreshTime(refreshDate);
            // 加载数据库中的数据
            List<AcItem> list = db.query(acType);
            adapter.setList(list);
            adapter.notifyDataSetChanged();

            acListView.startRefresh(); // 开始刷新

        } else {
            acListView.NotRefreshAtBegin(); // 不开始刷新
        }
        Log.e("NewsFrag", "onActivityCreate");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("NewsFrag", "onCreateView");
        return inflater.inflate(R.layout.activity_main, null);
    }

    // 初始化
    private void init() {
        db = new DB(getActivity());
        adapter = new AcListAdapter(getActivity());
        page = new Page();
        page.setPageStart();
    }

    // 初始化组件
    private void initComponent() {
        acListView = (XListView) getView().findViewById(R.id.acListView);
        acListView.setAdapter(adapter);// 设置适配器
        acListView.setPullRefreshEnable(this);// 设置可下拉刷新
        acListView.setPullLoadEnable(this);// 设置可上拉加载
        // 设置列表项点击事件
        acListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 获得博客列表项
                AcItem item = (AcItem) adapter.getItem(position - 1);
                Intent i = new Intent();
                i.setClass(getActivity(), AcArticleDetail.class);
                i.putExtra("acLink", item.getLink());
                startActivity(i);
                // 动画过渡
                getActivity().overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_no);
                Log.e("position", "" + position);
            }
        });

        noAcView = getView().findViewById(R.id.noAcLayout);
    }

    private class MainTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            // 获取网页html数据
            String temp = HttpUtil.httpGet(params[0]);
            if (temp == null) {
                return Constants.DEF_RESOUT_CODE.ERROR;
            }
            // 解析html页面获取列表
            List<AcItem> list = JsoupUtil.getAcItemList(acType, temp);
            if (list.size() == 0) {
                return Constants.DEF_RESOUT_CODE.NO_DATA;
            }
            // 刷新动作
            if (params[1].equals("refresh")) {
                adapter.setList(list);
                return Constants.DEF_RESOUT_CODE.REFRESH;
            } else {// 加载更多
                adapter.addList(list);
                return Constants.DEF_RESOUT_CODE.LOAD;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            // 通知列表数据更新
            adapter.notifyDataSetChanged();
            switch (result) {
                case Constants.DEF_RESOUT_CODE.ERROR: // 错误
                    Toast.makeText(getActivity(), "网络信号不佳", Toast.LENGTH_LONG);
                    acListView.stopRefresh(getDate());
                    acListView.stopLoadMore();
                    break;
                case Constants.DEF_RESOUT_CODE.NO_DATA: // 无数据
                    // Toast.makeText(getActivity(), "无更多加载内容", Toast.LENGTH_LONG)
                    // .show();
                    acListView.stopLoadMore();
                    // noAcView.setVisibility(View.VISIBLE); // 显示无博客
                    break;
                case Constants.DEF_RESOUT_CODE.REFRESH: // 刷新
                    acListView.stopRefresh(getDate());

                    db.delete(acType);
                    db.insert(adapter.getList());// 保存到数据库
                    if (adapter.getCount() == 0) {
                        noAcView.setVisibility(View.VISIBLE); // 显示无博客
                    }
                    break;
                case Constants.DEF_RESOUT_CODE.LOAD:
                    acListView.stopLoadMore();
                    page.addPage();
                    if (adapter.getCount() == 0) {
                        noAcView.setVisibility(View.VISIBLE); // 显示无博客
                    }
                    break;
                default:
                    break;
            }
            super.onPostExecute(result);
        }

    }

    // 加载更多时调用
    @Override
    public void onLoadMore() {
        System.out.println("loadmore");
        new MainTask()
                .execute(
                        URLUtil.getAcListUrl(acType, page.getCurrentPage()),
                        "load");
    }

    @Override
    public void onRefresh() {
        System.out.println("refresh");
        page.setPageStart();
        new MainTask().execute(URLUtil.getRefreshAcListUrl(acType),
                "refresh");
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm",
                Locale.CHINA);
        return sdf.format(new java.util.Date());
    }

}
