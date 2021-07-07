package com.example.RestaurantManagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class UnitFirebase extends AsyncTask<Void, String, String> {

    Context context = null;

    public UnitFirebase(UnitFirebase instance, Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... Void) {

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }
}
