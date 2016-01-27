package net.justudio.acjunk.model;

/**
 * Created by Administrator on 2015/12/24 0024.
 */
public class AcItem {

    private String title;
    private String name;
    private String link;
    private String id;
    private String date;
    private String commentNum;
    private String content;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentnum) {
        this.commentNum = commentnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
