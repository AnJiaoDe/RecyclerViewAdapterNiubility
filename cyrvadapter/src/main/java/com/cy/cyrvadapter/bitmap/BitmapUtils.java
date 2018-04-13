package com.cy.cyrvadapter.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lenovo on 2017/12/24.
 */

public class BitmapUtils {

    /**

     * 读取图片的旋转的角度

     *

     * @param path

     *            图片绝对路径

     * @return 图片的旋转角度

     */

    public static int getBitmapDegree(String path) {

        int degree = 0;

        try {

            // 从指定路径下读取图片，并获取其EXIF信息

            ExifInterface exifInterface = new ExifInterface(path);

            // 获取图片的旋转信息

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,

                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:

                    degree = 90;

                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:

                    degree = 180;

                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:

                    degree = 270;

                    break;

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        return degree;

    }




    /**

     * 将图片按照某个角度进行旋转

     *

     * @param bm

     *            需要旋转的图片

     * @param degree

     *            旋转角度

     * @return 旋转后的图片

     */

    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {

        Bitmap returnBm = null;



        // 根据旋转角度，生成旋转矩阵

        Matrix matrix = new Matrix();

        matrix.postRotate(degree);

        try {

            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片

            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        } catch (OutOfMemoryError e) {

        }

        if (returnBm == null) {

            returnBm = bm;

        }

        if (bm != returnBm) {

            bm.recycle();

        }

        return returnBm;

    }
    /*
  * 质量压缩法：将bitmap压缩为文件,压缩是耗时操作
  */
    public static File compressBmpToFile(Bitmap bitmap, File file, Handler handler,int what) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 80;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        while (baos.toByteArray().length / 1024 > 100 && quality > 50) {

            // 循环判断如果压缩后图片是否大于200kb,大于继续压缩,

            baos.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message message=Message.obtain();
        message.what=what;
        message.obj=file;
        handler.sendMessage(message);
        return file;
    }
    /*
  * 质量压缩法：将图片文件压缩,压缩是耗时操作
  */
    public static File compressFile( File file,int reqWidth, int reqHeight,OnBitmapListener onBitmapListener) {
        Bitmap bitmap=decodeBitmapFromFilePath(file.getAbsolutePath(),reqWidth,reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 80;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        while (baos.toByteArray().length / 1024 > 100 && quality > 50) {

            // 循环判断如果压缩后图片是否大于200kb,大于继续压缩,

            baos.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onBitmapListener.onCompressFileFinish(file);
//        Message message=Message.obtain();
//        message.what=what;
//        message.obj=file;
//        handler.sendMessage(message);

        return file;
    }
    public static abstract class OnBitmapListener{
        public void onCompressFileFinish(File file){}
    }


	/*
     * 由file转bitmap
	 */

    public static Bitmap decodeBitmapFromFilePath(String path, int reqWidth,
                                                  int reqHeight) {

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
    /*
      switch (BitmapUtils.getBitmapDegree(pictureFile.getAbsolutePath())) {
//                                case 0:
//                                    Log.e("0","=================");
//                                    break;
//                                case 90:
//                                    Log.e("90","=================");
//
//                                    originBitmap = BitmapUtils.rotateBitmapByDegree(originBitmap, 180);
//
//
//                                    break;
//                                case 180:
//                                    Log.e("180","=================");
//
//                                    originBitmap = BitmapUtils.rotateBitmapByDegree(originBitmap, 180);
//
//                                    break;
//                                case 270:
//                                    Log.e("270","=================");
//
//                                    originBitmap = BitmapUtils.rotateBitmapByDegree(originBitmap, 90);
//
//                                    break;
//                            }
     */


}
