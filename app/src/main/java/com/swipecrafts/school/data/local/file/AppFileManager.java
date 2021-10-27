package com.swipecrafts.school.data.local.file;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import okhttp3.ResponseBody;

/**
 * Created by Madhusudan Sapkota on 3/4/2018.
 */

public class AppFileManager {

    private Context context;

    @Inject
    public AppFileManager(Context context) {
        this.context = context;
    }

    public String appDirectory() {
        return context.getFilesDir().getAbsolutePath();
    }

    private void writeToDisk(@NonNull File location, FileWriteListener listener) {

    }

    public String getImageDirectory(String path, String fileName) {
        return context.getFilesDir() + File.separator + path + File.separator + fileName + ".png";
    }

    public void writeToDisk(@NonNull ResponseBody body, String filepath, String fileNameWithType, FileWriteListener listener) {

        String url = context.getFilesDir() + File.separator + filepath + File.separator + fileNameWithType;
        File temp = new File(url);
        if (temp.exists() && temp.isFile()) {
            Log.e("FileCache", "local");
            listener.onSuccess(temp, url);
            return;
        }

        new AsyncTask<Void, Long, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    File path = new File(context.getFilesDir(), filepath);
                    if (!path.exists()) path.mkdirs();
                    File downloadedFile = new File(path, fileNameWithType);
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    try {
                        byte[] fileReader = new byte[4096];

                        long fileSize = body.contentLength();
                        long fileSizeDownloaded = 0;

                        inputStream = body.byteStream();
                        outputStream = new FileOutputStream(downloadedFile);

                        while (true) {
                            int read = inputStream.read(fileReader);
                            if (read == -1) break;
                            outputStream.write(fileReader, 0, read);
                            fileSizeDownloaded += read;
                            listener.onProgress((fileSizeDownloaded / fileSize) * 100);
                        }
                        outputStream.flush();

                        listener.onSuccess(downloadedFile, context.getFilesDir() + File.separator + filepath + File.separator + fileNameWithType);
                    } catch (IOException e) {
                        listener.onFailed(e.getMessage());
                    } finally {
                        if (inputStream != null) inputStream.close();
                        if (outputStream != null) outputStream.close();
                    }
                } catch (IOException e) {
                    listener.onFailed(e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    public void writeToDisk(@NonNull ResponseBody body, String filepath, String fileName, String fileType, FileWriteListener listener) {
        this.writeToDisk(body, filepath, fileName + fileType, listener);
    }

    public void writeToDisk(@NonNull ResponseBody body, String filepath, String fileName, FileListener listener) {
        new AsyncTask<Void, Long, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                File path = new File(context.getFilesDir(), filepath);
                if (!path.exists()) path.mkdirs();

                File downloadedFile = new File(path, fileName + ".png");

                listener.onProgress(0, downloadedFile);

                try {
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    try {
                        byte[] fileReader = new byte[4096];

                        long fileSize = body.contentLength();
                        long fileSizeDownloaded = 0;

                        inputStream = body.byteStream();
                        outputStream = new FileOutputStream(downloadedFile);

                        while (true) {
                            int read = inputStream.read(fileReader);
                            if (read == -1) break;
                            outputStream.write(fileReader, 0, read);
                            fileSizeDownloaded += read;
                            listener.onProgress((fileSizeDownloaded / fileSize) * 100, downloadedFile);
                        }
                        outputStream.flush();
                        listener.onProgress(100, downloadedFile);

                    } catch (IOException e) {
                        listener.onFailed(e.getMessage());
                    } finally {
                        if (inputStream != null) inputStream.close();
                        if (outputStream != null) outputStream.close();
                    }
                } catch (IOException e) {
                    listener.onFailed(e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    public interface FileWriteListener {
        void onSuccess(File file, String url);

        void onProgress(long percent);

        void onFailed(String message);
    }

    public interface FileListener {
        void onSuccess(File file, String url);

        void onProgress(long percent, File file);

        void onFailed(String message);
    }

}
