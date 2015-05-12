import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by bitcores on 2015-04-23.
 */

public class BitmapAdapter {
    private static LruCache<String, Bitmap> mMemoryCache = null;

    public static final int TYPE_URL = 0;
    public static final int TYPE_FILE = 1;
    public static final int METHOD_GET = 2;
    public static final int METHOD_SET = 3;

    public BitmapAdapter() {

    }

    public interface BitmapResponse {
        void bitmapReturn(Bitmap bitmap);
    }

    public void initBitmapCache() {
        if (mMemoryCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;

            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    public void getBitmap(String path, Integer type, Integer method, ImageView imageView, BitmapResponse response) {
        String[] params = new String[1];
        BitmapLoader task = new BitmapLoader(path, type, method, imageView, response);
        task.execute(params);
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private class BitmapLoader extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final BitmapResponse returnTarget;
        private final Integer type;
        private final Integer method;
        private final String key;

        public BitmapLoader(String path, Integer requestType, Integer requestMethod, ImageView imageView, BitmapResponse target) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            returnTarget = target;
            type = requestType;
            method = requestMethod;
            key = path;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = getBitmapFromMemCache(key);

            if (bitmap == null) {
                if (type == TYPE_URL) {
                    String baseUrl = "https://maps.googleapis.com/maps/api/streetview?";

                    try {
                        URL requestUrl = new URL(baseUrl + key);
                        InputStream content = (InputStream) requestUrl.getContent();
                        bitmap = BitmapFactory.decodeStream(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (type == TYPE_FILE) {
                    bitmap = BitmapFactory.decodeFile(key);
                }
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && key != null) {
                addBitmapToMemoryCache(key, bitmap);

                if (method == METHOD_SET) {
                    if (imageViewReference != null) {
                        final ImageView imageView = imageViewReference.get();
                        if (imageView != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                } else if (method == METHOD_GET) {
                    if (returnTarget != null) {
                        returnTarget.bitmapReturn(bitmap);
                    }
                }
            }
        }

    }

}

