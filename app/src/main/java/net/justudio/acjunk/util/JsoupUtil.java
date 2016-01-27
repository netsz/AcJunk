package net.justudio.acjunk.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.select.Elements;
import net.justudio.acjunk.model.AcArticle;
import net.justudio.acjunk.model.AcItem;
import net.justudio.acjunk.model.Comment;

import org.jsoup.Jsoup;
import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/23 0023.
 */
public class JsoupUtil {

    private static final String AC_MAIN_URL = "http://www.acfun.tv";
    public static boolean contentFirstPage = true; // 第一页
    public static boolean contentLastPage = true; // 最后一页
    public static boolean multiPages = false; // 多页


    public static void resetPages() {
        contentFirstPage = true;
        contentLastPage = true;
        multiPages = false;
    }


    public static List<AcItem> getAcItemList(int acType, String str) {
        List<AcItem> list = new ArrayList<AcItem>();
        Document doc = Jsoup.parse(str);
        Element acMain = doc.getElementById("block-content-article");
        Elements acList=acMain.getElementsByClass("item");

        for (Element acItem : acList) {

            AcItem item = new AcItem();
            String title = acItem.select("a.title").text();
            Log.e("title",title);
            String description = acItem.select("div.desc").text();
            Log.e("description", description);
            String commentNum = acItem.select("a.hint-comm-article").text();
            Log.e("commentNum", commentNum);
            String name = acItem.select("p.article-info").select("a").text();
            Log.e("name", name);
            String date = acItem.select("a.hint-comm-article").attr("title");
            Log.e("date", date);
            String link = AC_MAIN_URL + acItem.select("a.hint-comm-article").attr("href");
            Log.e("link",link);



            item.setTitle(title);
            item.setContent(description);
            item.setCommentNum(commentNum);
            item.setName(name);
            item.setDate(date);
            item.setLink(link);
            item.setType(acType);

            list.add(item);


        }

        return list;
    }


    /**
     * 内容页
     **/


