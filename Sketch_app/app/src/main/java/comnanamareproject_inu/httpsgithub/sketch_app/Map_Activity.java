package comnanamareproject_inu.httpsgithub.sketch_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;


/**
 * Created by nanamare on 2016-07-23.
 */
public class Map_Activity extends Activity {
    final String DAUM_MMAPS_ANDROID_APIKEY = "b9c42236cc84248fa5647dc416a8abdb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey(DAUM_MMAPS_ANDROID_APIKEY);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        Intent intent = getIntent();
        String lat = intent.getStringExtra(Camera_Result_Activity.taglat);
        String lng = intent.getStringExtra(Camera_Result_Activity.taglng);

        if (lat != null && lng != null) {

        Double lat2 = Double.parseDouble(lat);
        Double lng2 = Double.parseDouble(lng);


            //마커
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName("현재 신현성 위치");
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(lat2, lng2));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView.addPOIItem(marker);

            MapPOIItem marke2r = new MapPOIItem();
            marker.setItemName("세종대왕 위치");
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(37.545024, 127.03923));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView.addPOIItem(marker);


            MapPolyline polyline = new MapPolyline();
            polyline.setTag(1000);
            polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.
//
//// Polyline 좌표 지정.
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat2, lng2));
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.545024, 127.03923));
//        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.527896,127.036245));
//        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.541889,127.095388));
//
//// Polyline 지도에 올리기.
            mapView.addPolyline(polyline);
//
//// 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
            MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
            int padding = 100; // px
            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        }
    }
}
