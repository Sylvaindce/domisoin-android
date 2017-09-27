package com.sylvain.domisoin.Utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Login extends AsyncTask<Void, Integer, Void>
{
    private Context _ourContext = null;
    private String _login = null;
    private String _password = null;


    public Login(Context ourContext, String login, String password) {
        _ourContext = ourContext;
        _login = login;
        _password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(_ourContext, "debut async", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        super.onProgressUpdate(values);
        // Mise à jour de la ProgressBar
        Log.v("Login task", String.valueOf(values[0]));
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        int progress;
        for (progress=0;progress<=100;progress++)
        {
            for (int i=0; i<1000000; i++){}
            //mise a jour status
            publishProgress(progress);
            progress++;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Toast.makeText(_ourContext, "tache terminé", Toast.LENGTH_LONG).show();
    }
}