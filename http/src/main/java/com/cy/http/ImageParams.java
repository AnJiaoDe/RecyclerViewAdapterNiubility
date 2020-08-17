package com.cy.http;

import android.widget.ImageView;

public class ImageParams {
    private String url;
    private String cachePath;
    private String cachePathDefault;
    private boolean cache = true;
    private int reqWidth;
    private int reqHeight;
    private ImageView imageView;


//    private ImageParams(Builder builder) {
//        this.cachePath = builder.getCachePath();
//        this.cachePathDefault = builder.getCachePathDefault();
//        this.cache = builder.isCache();
//        this.reqWidth = builder.getReqWidth();
//        this.reqHeight = builder.getReqHeight();
//    }

//    public static class Builder {
//        private String cachePath;
//        private String cachePathDefault;
//        private boolean cache = true;
//        private int reqWidth;
//        private int reqHeight;
//
//        public String getCachePath() {
//            return cachePath;
//        }
//
//        public Builder setCachePath(String cachePath) {
//            this.cachePath = cachePath;
//            return this;
//        }
//
//        public String getCachePathDefault() {
//            return cachePathDefault;
//        }
//
//        public Builder setCachePathDefault(String cachePathDefault) {
//            this.cachePathDefault = cachePathDefault;
//            return this;
//
//        }
//
//        public boolean isCache() {
//            return cache;
//        }
//
//        public Builder setCache(boolean cache) {
//            this.cache = cache;
//            return this;
//
//        }
//
//        public int getReqWidth() {
//            return reqWidth;
//        }
//
//        public Builder setReqWidth(int reqWidth) {
//            this.reqWidth = reqWidth;
//            return this;
//
//        }
//
//        public int getReqHeight() {
//            return reqHeight;
//        }
//
//        public Builder setReqHeight(int reqHeight) {
//            this.reqHeight = reqHeight;
//            return this;
//        }
//
//        public ImageParams build() {
//            return new ImageParams(this);
//        }
//    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public String getCachePathDefault() {
        return cachePathDefault;
    }

    public void setCachePathDefault(String cachePathDefault) {
        this.cachePathDefault = cachePathDefault;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public int getReqWidth() {
        return reqWidth;
    }

    public void setReqWidth(int reqWidth) {
        this.reqWidth = reqWidth;
    }

    public int getReqHeight() {
        return reqHeight;
    }

    public void setReqHeight(int reqHeight) {
        this.reqHeight = reqHeight;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
