package vladyagl.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.Objects;

import static vladyagl.homework3.MainActivity.IMAGE_FILE;


public class ActionReceiver extends BroadcastReceiver {


    private static final String GACHI = "https://yt3.ggpht.com/-v1lAyEgNFE0/AAAAAAAAAAI/AAAAAAAAAAA/2uC9IGw8jzI/s900-c-k-no-mo-rj-c0xffffff/photo.jpg";
    private static final String SUPER_HIGHRES_BIBLETHUMP = "http://i.imgur.com/7M1ed6R.jpg";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ImageLoadService.class);
        serviceIntent.putExtra(ImageLoadService.IMAGE_FILE, new File(context.getFilesDir(), IMAGE_FILE).getAbsolutePath());
        if (Objects.equals(intent.getAction(), Intent.ACTION_POWER_CONNECTED)) {
            serviceIntent.putExtra(ImageLoadService.IMAGE_URL, GACHI);
        } else if (Objects.equals(intent.getAction(), Intent.ACTION_POWER_DISCONNECTED)) {
            serviceIntent.putExtra(ImageLoadService.IMAGE_URL, SUPER_HIGHRES_BIBLETHUMP);
        }
        context.startService(serviceIntent);
    }
}
