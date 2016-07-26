package comnanamareproject_inu.httpsgithub.sketch_app;

import android.app.Activity;
import android.view.SurfaceHolder;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nanamare on 2016-07-22.
 */
public class Camera_Activity extends Activity implements SurfaceHolder.Callback {
    @SuppressWarnings("deprecation")
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button sv_btn;
    String str;
    Button AutoFocus;

    @SuppressWarnings("deprecation")
    Camera.PictureCallback jpegCallback;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        sv_btn = (Button) findViewById(R.id.sv_btn);
        AutoFocus = (Button) findViewById(R.id.AutoFocus);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        AutoFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv_btn.setEnabled(false);
                camera.autoFocus(mAutoFoucus);
            }
        });

        sv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, jpegCallback);

            }
        });

        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv_btn.setEnabled(false);
                camera.autoFocus(mAutoFoucus);
            }
        });


        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    long time = System.currentTimeMillis();
                    SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String str2 = dayTime.format(new Date(time));
                    str = String.format("/sdcard/Download/%s.jpg",
                            str2);
                    //System.currentTimeMillis()
                    outStream = new FileOutputStream(str);
                    outStream.write(data);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }

                Toast.makeText(getApplicationContext(),
                        "Picture Saved", Toast.LENGTH_LONG).show();
                refreshCamera();

                Intent intent = new Intent(getApplicationContext(),
                        Camera_Result_Activity.class);
                intent.putExtra("strParamName", str);
                startActivity(intent);
            }
        };


    }

    //콜백 포커싱
    Camera.AutoFocusCallback mAutoFoucus = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
//            if(success){
//                camera.takePicture(null, null, jpegCallback);
//            }
            sv_btn.setEnabled(success);
        }
    };


    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
            //camera.autoFocus(mAutoFoucus);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @SuppressWarnings("deprecation")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        camera = Camera.open();
        camera.stopPreview();
        Camera.Parameters param = camera.getParameters();
        param.setRotation(90);
        camera.setParameters(param);

        try {

            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


}
