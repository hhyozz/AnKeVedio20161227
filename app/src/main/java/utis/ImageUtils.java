package utis;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.anke.vedio.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

public class ImageUtils {

    //	private final static String tag = "ImageUtils";
    private static DisplayImageOptions options;

    private static ImageLoader loader = ImageLoader.getInstance();

    public static void displayImage(String imageUrl, ImageView mImageView) {
        initOptions();
        displayImage(imageUrl, mImageView, null);
    }

    public static void displayImage(String imageUrl, ImageView imageView, SimpleImageLoadingListener listener) {
        if (imageUrl == null || imageView == null) return;
        initOptions();
        loader.displayImage(imageUrl, imageView, options, listener);
    }

    public static void displayImage(String imageUrl, ImageView imageView, int defaultImageRes, SimpleImageLoadingListener listener) {
        if (imageUrl == null || imageView == null) return;
        initOptions();
        final DisplayImageOptions tempOptions = new DisplayImageOptions.Builder()
                .cloneFrom(options)
                .showImageOnFail(defaultImageRes)
                .showImageForEmptyUri(defaultImageRes)
                .showImageOnLoading(defaultImageRes)
                .build();
        loader.displayImage(imageUrl, imageView, tempOptions, listener);
    }

    /** 显示服务器上的缩略图 */
    public static void displayServerThumb(String imageUrl, ImageView imageView) {
        displayImage(thumbUrl(imageUrl), imageView);
    }

    /** 显示服务器上的缩略图 */
    public static void displayServerThumb(String imageUrl, ImageView imageView, int defaultImageRes) {
        displayServerThumb(imageUrl, imageView, defaultImageRes, null);
    }

    /** 显示服务器上的缩略图 */
    public static void displayServerThumb(String imageUrl, ImageView imageView, @DrawableRes int defaultImageRes, SimpleImageLoadingListener listener) {
        displayImage(thumbUrl(imageUrl), imageView, defaultImageRes, listener);
    }

    private static String thumbUrl(String imageUrl) {
        // 按照约定，服务器上的缩略图为原图片地址（移除后缀） + _thumb+ 原后缀
        if(imageUrl != null && !imageUrl.isEmpty()) {
            int pointIndex = imageUrl.lastIndexOf(".");
            return imageUrl.substring(0, pointIndex) + "_thumb" + imageUrl.substring(pointIndex, imageUrl.length());
        }
        return null;
    }

    /** 显示本地图片 */
    public static void displayLocalImage(String path, ImageView imageView) {
        if(path == null || path.isEmpty() || imageView == null) return;
        displayImage("file://" + path, imageView);
    }

//    /** 显示本地图片 */
//    public static void displayLocalImage(String path, final ImageView imageView, int width, int height) {
//        if(path == null || path.isEmpty() || imageView == null) return;
//        ImageSize size = new ImageSize(width, height);
//        loader.displayImage("file://" + path, imageView, size);
//    }

    /** 显示资源图片 */
    public static void displayResourece(ImageView imageView, @DrawableRes int rid) {
        if(imageView == null) return;
        loader.cancelDisplayTask(imageView);
        imageView.setImageResource(rid);
    }

    public static void loadImage(String imageUrl, int width, int height,  SimpleImageLoadingListener listener) {
        if(imageUrl == null || imageUrl.isEmpty()) return;
        ImageSize size = new ImageSize(width, height, 0);
        loader.loadImage(imageUrl, size, listener);
    }

    public static File getImageDiskCache(String imageUrl) {
        return loader.getDiskCache().get(imageUrl);
    }

    private static void initOptions() {
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_empty)
                    .showImageForEmptyUri(R.mipmap.ic_empty)
                    .showImageOnFail(R.mipmap.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
        }
    }

}