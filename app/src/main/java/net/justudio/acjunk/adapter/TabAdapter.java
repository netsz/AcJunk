package net.justudio.acjunk.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.justudio.acjunk.Activity.AcFrag;

/**
 * Created by Administrator on 2015/12/30 0030.
 */
public class TabAdapter extends FragmentPagerAdapter {

    public static final String[] TITLE =  new String[]{"综合","工作·情感","动漫文化","漫画·轻小说","游戏"};

    public TabAdapter(FragmentManager fm){
        super(fm);
    }

    @Override

    public Fragment getItem(int position){
        System.out.println("Fragment position:" + position);
        switch (position) {
            case 0:
                break;
            default:
                break;

        }

        return new AcFrag(position);
    }

    @Override

    public CharSequence getPageTitle(int position) {
        return TITLE[position % TITLE.length].toUpperCase();
    }

    @Override

    public int getCount(){
        return TITLE.length;
    }

}