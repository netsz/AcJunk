package net.justudio.acjunk.Activity;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.viewpagerindicator.PageIndicator;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import net.justudio.acjunk.R;
import net.justudio.acjunk.adapter.TabAdapter;

/**
 * Created by Administrator on 2015/12/30 0030.
 */
public class MainTabActivity extends SlidingFragmentActivity {

    @Override

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab);

        FragmentPagerAdapter adapter = new TabAdapter(getSupportFragmentManager());

        //视图切换器

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(adapter);

        //页面指示器

        PageIndicator indicator = (PageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        initSlidingMenu(savedInstanceState);

    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        // 设置右侧滑动菜单
        setBehindContentView(R.layout.menu_frame_right);
        // 实例化滑动菜单对象
        SlidingMenu sm = getSlidingMenu();
        // 实例化滑动菜单对象
        sm = getSlidingMenu();
        // 设置可以左右滑动菜单
        sm.setMode(SlidingMenu.LEFT);
        // 设置滑动阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单阴影的图像资源
        sm.setShadowDrawable(null);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        sm.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // 设置下方视图的在滚动时的缩放比例
        sm.setBehindScrollScale(0.0f);
        sm.setBackgroundResource(R.drawable.biz_news_local_weather_bg_big);

    }

    @Override

    public void onBackPressed() {

        Builder dialog=new AlertDialog.Builder(MainTabActivity.this)
                .setMessage("要离开我么？")
                .setPositiveButton("是的", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface arg0, int arg1){
                        finish();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){

                    }
                });

        dialog.show();
    }

}