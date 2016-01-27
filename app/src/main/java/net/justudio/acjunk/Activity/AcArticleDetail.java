package net.justudio.acjunk.Activity;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.justudio.acjunk.R;
import net.justudio.acjunk.adapter.AcDetailAdapter;
import net.justudio.acjunk.util.Constants;
import net.justudio.acjunk.util.HttpUtil;
import net.justudio.acjunk.util.JsoupUtil;

import me.maxwin.view.XListView;
import me.maxwin.view.IXListViewLoadMore;

/**
 * Ac文章页面活动
 */
public class AcArticleDetail extends Activity implements IXListViewLoadMore {
    private XListView listView;
    private AcDetailAdapter acDetailAdapter;

    private ProgressBar progressBar;
    private ImageView reLoadImageView;
    private ImageView backBtn;
    private ImageView commentBtn;

    public static String url;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        init();
        initComponent();

        new MainTask().execute(url, Constants.DEF_TASK_TYPE.FIRST);
    }

    private void init() {
        acDetailAdapter = new AcDetailAdapter(this);
        url=getIntent().getExtras().getString("acLink");
        filename =url.substring(url.lastIndexOf("/") + 1);
        System.out.println("filename~" + filename);
    }

    private void initComponent() {
        progressBar = (ProgressBar)findViewById(R.id.acContentPro);
        reLoadImageView = (ImageView)findViewById(R.id.reLoadImage);
        reLoadImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                reLoadImageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

            }
        });
        backBtn = (ImageView)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        commentBtn = (ImageView)findViewById(R.id.comment);

        listView =(XListView)findViewById(R.id.listView);
        listView.setAdapter(acDetailAdapter);
        listView.setPullLoadEnable(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int state=acDetailAdapter.getList().get(position-1).getState();
                switch (state) {
                    case Constants.DEF_AC_ITEM_TYPE.IMG:
                        String url = acDetailAdapter.getList().get(position-1).getImgLink();
                        Intent i = new Intent();
                        i.setClass(AcArticleDetail.this, ImageActivity.class);
                        i.putExtra("url", url);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public void finish(){
        super.finish();;
    }

    private class MainTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String...params){
            String temp = HttpUtil.httpGet(params[0]);
            if (temp==null) {
                if (params[1].equals(Constants.DEF_TASK_TYPE.FIRST)){
                    return Constants.DEF_RESOUT_CODE.FIRST;
                } else {
                    return Constants.DEF_RESOUT_CODE.ERROR;
                }
            }
            acDetailAdapter.addList(JsoupUtil.getContent(url,temp));
            if (params[1].equals(Constants.DEF_TASK_TYPE.FIRST)){
                return Constants.DEF_RESOUT_CODE.REFRESH;
            }
            return Constants.DEF_RESOUT_CODE.LOAD;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result==Constants.DEF_RESOUT_CODE.FIRST){
                Toast.makeText(getApplicationContext(), "网络不好╮(╯-╰)╭",Toast.LENGTH_LONG).show();
                reLoadImageView.setVisibility(View.VISIBLE);
            } else if (result == Constants.DEF_RESOUT_CODE.ERROR){
                listView.stopLoadMore();
            } else if (result==Constants.DEF_RESOUT_CODE.REFRESH){
                acDetailAdapter.notifyDataSetChanged();
            } else {
                acDetailAdapter.notifyDataSetChanged();
                listView.stopLoadMore();
            }
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
        }
    }

    @Override
    public void onLoadMore() {
        if(JsoupUtil.contentLastPage) {
            new MainTask().execute(url, Constants.DEF_TASK_TYPE.NOT_FIRST);

        } else {
            listView.stopLoadMore("-结束-");
        }
    }
}
