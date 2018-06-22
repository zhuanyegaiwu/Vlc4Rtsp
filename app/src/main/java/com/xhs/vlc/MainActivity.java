package com.xhs.vlc;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.xhs.vlc.constant.Constant;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

/**
 * 作者: 布鲁斯.李 on 2018/6/7 11 03
 * 邮箱: lzp_lizhanpeng@163.com
 */
public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SurfaceView srfc = null;
    private LibVLC libVLC = null;
    private int width;
    private int height;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        getScreenW4H();
        initPlay();
    }

    private void getScreenW4H() {
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    private void initPlay() {
        webview =findViewById(R.id.webview);
        webview.loadUrl("https://blog.csdn.net/lowprofile_coding/article/details/77928614");//加载url
        srfc=findViewById(R.id.srfc);
        ArrayList<String> options = new ArrayList<>();
        libVLC = new LibVLC(getApplication(), options);
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new MediaPlayer(libVLC);
            mediaPlayer.getVLCVout().setWindowSize(width, height);
            mediaPlayer.setAspectRatio(width+":"+height);
            mediaPlayer.setScale(0);
            mediaPlayer.getVLCVout().setVideoSurface(srfc.getHolder().getSurface(), srfc.getHolder());
            mediaPlayer.getVLCVout().attachViews();
            Media media = new Media(libVLC, Uri.parse(Constant.uPath));
            int cache = 500; //设置缓存数加载
            media.addOption(":network-caching=" + cache);
            media.addOption(":file-caching=" + cache);
            media.addOption(":live-cacheing=" + cache);
            media.addOption(":sout-mux-caching=" + cache);
            media.addOption(":codec=mediacodec,iomx,all");
            mediaPlayer.setMedia(media);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
