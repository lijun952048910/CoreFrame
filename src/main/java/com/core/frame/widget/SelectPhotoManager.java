package com.core.frame.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.core.frame.R;

import java.io.File;
import java.io.IOException;

public final class SelectPhotoManager
{

	public static final int REQUEST_CODE_CAMERA = 0xf1;
	public static final int REQUEST_CODE_ALBUM = 0xf2;
	public static final int REQUEST_CODE_CROP = 0xf3;
	public static final int ACTION_TAKE_PHOTO = 0;
	public static final int ACTION_ALBUM = 1;
	public static final int ACTION_PREVIEW = 2;
	public static final int ACTION_CANCEL = 3;
	private static SelectPhotoManager sInstance;

	private static String TEMP_PATH_NAME = "/coreFrame";

	public interface SelectClickHandler
	{
		/**
		 * @param action
		 *            can be one of {{@link #ACTION_TAKE_PHOTO}
		 *            {@link #ACTION_ALBUM} {@link #ACTION_CANCEL}
		 */
		void onClick(int action);
	}

	private PhotoReadyHandler mPhotoReadyHandler;
	private File mTempDir;
	private File mTempFile;
	private Activity mActivity;
	private CropOption mCropOption;

	private SelectPhotoManager()
	{
	}

	public static SelectPhotoManager getInstance()
	{
		if (sInstance == null)
		{
			sInstance = new SelectPhotoManager();
		}
		return sInstance;
	}

	public void setCropOption(CropOption option)
	{
		mCropOption = option;
	}

	public void setPhotoReadyHandler(PhotoReadyHandler handler)
	{
		mPhotoReadyHandler = handler;
	}

	/**
	 * 启动相册or相机对话框选择图片
	 * @param activity
	 */
	public void start(Activity activity)
	{
		start(activity, null, 0);
	}

	/** 图片path */
	private String previewImagePath;

	/**
	 * 启动相册or相机对话框选择图片 增加预览已有图片功能
	 * @param activity
	 */
	public void startWithPreview(Activity activity, String path)
	{
		previewImagePath = path;
		start(activity, null, 3);
	}

	/**
	 * 启动相册选择图片
	 * @param activity
     */
	public void startToAlbum(Activity activity)
	{
		start(activity, null, 1);
	}

	/**
	 * 启动相机拍照
	 * @param activity
	 */
	public void startToCamera(Activity activity)
	{
		start(activity, null, 2);
	}

