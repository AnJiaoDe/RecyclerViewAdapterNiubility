package com.cy.recyclerviewadapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.refreshlayout.LogUtils;
import com.cy.recyclerviewadapter.activity.grv.GRVTypeActivity;
import com.cy.recyclerviewadapter.activity.hr.HRVActivity;
import com.cy.recyclerviewadapter.activity.sgrv.SGRVTypeActivity;
import com.cy.recyclerviewadapter.activity.tabrv.TabLayoutRVActivity;
import com.cy.recyclerviewadapter.activity.vr.VRTypeActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_vertical).setOnClickListener(this);
        findViewById(R.id.btn_horizontal).setOnClickListener(this);
        findViewById(R.id.btn_grid).setOnClickListener(this);
        findViewById(R.id.btn_staggeredGrid).setOnClickListener(this);
        findViewById(R.id.btn_tabRV).setOnClickListener(this);
//        findViewById(R.id.btn_code_adapter).setOnClickListener(this);
//        findViewById(R.id.btn_extendsRV).setOnClickListener(this);

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
        Log.e("TAG", "Max memory is " + maxMemory + "MB");


//        CrashHandler.getInstance().init(getContext_applicaiton(), "crash_RecyclerVeiwAdapter");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpsURLConnection con = null;
//                try {
//                    URL url = new URL("https://pp.myapp.com/ma_icon/0/icon_10452361_1588219405/256");
//                    con = (HttpsURLConnection) url.openConnection();
//                    con.setConnectTimeout(5 * 1000);
//                    con.setReadTimeout(10 * 1000);
//
//                    // First decode with inJustDecodeBounds=true to check dimensions
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;//如此，无法decode bitmap
//                    InputStream inputStream = con.getInputStream();
//                    RecyclableBufferedInputStream recyclableBufferedInputStream=new RecyclableBufferedInputStream(inputStream,null);
//                    BitmapFactory.decodeStream(recyclableBufferedInputStream, null, options);
//
//
//                    // Calculate inSampleSize
//                    options.inSampleSize = calculateInSampleSize(options, 100, 100);
//
//                    // Decode bitmap with inSampleSize set
//                    options.inJustDecodeBounds = false;//如此，方可decode bitmap
//
//                    recyclableBufferedInputStream.mark(5*1024*1024);
//                    recyclableBufferedInputStream.reset();
//                    final Bitmap bitmap = BitmapFactory.decodeStream(recyclableBufferedInputStream, null, options);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ((ImageView) findViewById(R.id.iv)).setImageBitmap(bitmap);
//                        }
//                    });
//                } catch (MalformedURLException e) {
//                    LogUtils.log("e.printStackTrace()", e.getMessage());
//                    e.printStackTrace();
//
//                } catch (IOException e) {
//                    LogUtils.log("e.printStackTrace()", e.getMessage());
//                    e.printStackTrace();
//
//                } finally {
//                    if (con != null) {
//                        con.disconnect();
//                    }
//                }
//            }
//        }).start();
    }

    //可以直接将网络连接得到的输入流读取到字节流，压缩为bitmpa
    public Bitmap decodeBitmapFromBytes(byte[] data, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /*
     * 计算采样率
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_vertical:
                startAppcompatActivity(VRTypeActivity.class);
                break;
            case R.id.btn_horizontal:
                startAppcompatActivity(HRVActivity.class);
                break;
            case R.id.btn_grid:
                startAppcompatActivity(GRVTypeActivity.class);
                break;
            case R.id.btn_staggeredGrid:
                startAppcompatActivity(SGRVTypeActivity.class);
                break;
            case R.id.btn_tabRV:
                startAppcompatActivity(TabLayoutRVActivity.class);
                break;
//            case R.id.btn_code_adapter:
//                startAppcompatActivity(VRCodeViewActivity.class);
//                break;
//            case R.id.btn_extendsRV:
//                startAppcompatActivity(ExtendsRVActivity.class);
//                break;
        }
    }

    private class MyFilterInputStream extends FilterInputStream {

        /**
         * Creates a <code>FilterInputStream</code>
         * by assigning the  argument <code>in</code>
         * to the field <code>this.in</code> so as
         * to remember it for later use.
         *
         * @param in the underlying input stream, or <code>null</code> if
         *           this instance is to be created without an underlying stream.
         */
        protected MyFilterInputStream(InputStream in) {
            super(in);
        }
    }
}
