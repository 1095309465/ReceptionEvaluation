package com.jhzy.receptionevaluation.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by nakisaRen
 * on 17/2/23.
 */

public class ImageLoaderUtils {

    public static void load(SimpleDraweeView imageview, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        url = url + "?x-oss-process=image/resize,m_lfit,h_300,w_300";
        Uri uri = Uri.parse(url);
        imageview.setImageURI(uri);
    }
    public static void loadLocalImage(SimpleDraweeView imageview, Uri uri) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(300, 200))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(imageview.getController())
                .setImageRequest(request)
                .build();
        imageview.setController(controller);
    }
}
