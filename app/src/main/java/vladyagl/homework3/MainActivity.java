package vladyagl.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String IMAGE_FILE = "image.jpg";

    private ImageView imageView;
    private TextView errorTextView;
    private File imageFile;

    private final BroadcastReceiver loadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showImage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(loadReceiver, new IntentFilter(ImageLoadService.FINISH_BROADCAST));

        imageView = (ImageView) findViewById(R.id.imageView);
        errorTextView = (TextView) findViewById(R.id.textView);
        imageFile = new File(getFilesDir(), IMAGE_FILE);

        if (imageFile.exists()) {
            showImage();
        } else {
            imageView.setVisibility(View.INVISIBLE);
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(loadReceiver);
    }

    void showImage() {
        imageView.setVisibility(View.VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageView.setImageBitmap(bitmap);
        errorTextView.setVisibility(View.INVISIBLE);
    }
}