    public static List<AcArticle> getContent(String url, String str) {

        List<AcArticle> list = new ArrayList<AcArticle>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .timeout(30000)
                    .get();
            doc.select("script").remove();



            Element title = doc.select("span.txt-title-view_1").get(0);


            AcArticle acArticleTitle = new AcArticle();

            acArticleTitle.setState(Constants.DEF_AC_ITEM_TYPE.TITLE);
            acArticleTitle.setTitle(ToDBC(title.text()));


            Element content = doc.getElementById("area-player");
            content.getElementById("noflash-alert").remove();

            // 获取所有标签为<a的元素
            Elements as = content.getElementsByTag("a");
            for (int b = 0; b < as.size(); b++) {
                Element blockquote = as.get(b);
                // 改变这个元素的标记。例如,<span>转换为<div> 如el.tagName("div");。
                blockquote.tagName("bold"); // 转为粗体
            }

            Elements ss = content.getElementsByTag("strong");
            for (int b = 0; b < ss.size(); b++) {
                Element blockquote = ss.get(b);
                blockquote.tagName("bold");
            }

            // 获取所有标签为<p的元素
            Elements ps = content.getElementsByTag("p");
            for (int b = 0; b < ps.size(); b++) {
                Element blockquote = ps.get(b);
                blockquote.tagName("body");
            }

            // 获取所有引用元素
            Elements blockquotes = content.getElementsByTag("blockquote");
            for (int b = 0; b < blockquotes.size(); b++) {
                Element blockquote = blockquotes.get(b);
                blockquote.tagName("body");
            }

            // 获取所有标签为<ul的元素
            Elements uls = content.getElementsByTag("ul");
            for (int b = 0; b < uls.size(); b++) {
                Element blockquote = uls.get(b);
                blockquote.tagName("body");
            }

            // 找出粗体
            Elements bs = content.getElementsByTag("b");
            for (int b = 0; b < bs.size(); b++) {
                Element bold = bs.get(b);
                bold.tagName("bold");
            }

            for (int j = 0; j < content.children().size(); j++) {
                Element c = content.child(j);


                if (c.select("img").size() > 0) {
                    Elements imgs = c.getElementsByTag("img");
                    System.out.println("img");
                    for (Element img : imgs) {
                        if (!img.attr("src").equals("")) {
                            AcArticle acImgs = new AcArticle();

                            if (!img.parent().attr("href").equals("")) {
                                acImgs.setImgLink(img.parent().attr("href"));
                                System.out.println("href="
                                        + img.parent().attr("href"));

                                if (img.parent().parent().tagName().equals("p")) {

                                }
                                img.parent().remove();
                            }

                            acImgs.setContent(img.attr("src"));
                            acImgs.setImgLink(img.attr("src"));
                            System.out.println(acImgs.getContent());
                            acImgs.setState(Constants.DEF_AC_ITEM_TYPE.IMG);
                            list.add(acImgs);
                        }
                    }
                }
                c.select("img").remove();

                //获取文章内容

                AcArticle acArticle = new AcArticle();
                acArticle.setState(Constants.DEF_AC_ITEM_TYPE.CONTENT);

                if (c.text().equals("")) {
                    continue;
                } else if (c.children().size() == 1) {
                    if (c.child(0).tagName().equals("bold")
                            || c.child(0).tagName().equals("span")) {
                        if (c.ownText().equals("")) {
                            // 小标题，咖啡色
                            acArticle
                                    .setState(Constants.DEF_AC_ITEM_TYPE.BOLD_TITLE);
                        }
                    }
                }


                // 代码
                if (c.select("pre").attr("name").equals("code")) {
                    acArticle.setState(Constants.DEF_AC_ITEM_TYPE.CODE);
                    acArticle.setContent(ToDBC(c.outerHtml()));
                } else {
                    acArticle.setContent(ToDBC(c.outerHtml()));
                }
                list.add(acArticle);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


            return list;

    }

        /**
         * 获取AC评论列表
         *
         */

//        public static List<Comment> getAcCommentList(String str, int pageIndex, int pageSize) {
//
//            List<Comment> list = new ArrayList<Comment>();
//
//            try {
//                //创建一个json对象
//                JSONObject jsonObject = new JSONObject(str);
//                JSONArray jsonArray = jsonObject.getJSONArray("list");
//                int index = 0;
//                int len = jsonArray.length();
//                AcCommentActivity.commentCount = String.valueOf(len);
//
//                if (len > 20) {
//                    index = (pageSize * pageIndex - 20);
//                }
//                if (len < pageSize && pageIndex > 1) {
//                    return list;
//                }
//
//                if ((pageSize * pageIndex) < len) {
//                    len = pageSize * pageIndex;
//                }
//
//                for (int i = index; i < len; i++) {
//                    JSONObject item = jsonArray.getJSONObject(i);
//                    String commentId = item.getString("index-comment");
//                    String username = item.getString("name");
//                    String content = item.getString("content-comment");
//                    String postTime = item.getString("time_");
//                    String parentId = item.getString("item-comment");
//                    String userface = item.getString("avatar");
//
//                    Comment comment = new Comment();
//                    comment.setCommentId(commentId);
//                    comment.setUsername(username);
//                    comment.setContent(content);
//                    comment.setPostTime(postTime);
//                    comment.setParentId(parentId);
//                    comment.setUserface(userface);
//
//                    if (parentId.equals("0")) {
//                        comment.setType(Constants.DEF_COMMENT_TYPE.PARENT);
//                    } else {
//                        comment.setType(Constants.DEF_COMMENT_TYPE.CHILD);
//                    }
//                    list.add(comment);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//            }
//
//            return list;
//        }
    //半角转全角
    public static String ToDBC(String input) {

        char[] c = input.toCharArray();
        for (int i=0;i<c.length;i++){
            if(c[i]==12288){
                c[i] =(char) 32;
                continue;
            }
            if(c[i]>65280&c[i]<65375)
                c[i]=(char)(c[i]-65248);
            }


        return new String(c);
    }
}