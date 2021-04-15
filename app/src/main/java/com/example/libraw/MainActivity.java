package com.example.libraw;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int PREVIEW_CODE = 1;
    private static final int TO_DNG_CODE = 2;
    private static final int TO_JPG_CODE = 3;
    private static final int TO_THUMBNAIL_CODE = 4;
    private static final int REQUEST_PERMISSION_CODE = 5;

    private static final String IMAGE_SUB_PATH = "/libraw";
    private static final String CACHE_FILENAME = "file.cache";

    private static final String SUCCESS = "success";
    private static final String DECODE_RAW_FAILED = "decode raw failed";
    private static final String DECODE_THUMBNAIL_FAILED = "decode thumbnail failed";

    Button preview, toDng, toJpg, toThumbnail;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        preview = findViewById(R.id.preview);
        toDng = findViewById(R.id.toDng);
        toJpg = findViewById(R.id.toJpg);
        toThumbnail = findViewById(R.id.toThumbnail);
        imageView = findViewById(R.id.imageView);

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles(false, PREVIEW_CODE);
            }
        });

        toDng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles(true, TO_DNG_CODE);
            }
        });

        toJpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles(true, TO_JPG_CODE);
            }
        });

        toThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles(true, TO_THUMBNAIL_CODE);
            }
        });
    }

    private void setButtonsEnable(boolean enable) {
        preview.setEnabled(enable);
        toDng.setEnabled(enable);
        toJpg.setEnabled(enable);
        toThumbnail.setEnabled(enable);
    }

    private void setButtonsEnableOnUiThread(boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setButtonsEnable(enable);
            }
        });
    }

    private void showToastOnUiThread(String text, int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, duration).show();
            }
        });
    }

    private void requestPermissions() {
        for (String permission: new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION_CODE);
            }
        }
    }

    private void openFiles(boolean allowMultiple, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PREVIEW_CODE:
                    Uri uri = data.getData();
                    // doPreview(uri); // very slow
                    doPreviewThumbnail(uri);
                    break;
                case TO_DNG_CODE:
                case TO_JPG_CODE:
                case TO_THUMBNAIL_CODE:
                    Uri[] uris;
                    if (null != data.getClipData()) {
                        uris = new Uri[data.getClipData().getItemCount()];
                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            uris[i] = data.getClipData().getItemAt(i).getUri();
                        }
                    } else {
                        uris = new Uri[]{data.getData()};
                    }
                    if (requestCode == TO_DNG_CODE) {
                        doToDng(uris);
                    } else {
                        doToJpgOrThumbnail(uris, requestCode == TO_JPG_CODE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void uriToFile(final Uri uri, String filename) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = openFileOutput(filename, MODE_PRIVATE);
            byte[] temp = new byte[1024 * 1024];
            while (true) {
                int size = inputStream.read(temp);
                if (size <= 0) {
                    break;
                }
                outputStream.write(temp, 0, size);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doPreview(final Uri uri) {
        new Thread() {
            @Override
            public void run() {
                setButtonsEnableOnUiThread(false);
                uriToFile(uri, CACHE_FILENAME);
                String cacheFile = getFileStreamPath(CACHE_FILENAME).getAbsolutePath();
                final Bitmap bitmap = LibRaw.decodeAsBitmap(cacheFile, true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            Toast.makeText(getApplicationContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), DECODE_RAW_FAILED, Toast.LENGTH_SHORT).show();
                        }
                        setButtonsEnable(true);
                    }
                });
            }
        }.start();
    }

    private Bitmap rotateBitmap(Bitmap origin, int orientation) {
        if (null == origin) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        switch (orientation) {
            case 0:
                break;
            case 3:
                matrix.setRotate(180);
                break;
            case 5:
                matrix.setRotate(-90);
                break;
            case 6:
                matrix.setRotate(90);
                break;
            default:
                break;
        }
        Bitmap result = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return result;
    }

    private void doPreviewThumbnail(final Uri uri) {
        new Thread() {
            @Override
            public void run() {
                setButtonsEnableOnUiThread(false);
                uriToFile(uri, CACHE_FILENAME);
                String cacheFile = getFileStreamPath(CACHE_FILENAME).getAbsolutePath();

                byte[] thumbnail = LibRaw.getThumbnail(cacheFile);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null == thumbnail) {
                            Toast.makeText(getApplicationContext(), DECODE_RAW_FAILED, Toast.LENGTH_SHORT).show();
                        } else {
                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            opts.inSampleSize = 4; // setImageBitmap too large image will fail
                            Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length, opts);
                            int orientation = LibRaw.getOrientation();
                            bitmap = rotateBitmap(bitmap, orientation);
                            if (null != bitmap) {
                                imageView.setImageBitmap(bitmap);
                                Toast.makeText(getApplicationContext(), SUCCESS, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), DECODE_THUMBNAIL_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        }
                        setButtonsEnable(true);
                    }
                });
            }
        }.start();
    }

    private void doToDng(final Uri[] uris) {
        new Thread() {
            @Override
            public void run() {
                setButtonsEnableOnUiThread(false);
                for (int i = 0; i < uris.length; i++) {
                    uriToFile(uris[i], CACHE_FILENAME);
                    String cache = getFileStreamPath(CACHE_FILENAME).getAbsolutePath();

                    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    String filename = format.format(new Date()) + ".dng";

                    String storePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + IMAGE_SUB_PATH;
                    File appDir = new File(storePath);
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }

                    File file = new File(appDir, filename);
                    Raw2dng.raw2dng(cache, file.getAbsolutePath());
                    Uri uri = Uri.fromFile(file);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                }
                showToastOnUiThread(SUCCESS, Toast.LENGTH_SHORT);
                setButtonsEnableOnUiThread(true);
            }
        }.start();
    }

    private void doToJpgOrThumbnail(final Uri[] uris, boolean isToJpg) {
        new Thread() {
            @Override
            public void run() {
                setButtonsEnableOnUiThread(false);
                for (int i = 0; i < uris.length; i++) {
                    uriToFile(uris[i], CACHE_FILENAME);
                    String cacheFile = getFileStreamPath(CACHE_FILENAME).getAbsolutePath();
                    Bitmap bitmap = null;
                    byte[] thumbnail = null;
                    if (isToJpg) {
                        bitmap = LibRaw.decodeAsBitmap(cacheFile, false);
                    } else {
                        thumbnail = LibRaw.getThumbnail(cacheFile);
                    }

                    if (null == bitmap && null == thumbnail) {
                        showToastOnUiThread(DECODE_RAW_FAILED, Toast.LENGTH_SHORT);
                        setButtonsEnableOnUiThread(true);
                        return;
                    }

                    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    String filename;
                    if (isToJpg) {
                        filename = format.format(new Date()) + ".jpg";
                    } else {
                        filename = format.format(new Date()) + ".thumb.jpg";
                    }

                    String storePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + IMAGE_SUB_PATH;
                    File appDir = new File(storePath);
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }

                    File file = new File(appDir, filename);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        if (isToJpg) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        } else {
                            outputStream.write(thumbnail, 0, thumbnail.length);
                        }
                        outputStream.flush();
                        outputStream.close();
                        if (isToJpg == false) {
                            int orientation = LibRaw.getOrientation();
                            rotateJpg(file.getAbsolutePath(), orientation);
                        }

                        Uri uri = Uri.fromFile(file);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    } catch (IOException e) {
                        showToastOnUiThread(e.getMessage(), Toast.LENGTH_LONG);
                        setButtonsEnableOnUiThread(true);
                        return;
                    }
                }
                showToastOnUiThread(SUCCESS, Toast.LENGTH_SHORT);
                setButtonsEnableOnUiThread(true);
            }
        }.start();
    }

    private void rotateJpg(String filename, int orientation) {
        try {
            ExifInterface exifInterface = new ExifInterface(filename);
            switch (orientation) {
                case 0:
                    break;
                case 3:
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_180));
                    break;
                case 5:
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_270));
                    break;
                case 6:
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
                    break;
            }
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}