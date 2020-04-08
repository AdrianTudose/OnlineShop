package com.example.onlineshop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraActivity extends AppCompatActivity {

    Camera.PictureCallback mCameraPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bm = BitmapFactory.decodeByteArray(data,0,data.length);
            Bitmap image;

            if(activityOrientation == Configuration.ORIENTATION_PORTRAIT) {
                Matrix m=new Matrix();
                m.postRotate(90);
                image=Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),m,true);
            } else {
                image=Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),null,true);
            }

            String fileName = "image" + currentDateFormat() +".jpg";

            if (savePhoto(image,fileName)){
                String message="Image saved to " + "/DCIM/" + fileName;
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            } else {
                String message="Image could not be saved.";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            camera.startPreview();
        }

        private boolean savePhoto(Bitmap image, String name) {
            try {
                MediaStore.Images.Media.insertImage(getContentResolver(), image, name , null);
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }

        private String currentDateFormat() {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = Calendar.getInstance().getTime();
            return dateFormat.format(date);
        }
    };
    private int activityOrientation = Configuration.ORIENTATION_PORTRAIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
    }

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    private static final int REQUEST_PERMISSIONS = 34;

    @SuppressLint("NewApi")
    private boolean arePermissionsDenied() {
        for(String permission : PERMISSIONS) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return true;
        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0) {
            if(arePermissionsDenied()) {
                ((ActivityManager) (Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE)))).clearApplicationUserData();
                recreate();
            } else {
                onResume();
            }
        }
    }

    private boolean isCameraInitialized;

    private Camera mCamera = null;

    private static SurfaceHolder myHolder;

    private static CameraPreview mPreview;

    private FrameLayout preview;


    private static OrientationEventListener orientationEventListener = null;

    private static boolean fM;

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && arePermissionsDenied()) {
            requestPermissions(PERMISSIONS,REQUEST_PERMISSIONS);
            return;
        }
        if(!isCameraInitialized) {
            mCamera = Camera.open();
            mPreview = new CameraPreview(this, mCamera);
            preview = findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            rotateCamera();
            orientationEventListener = new OrientationEventListener(this) {
                @Override
                public void onOrientationChanged(int i) {
                    rotateCamera();
                }
            };
            orientationEventListener.enable();
            preview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(wichCamera) {
                        if(fM) {
                            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        } else {
                            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                        }
                        try {
                            mCamera.setParameters(p);
                        }
                        catch (Exception e) {

                        }
                        fM = !fM;
                    }
                    return true;
                }
            });
        }

    }

    @Override
    protected  void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if(mCamera != null) {
            preview.removeView(mPreview);
            mCamera.release();
            orientationEventListener.disable();
            mCamera = null;
            wichCamera = !wichCamera;
        }
    }

    private static List<String> camEffects;

    private static int rotation;

    private static boolean wichCamera = true;

    private static Camera.Parameters p;

    private void rotateCamera() {
        if(mCamera != null) {
            rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            if(rotation == 0) {
                rotation = 90;
            } else if(rotation == 1) {
                rotation = 0;
            } else if(rotation == 2) {
                rotation =270;
            } else if(rotation == 3) {
                rotation = 180;
            }
            mCamera.setDisplayOrientation(rotation);
            if(!wichCamera) {
                if(rotation == 90) {
                    rotation = 270;
                } else if(rotation == 270) {
                    rotation = 90;
                }

            }
            p = mCamera.getParameters();
            p.setRotation(rotation);
            mCamera.setParameters(p);
        }
    }

    private static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
        private static SurfaceHolder mHolder;
        private static Camera mCamera;

        private CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            myHolder = holder;
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {}

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {}
    }

    public void takePhoto(View view) {
        if(mCamera!=null)
        {
            mCamera.takePicture(null,null,mCameraPictureCallback);
        }
    }

}
