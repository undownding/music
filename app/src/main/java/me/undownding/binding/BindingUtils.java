package me.undownding.binding;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by undownding on 16-6-11.
 */
public class BindingUtils {

    @android.databinding.BindingAdapter("image_url")
    public static void bindImage(SimpleDraweeView view, String url) {
        view.setAspectRatio(1.0F);
        if (url.startsWith("//")) {
            url = "http:" + url;
        }
        view.setImageURI(Uri.parse(url));
    }

}
