package vladyagl.homework3;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoadService extends Service {
    public static final String FINISH_BROADCAST = "vladyagl.homework3.LOAD_FINISHED";

    public static final String IMAGE_FILE = "image_file";

    public static final String IMAGE_URL = "image_url";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String fileName = intent.getExtras().getString(IMAGE_FILE);
        String ImageUrl = intent.getExtras().getString(IMAGE_URL);
        if (fileName == null) {
            Log.wtf(TAG, "fileName = null");
        } else {
            File file = new File(fileName);

            try {
                new ImageLoader(file, ImageUrl).execute();
            } catch (MalformedURLException e) {
                Log.wtf(TAG, "Broken IMAGE_URL: ", e);
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private class ImageLoader extends AsyncTask<Void, Void, Void> {
        private final File file;
        private final URL url;

        ImageLoader(File file, String url) throws MalformedURLException {
            this.url = new URL(url);
            this.file = file;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try (InputStream inputStream = url.openStream()) {
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    Bitmap tmp = BitmapFactory.decodeStream(inputStream);
                    if (!Bitmap.createScaledBitmap(tmp, 1000, (int)((1000F / tmp.getWidth()) * tmp.getHeight()), false)
                            .compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                        throw new IOException("Can't compress bitmap.");
                    }
                }
            } catch (IOException e) {
                Log.wtf(TAG, "Error while loading image: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sendBroadcast(new Intent(FINISH_BROADCAST));
        }
    }

    private static final String TAG = "ImageLoadService";
}
