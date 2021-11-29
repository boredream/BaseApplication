package com.boredream.baseapplication.net;

import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.boredream.baseapplication.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class GlideHelper {

    public static void loadImage(ImageView iv, String model) {
        int defaultImg = R.drawable.rect_gray;

        RequestOptions options = new RequestOptions()
                .transform(new CenterCrop())
                .placeholder(defaultImg)
                .error(defaultImg);

        Glide.with(iv.getContext())
                .load(model)
                .apply(options)
                .into(iv);
    }

    public static void loadRoundedImg(ImageView iv, String model) {
        loadRoundedImg(iv, model, 4);
    }

    public static void loadRoundedImg(ImageView iv, String model, int corner) {
        int defaultImg = R.drawable.rect_gray;

        RoundedCorners corners = new RoundedCorners(SizeUtils.dp2px(corner));
        RequestOptions options = new RequestOptions()
                .transform(new MultiTransformation<>(new CenterCrop(), corners))
                .placeholder(defaultImg)
                .error(defaultImg);

        Glide.with(iv.getContext())
                .load(model)
                .apply(options)
                .into(iv);
    }

    public static void loadOvalImg(ImageView iv, String model) {
        int defaultImg = R.drawable.rect_gray;

        CircleCrop circleCrop = new CircleCrop();
        RequestOptions options = new RequestOptions()
                .transform(new MultiTransformation<>(new CenterCrop(), circleCrop))
                .placeholder(defaultImg)
                .error(defaultImg);

        Glide.with(iv.getContext())
                .load(model)
                .apply(options)
                .into(iv);
    }
}
