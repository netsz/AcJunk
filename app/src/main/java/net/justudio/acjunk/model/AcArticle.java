package net.justudio.acjunk.model;

/**
 * Created by Administrator on 2015/12/25 0025.
 */
public class AcArticle {

    private String title;
    private String content;
    private String acImgs;
    private String imgLink;
    private String summary;
    private String link;
    private String commentCount;
    private int state;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAcImgs() {
        return acImgs;
    }

    public void setAcImgs(String acImgs) {
        this.acImgs = acImgs;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
