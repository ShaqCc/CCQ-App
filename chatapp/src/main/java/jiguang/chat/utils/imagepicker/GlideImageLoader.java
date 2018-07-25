package jiguang.chat.utils.imagepicker;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import jiguang.chat.R;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImages(Activity activity, String path, ImageView imageView, int width, int height) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.mipmap.default_image)
                .placeholder(R.mipmap.black)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .apply(requestOptions)        //设置错误图片
                .into(imageView);
    }


    @Override
    public void clearMemoryCache() {
    }
}
