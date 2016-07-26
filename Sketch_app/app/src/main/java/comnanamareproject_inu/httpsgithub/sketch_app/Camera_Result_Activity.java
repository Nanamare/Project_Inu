package comnanamareproject_inu.httpsgithub.sketch_app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by nanamare on 2016-07-22.
 */
public class Camera_Result_Activity extends Activity {
    PhotoViewAttacher mAttacher;
    TextView longitude; //경도
    TextView latitude; //위도
    LocationManager lm;
    LocationListener locListenD;
    double lat;
    double lng;
    Button maker_btn;
    public final static String taglat = "getlatitude";
    public final static String taglng = "getlongitude";
    public final int MY_PERMISSION_REQUEST_FIND_LOCATION = 1;
    //String provider = LocationManager.GPS_PROVIDER;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);

        maker_btn = (Button) findViewById(R.id.maker_btn);

        //사진
        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("strParamName");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        Matrix matrix = new Matrix();
        matrix.preRotate(90);
        Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);

        ImageView img = (ImageView) findViewById(R.id.imageView1);
        img.setImageBitmap(adjustedBitmap);
        mAttacher = new PhotoViewAttacher(img);


        // 갤러리 사용 권한 체크( 사용권한이 없을경우 -1 )
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을경우
            Toast.makeText(getApplicationContext(), "권한 없음", Toast.LENGTH_SHORT).show();
            // 최초 권한 요청인지, 혹은 사용자에 의한 재요청인지 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 사용자가 임의로 권한을 취소시킨 경우
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FIND_LOCATION);

            } else {
                // 최초로 권한을 요청하는 경우(첫실행)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FIND_LOCATION);
            }
        } else {

            // 사용 권한이 있음을 확인한 경우
            LocationManager locationManager;
            String context = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) getSystemService(context);
            String provider2 = LocationManager.NETWORK_PROVIDER;
            Location location = locationManager.getLastKnownLocation(provider2);
            updateWithNewLocation(location);

        }

        maker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a, b;
                a = Double.toString(lat);
                b = Double.toString(lng);
                Log.d("lat", a);
                Log.d("lng", b);
                Intent intent = new Intent(getApplicationContext(), Map_Activity.class);
                intent.putExtra(taglat, a);
                intent.putExtra(taglng, b);
                startActivity(intent);
            }
        });

    }


    private void updateWithNewLocation(Location location) {
        String latLongString;
        TextView myLocationText;
        myLocationText = (TextView) findViewById(R.id.location);
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            latLongString = "위도:" + lat + "\n경도:" + lng;
        } else {
            latLongString = "위치를 찾을수 없음";
        }
        myLocationText.setText("당신의 현재 위치는:\n" + latLongString);
    }

    //Contextcompat.checkselfpermission이랑 짝궁 onRequestoncreate랑 비슷함
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FIND_LOCATION: {
                // 갤러리 사용권한에 대한 콜백을 받음
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 동의 버튼 선택
                    //initLayout();
                } else {
                    // 사용자가 권한 동의를 안함
                    // 권한 동의안함 버튼 선택
                    Toast.makeText(getApplicationContext(), "권한사용을 동의해주셔야 이용이 가능합니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
            // 예외케이스
        }
    }


}



