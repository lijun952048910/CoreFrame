package com.core.frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.frame.widget.PhotoReadyHandler;
import com.core.frame.widget.SelectPhotoManager;

import java.io.File;


/**
 * @author lijun
 * @date 2016/10/27 17:42
 */
public class Phd extends  BaseCoreActivity implements PhotoReadyHandler {

    private TextView fileSize;
    private TextView imageSize;
    private TextView thumbFileSize;
    private TextView thumbImageSize;
    private ImageView image;
    private Button btn_select,btn_ys;
    private SelectPhotoManager mSelectPhotoManager = SelectPhotoManager.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phd_activity);
        fileSize = (TextView)findViewById(R.id.file_size);
        imageSize = (TextView)findViewById(R.id.image_size);
        thumbFileSize = (TextView)findViewById(R.id.thumb_file_size);
        thumbImageSize = (TextView)findViewById(R.id.thumb_image_size);
        image = (ImageView)findViewById(R.id.image);
        btn_select= (Button)findViewById(R.id.btn_select);
        btn_ys= (Button)findViewById(R.id.btn_ys);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectPhotoManager.start(Phd.this);
            }
        });
        btn_ys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compressWithLs(new File(mPictureUrl));
            }
        });
    }
    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(File file) {
       /* Luban.get(Phd.this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setFilename(System.currentTimeMillis() + "")
                .setCompressListener(new OnCompressListener() {

                    @Override
                    public void onStart() {
                        Toast.makeText(Phd.this, "I'm start", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess(File file) {
                        Log.i("path", file.getAbsolutePath());
                        ImageLoader.getInstance().displayImage(file.getAbsolutePath(),image);
                        thumbFileSize.setText(file.length() / 1024 + "k");
                        thumbImageSize.setText(Luban.get(Phd.this).getImageSize(file.getPath())[0] + " * " + Luban.get(Phd.this).getImageSize(file.getPath())[1]);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();*/
    }
    String mPictureUrl="";
    @Override
    public void onPhotoReady(int from, String imgPath) {
        if (TextUtils.isEmpty(imgPath)) return;
        mPictureUrl=imgPath;
        File imgFile = new File(imgPath);
        if (imgFile.exists()){
            mPictureUrl = imgFile.getAbsolutePath();
        }
        fileSize.setText(imgFile.length() / 1024 + "k");
       // imageSize.setText(Luban.get(getApplicationContext()).getImageSize(imgFile.getPath())[0] + " * " + Luban.get(getApplicationContext()).getImageSize(imgFile.getPath())[1]);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSelectPhotoManager.setPhotoReadyHandler(this);
        mSelectPhotoManager.setCropOption(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSelectPhotoManager.onActivityResult(requestCode, resultCode, data);
    }
}