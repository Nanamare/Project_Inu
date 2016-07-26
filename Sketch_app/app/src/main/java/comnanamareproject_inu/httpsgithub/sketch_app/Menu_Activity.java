package comnanamareproject_inu.httpsgithub.sketch_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by nanamare on 2016-07-21.
 */
public class Menu_Activity extends AppCompatActivity {
    TextView menu_text;
    Button start_btn;
    Button picture_btn;
    Button map_btn;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menu_text = (TextView) findViewById(R.id.menu_text);
        start_btn = (Button) findViewById(R.id.start_btn);
        picture_btn = (Button) findViewById(R.id.picture_btn);
        map_btn = (Button) findViewById(R.id.map_btn);

        Intent intent = getIntent();
        String name = intent.getStringExtra(Login_Activity.EXTRA_MESSAGE).toString();

        menu_text.setText(name + "님 반갑습니다.");

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "게임 시작", Toast.LENGTH_SHORT).show();
            }
        });

        picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "사진 모드", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent,1);
                Intent intent = new Intent(getApplicationContext(), Camera_Activity.class);
                startActivity(intent);
            }
        });

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu_Activity.this, "다음 맵 보기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Map_Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == 1 && !data.equals(null)) {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    //String ImageBase64 = Base64.encodeBytes(image);
                    String ImageBase64 = Base64.encodeToString(image, 0);
                    //이후 서버를 통해 db에 png 파일 저장
                } catch (Exception e) {
                    return;
                }
            }
        }
    }


}
