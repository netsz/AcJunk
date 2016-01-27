package net.justudio.acjunk.util;

/**
 * Created by Administrator on 2015/12/24 0024.
 */
public class URLUtil{
    public static String AC_URL_ZONGHE = "http://www.acfun.tv/v/list110/index";
    public static String AC_URL_JOB_LOVE = "http://www.acfun.tv/v/list73/index";
    public static String AC_URL_COMMIC = "http://www.acfun.tv/v/list74/index";
    public static String AC_URL_NOVEL = "http://www.acfun.tv/v/list75/index";
    public static String AC_URL_GAME = "http://www.acfun.tv/v/list164/index";

    /**
     **获取当前文章目录
     **/

    public static String getAcListUrl(int acType, String page) {
        String url="";
        switch(acType) {
            case Constants.DEF_ARTICLE_TYPE.ZONGHE:
                url=AC_URL_ZONGHE;
                break;
            case Constants.DEF_ARTICLE_TYPE.JBLOVE:
                url=AC_URL_JOB_LOVE;
                break;
            case Constants.DEF_ARTICLE_TYPE.COMMIC:
                url=AC_URL_COMMIC;
                break;
            case Constants.DEF_ARTICLE_TYPE.NOVEL:
                url=AC_URL_NOVEL;
                break;
            case Constants.DEF_ARTICLE_TYPE.GAME:
                url=AC_URL_GAME;
                break;
            default:
                break;
        }
        url = url + page +".htm";
        return url;

    }

    public static String getRefreshAcListUrl(int acType) {
        String url="";
        switch(acType) {
            case Constants.DEF_ARTICLE_TYPE.ZONGHE:
                url=AC_URL_ZONGHE;
                break;
            case Constants.DEF_ARTICLE_TYPE.JBLOVE:
                url=AC_URL_JOB_LOVE;
                break;
            case Constants.DEF_ARTICLE_TYPE.COMMIC:
                url=AC_URL_COMMIC;
                break;
            case Constants.DEF_ARTICLE_TYPE.NOVEL:
                url=AC_URL_NOVEL;
                break;
            case Constants.DEF_ARTICLE_TYPE.GAME:
                url=AC_URL_GAME;
                break;
            default:
                break;
        }
        url = url + "1.htm";
        return url;

    }

    /**
     **返回AC评论列表链接
     **/




}