	/**
	 * 清除临时文件
	 * @param activity
	 */
	public void clearTempFile(Activity activity)
	{
		String temp_path = BaseTools.getSDPath(activity) + TEMP_PATH_NAME;

		File path = new File(temp_path);

		try
		{
			deleteDirectoryQuickly(path);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void start(final Activity activity, final SelectClickHandler handler, int type)
	{
		String temp_path = BaseTools.getSDPath(activity) + TEMP_PATH_NAME;

		File path = new File(temp_path);


		if (!path.exists() && !path.mkdirs())
		{
			Toast.makeText(activity, R.string.cube_photo_can_not_use_camera, Toast.LENGTH_SHORT).show();
			return;
		} else
		{
			path.deleteOnExit();
		}

		mTempDir = path;
		mActivity = activity;
		mTempFile = new File(mTempDir.getAbsolutePath(), Long.toString(System.nanoTime()) + ".jpg");

		if(type == 2)
		{
			PhotoUtils.toCamera(activity, mTempFile, REQUEST_CODE_CAMERA);
		}
		else if(type == 1)
		{
			PhotoUtils.toAlbum(activity, REQUEST_CODE_ALBUM);
		}
		else if(type == 0)
		{
			new ImagePicDialog(mActivity, handler, false).show();
		}
		else
		{
			new ImagePicDialog(mActivity, handler, true).show();
		}

		//自定义Dialog

		/*DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int i)
			{
				final int action = i;
				if (handler != null)
				{
					handler.onClick(action);
				}
				switch (action)
				{
					case ACTION_TAKE_PHOTO:
						PhotoUtils.toCamera(activity, mTempFile, REQUEST_CODE_CAMERA);
						break;
					case ACTION_ALBUM:
						PhotoUtils.toAlbum(activity, REQUEST_CODE_ALBUM);
						break;
					case ACTION_CANCEL:
						// do nothing
						break;
				}
				dialog.dismiss();
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setItems(R.array.cube_photo_pick_options, clickListener);
		builder.show().setCanceledOnTouchOutside(true);*/
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != Activity.RESULT_OK)
		{
			return;
		}
		String imgPath;
		if (requestCode == REQUEST_CODE_CAMERA && mTempFile != null)
		{
			imgPath = mTempFile.getPath();
			if (!afterPhotoTaken(imgPath))
			{
				sendMessage(PhotoReadyHandler.FROM_CAMERA, imgPath);
			}
		} else if (requestCode == REQUEST_CODE_ALBUM && data != null)
		{
			Uri imgUri = data.getData();
			imgPath = PhotoUtils.uriToPath(mActivity, imgUri);
			if (!afterPhotoTaken(imgPath))
			{
				sendMessage(PhotoReadyHandler.FROM_ALBUM, imgPath);
			}
		} else if (requestCode == REQUEST_CODE_CROP && mTempFile != null)
		{
			imgPath = mTempFile.getPath();
			sendMessage(PhotoReadyHandler.FROM_CROP, imgPath);
		}
	}

	private void sendMessage(int from, String imgPath)
	{

		if (mPhotoReadyHandler != null)
		{
			mPhotoReadyHandler.onPhotoReady(from, imgPath);
		}

	}

	private boolean afterPhotoTaken(String imgPath)
	{
		if (TextUtils.isEmpty(imgPath))
		{
			return false; // throw new RuntimeException();
		}
		if (mCropOption == null)
		{
			return false;
		}
		File f = new File(imgPath);
		mTempFile = new File(mTempDir.getAbsolutePath(), Long.toString(System.nanoTime()) + "_cropped.jpg");
		PhotoUtils.toCrop(mActivity, f, mTempFile, mCropOption, REQUEST_CODE_CROP);
		return true;
	}

	 /**
     * Try to delete directory in a fast way.
     */
    public static void deleteDirectoryQuickly(File dir) throws IOException {

        if (!dir.exists()) {
            return;
        }
        final File to = new File(dir.getAbsolutePath() + System.currentTimeMillis());
        dir.renameTo(to);
        if (!dir.exists()) {
            // rebuild
            dir.mkdirs();
        }

        // try to run "rm -r" to remove the whole directory
        if (to.exists()) {
            String deleteCmd = "rm -r " + to;
            Runtime runtime = Runtime.getRuntime();
            try {
                Process process = runtime.exec(deleteCmd);
                process.waitFor();
            } catch (IOException e) {

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!to.exists()) {
            return;
        }
        deleteDirectoryRecursively(to);
        if (to.exists()) {
            to.delete();
        }
    }

    /**
     * recursively delete
     *
     * @param dir
     * @throws java.io.IOException
     */
    private static void deleteDirectoryRecursively(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("not a directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectoryRecursively(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    /**
     * 图片选择
     */
    private class ImagePicDialog extends Dialog implements android.view.View.OnClickListener
    {

    	private Button select_pic_from_album_btn;

    	private Button select_pic_from_camera_btn;

    	private Button preview_btn;

    	private Button btn_cancel;

    	private SelectClickHandler handler;

    	private Activity activity;

		// 是否显示预览按钮
		private boolean isSHowPreview = false;

    	public ImagePicDialog(Activity activity, SelectClickHandler handler, boolean isSHowPreview)
    	{
    		super(activity, R.style.attr_dialog);

    		setCanceledOnTouchOutside(true);
    		this.handler = handler;
    		this.activity = activity;
    		this.isSHowPreview = isSHowPreview;
    	}

    	@Override
    	protected void onCreate(Bundle savedInstanceState)
    	{
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.mamahelp_select_pic);

    		Window dialogWindow = getWindow();
    		dialogWindow.setGravity(Gravity.BOTTOM);
    		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    		lp.width = BaseTools.getScreenSize(this.getContext()).x;
    		dialogWindow.setAttributes(lp);
			setCancelable(false);

			preview_btn = (Button) findViewById(R.id.preview_btn);
			select_pic_from_album_btn = (Button) findViewById(R.id.select_pic_from_album_btn);
    		select_pic_from_camera_btn = (Button) findViewById(R.id.select_pic_from_camera_btn);
    		btn_cancel = (Button) findViewById(R.id.cancel_select_pic_btn);

			if(isSHowPreview)
			{
				preview_btn.setVisibility(View.VISIBLE);
			}
			preview_btn.setOnClickListener(this);
    		select_pic_from_album_btn.setOnClickListener(this);
    		select_pic_from_camera_btn.setOnClickListener(this);
    		btn_cancel.setOnClickListener(this);
    	}

    	@Override
    	public void onClick(View v)
    	{
    		int action = 0;

    		if(v == select_pic_from_album_btn) // 从相册选择
    		{
    			action = 0;
                PhotoUtils.toAlbum(activity, REQUEST_CODE_ALBUM);
			}
    		else if(v == select_pic_from_camera_btn) // 从图片选择
    		{
    			PhotoUtils.toCamera(activity, mTempFile, REQUEST_CODE_CAMERA);
    			action = 1;
    		}
    		else if(v == preview_btn)
    		{
    			action = 2;
    		}
    		else
    		{
    			action = 3;
    		}

    		if (handler != null)
    		{
    			handler.onClick(action);
    		}
    		dismiss();
    	}

    }
}
