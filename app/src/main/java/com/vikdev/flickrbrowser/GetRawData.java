package com.vikdev.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK};


class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    private DownloadStatus mDownloadStatus;
    private final onDownloadComplete callback;

    interface onDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(onDownloadComplete callback) {
        mDownloadStatus = DownloadStatus.IDLE;
        this.callback = callback;
    }

    void runInSameThread(String s) {
        Log.d(TAG, "runInSameThread: starts");

        onPostExecute(doInBackground(s));

        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected void onPostExecute(String s) {
        // Log.d(TAG, "onPostExecute: parameter = " + s);
        if (callback != null) {
            callback.onDownloadComplete(s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;

        if (strings == null) {
            this.mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            this.mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int response = httpURLConnection.getResponseCode();
            Log.d(TAG, "doInBackground: Response code = " + response);

            StringBuilder stringBuilder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            while (null != (line = reader.readLine())) {
                stringBuilder.append(line).append("\n");
            }
            this.mDownloadStatus = DownloadStatus.OK;
            return stringBuilder.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground : Invalid url" + e.getMessage());
        } catch (IOException io) {
            Log.e(TAG, "doInBackground: IOException" + io.getMessage());
        } catch (SecurityException se) {
            Log.e(TAG, "doInBackground: need internet permission " + se.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error in downloading " + e.getMessage());
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
