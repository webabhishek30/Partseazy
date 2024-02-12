package com.partseazy.android.network.manager;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.partseazy.android.network.caching.BitmapCacheManager;

/**
 * Created by Pumpkin Guy on 05/12/16.
 */

public class ImageManager {

    private static ImageManager instance;

    private ImageLoader imageLoader;
    private RequestQueue queue;

    private ImageManager(Context context) {
        this.queue = Volley.newRequestQueue(context);

        imageLoader = new ImageLoader(queue, new BitmapCacheManager());
        this.queue.start();
    }

    public static ImageManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null) {
                    instance = new ImageManager(context);
                }
            }
        }
        return instance;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public RequestQueue getQueue() {
        return queue;
    }

}

