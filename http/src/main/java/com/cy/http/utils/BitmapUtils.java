package com.cy.http.utils;//package com.cy.sdkstrategy_master.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lenovo on 2017/12/24.
 */

public class BitmapUtils {


    /**
     * 不压缩，传入压缩过的bitampa
     *
     * @param bitmap
     * @return
     */
    public static File saveBitmapToFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //    /**
//     * 根据路径 创建文件
//     *
//     * @param pathFile
//     * @return
//     * @throws IOException
//     */
    public static File createFile(String pathFile) throws IOException {
        File fileDir = new File(pathFile.substring(0, pathFile.lastIndexOf(File.separator)));
        File file = new File(pathFile);
        if (!fileDir.exists()) fileDir.mkdirs();
        if (!file.exists()) file.createNewFile();
        return file;
    }

    /**
     * 不压缩，传入压缩过的bitampa
     *
     * @param bitmap
     * @return
     */
    public static File saveBitmapToFile(Bitmap bitmap, String path) {
        File file = null;
        try {
            file = createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveBitmapToFile(bitmap, file);
    }
    /*
     * 由file转bitmap,压缩
     */

    public static Bitmap decodeBitmapFromStream(InputStream inputStream, long contentLength, int widthReq, int heightReq) {

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        // Calculate inSampleSize
        //不让图片文件>500kb,不一定精准
        float ratio = contentLength * 1f / 1024 / 500;
        if (ratio >= 1) {
            options.inSampleSize = (int) Math.round(ratio + 1.5);
        } else {
            options.inSampleSize = 2;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        if(bitmap==null)return null;
        return compressBitmap(bitmap, widthReq, heightReq);
    }

    /*
     * 由file转bitmap,压缩
     */

    public static Bitmap decodeBitmapFromPath(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /*
     * 计算采样率
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public static Bitmap cropBitmap(Bitmap bitmap, float hRatioW) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        return Bitmap.createBitmap(bitmap, 0, 0, w, (int) (w * hRatioW), null, false);
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
    }

    /*
     * 质量压缩法：将图片文件压缩,压缩是耗时操作
     */
    public static void compressBitmapToFile(CompressFileBean compressFileBean, CompressFileCallback compressFileCallback) {
        new CompressFileThread(compressFileBean, compressFileCallback).start();
    }
    /*
     * 由file转bitmap
     */

    public static Bitmap decodeBitmapFromFilePath(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//如此，方可decode bitmap

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, int widthReq, int heightReq) {
        Matrix matrix = new Matrix();
        float ratio_width = widthReq * 1f / bitmap.getWidth();
        float ratio_height = heightReq * 1f / bitmap.getHeight();
        float ratio = Math.min(ratio_width, ratio_height);
        matrix.setScale(ratio, ratio);
        final Bitmap bitmap_result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Log.e("wechat", "压缩后图片的大小" + (bitmap_result.getByteCount() / 1024)
                + "KB,宽度为" + bitmap_result.getWidth() + "高度为" + bitmap_result.getHeight());
        return bitmap_result;
    }
    public static Bitmap compressBitmapRecycle(Bitmap bitmap, int widthReq, int heightReq) {
        final Bitmap bitmap_result =compressBitmap(bitmap,widthReq,heightReq);
        bitmap.recycle();
        bitmap=null;
        return bitmap_result;
    }

    private static class CompressFileThread extends Thread {
        private Handler handler_deliver = new Handler(Looper.getMainLooper());
        private CompressFileBean compressFileBean;
        private CompressFileCallback compressFileCallback;


        public CompressFileThread(CompressFileBean compressFileBean, CompressFileCallback compressFileCallback) {
            this.compressFileBean = compressFileBean;
            this.compressFileCallback = compressFileCallback;
        }

        @Override
        public void run() {
            super.run();
//            final Bitmap bitmap = decodeBitmapFromFilePath(compressFileBean.getPathSource(), compressFileBean.getReqWidth(), compressFileBean.getReqHeight());
            final Bitmap bitmap = compressBitmap(compressFileBean.getBitmap(), compressFileBean.getReqWidth(), compressFileBean.getReqHeight());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int quality = 80;
            //quality，压缩精度尽量不要低于50，否则影响清晰度,不支持压缩PNG
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

//            long size=byteArrayOutputStream.size();
            while (byteArrayOutputStream.toByteArray().length / 1024 > compressFileBean.getKb_max() && quality > compressFileBean.getQuality_min()) {
                LogUtils.log("compress", byteArrayOutputStream.toByteArray().length / 1024);

                // 循环判断如果压缩后图片是否大于kb_max kb,大于继续压缩,
                byteArrayOutputStream.reset();
                quality -= 10;
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream);
            }
            try {
                final File fileCompressed = createFile(compressFileBean.getPathCompressed());
                FileOutputStream fileOutputStream = new FileOutputStream(fileCompressed);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());//写入目标文件
                fileOutputStream.flush();
                fileOutputStream.close();
                byteArrayOutputStream.close();
                if (fileCompressed != null && fileCompressed.length() > 0)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //压缩成功
                            compressFileCallback.onCompressFileFinished(fileCompressed, bitmap);
                        }
                    });
            } catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //压缩失败
                        compressFileCallback.onCompressFileFailed("压缩图片文件失败" + e.getMessage());
                    }
                });
            }

        }

        private void runOnUiThread(Runnable run) {
            handler_deliver.post(run);
        }

    }

    public static class CompressFileBean {
        //        private String pathSource;//原图文件路径
        private String pathCompressed;//压缩后的图片文件路径
        private int kb_max = 500;//压缩到多少KB，不能精确，只能<=kb_max
        private int quality_min = 50;//压缩精度，尽量>=50
        private int reqWidth = 1000;//期望的图片宽度
        private int reqHeight = 1000;//期望的图片高度
        private Bitmap bitmap;


        public String getPathCompressed() {
            return pathCompressed;
        }

        public void setPathCompressed(String pathCompressed) {
            this.pathCompressed = pathCompressed;
        }

        public int getKb_max() {
            return kb_max;
        }

        public void setKb_max(int kb_max) {
            this.kb_max = kb_max;
        }

        public int getQuality_min() {
            return quality_min;
        }

        public void setQuality_min(int quality_min) {
            this.quality_min = quality_min;
        }

        public int getReqWidth() {
            return reqWidth;
        }

        public void setReqWidth(int reqWidth) {
            if (reqWidth != 0) this.reqWidth = reqWidth;
        }

        public int getReqHeight() {
            return reqHeight;
        }

        public void setReqHeight(int reqHeight) {
            if (reqHeight != 0) this.reqHeight = reqHeight;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

    public static interface CompressFileCallback {
        //图片压缩成功
        public void onCompressFileFinished(File file, Bitmap bitmap);

        //图片压缩失败
        public void onCompressFileFailed(String errorMsg);
    }
}
