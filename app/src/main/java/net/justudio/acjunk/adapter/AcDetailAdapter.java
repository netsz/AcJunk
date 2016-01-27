package net.justudio.acjunk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.justudio.acjunk.R;
import net.justudio.acjunk.model.AcArticle;
import net.justudio.acjunk.util.Constants;
import net.justudio.acjunk.util.FileUtil;
import net.justudio.acjunk.util.MyTagHandler;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


import java.util.ArrayList;
import java.util.List;

/**
 * Ac文章内容页Adapter
 */
public class AcDetailAdapter extends BaseAdapter{

    private ViewHolder holder;
    private LayoutInflater layoutInflater;
    private Context context;
    private List<AcArticle> list;

    private SpannableStringBuilder htmlSpannable;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public AcDetailAdapter(Context context) {
        super();
        this.context = context;
        layoutInflater = layoutInflater.from(context);
        list = new ArrayList<AcArticle>();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.images)
                .showImageForEmptyUri(R.drawable.images)
                .showImageOnFail(R.drawable.images).cacheInMemory()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }

    public void setList(List<AcArticle> list) {
        this.list = list;
    }

    public void addList(List<AcArticle> list) {
        this.list.addAll(list);
    }

    public void clearList(List<AcArticle> list){
        this.list.clear();
    }

    public List<AcArticle> getList(){
        return list;
    }

    public void removeItem(int position) {
        if(list.size()>0){
            list.remove((position));
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
        AcArticle article = list.get(position);
        if(convertView == null) {
            holder = new ViewHolder();
            switch (article.getState()){
                case Constants.DEF_AC_ITEM_TYPE.TITLE:
                    convertView = layoutInflater.inflate(
                            R.layout.ac_detail_title, null);
                    holder.content=(TextView)convertView.findViewById(R.id.text);
                    break;
                case Constants.DEF_AC_ITEM_TYPE.SUMMARY:
                    convertView = layoutInflater.inflate(
                            R.layout.ac_detail_summary, null);

                    holder.content=(TextView)convertView.findViewById(R.id.text);
                    break;
                case Constants.DEF_AC_ITEM_TYPE.CONTENT:
                    convertView = layoutInflater.inflate(R.layout.ac_detail_text,null);
                    holder.content=(TextView)convertView.findViewById(R.id.text);
                    break;
                case Constants.DEF_AC_ITEM_TYPE.IMG:
                    convertView=layoutInflater.inflate(R.layout.ac_detail_img,null);
                    holder.image=(ImageView)convertView.findViewById(R.id.imageView);
                    break;
                case Constants.DEF_AC_ITEM_TYPE.BOLD_TITLE: // 加粗标题
                    convertView = layoutInflater.inflate(R.layout.ac_detail_bold_title, null);
                    holder.content = (TextView) convertView.findViewById(R.id.text);
                    break;
                case Constants.DEF_AC_ITEM_TYPE.CODE: // 代码
                    convertView = layoutInflater.inflate(R.layout.ac_detail_code, null);
                    holder.code = (WebView) convertView.findViewById(R.id.code_view);
                    break;

            }
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (article!=null){
            switch (article.getState()) {
                case Constants.DEF_AC_ITEM_TYPE.IMG:
                    imageLoader.displayImage(article.getContent(),holder.image,options);
                    break;

                case Constants.DEF_AC_ITEM_TYPE.CODE: // 代码，格式显示

                    // 读取代码文件和模板文件
                    String code = article.getContent();
                    // String code = FileUtil.getFileContent(context,
                    // "AboutActivity.java");
                    String template = FileUtil.getFileContent(context, "code.html");
                    // 生成结果
                    String html = template.replace("{{code}}", code);
                    holder.code.getSettings().setDefaultTextEncodingName("utf-8");
                    holder.code.getSettings().setSupportZoom(true);
                    holder.code.getSettings().setBuiltInZoomControls(true);

                    // holder.code.loadUrl("file:///android_asset/code.html");

                    holder.code.loadDataWithBaseURL("file:///android_asset/", html,
                            "text/html", "utf-8", null);

                    break;


                default:

                    holder.content.setText(Html.fromHtml(article.getContent(), null, new MyTagHandler()));

                    break;
            }
        }
        return convertView;
    }

    @Override

    public int getViewTypeCount(){
        return 6;
    }

    @Override

    public int getItemViewType(int position){
        switch (list.get(position).getState()){
            case Constants.DEF_AC_ITEM_TYPE.TITLE:
                return 0;
            case Constants.DEF_AC_ITEM_TYPE.SUMMARY:
                return 1;
            case Constants.DEF_AC_ITEM_TYPE.CONTENT:
                return 2;
            case Constants.DEF_AC_ITEM_TYPE.IMG:
                return 3;
            case Constants.DEF_AC_ITEM_TYPE.BOLD_TITLE:
                return 4;
            case Constants.DEF_AC_ITEM_TYPE.CODE:
                return 5;
        }
        return 1;
    }

    @Override
    public boolean isEnabled(int position) {
        switch (list.get(position).getState()){
            case Constants.DEF_AC_ITEM_TYPE.IMG:
                return true;
            default:
                return false;
        }
    }

    private class ViewHolder{
        TextView id;
        TextView date;
        TextView title;
        TextView content;
        ImageView image;
        WebView code;

    }
}
