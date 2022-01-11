package com.example.asynctask;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetImage().execute("https://images.chinatimes.com/newsphoto/2020-12-01/1024/20201201006541.jpg");
            }
        });

    }
    /** 要使用 AsyncTask，必定要建立一個繼承自 AsyncTask 的子類別，並傳入 3 項資料：
    Params -- 要執行 doInBackground() 時傳入的參數，數量可以不止一個
    Progress -- doInBackground() 執行過程中回傳給 UI thread 的資料，數量可以不止一個
    Rsesult -- 傳回執行結果， 若您沒有參數要傳入，則填入 Void (注意 V 為大寫)。
    **/
    private class GetImage extends AsyncTask<String, Integer, Bitmap>{
        ProgressDialog pd;

        @Override
        protected void onPreExecute() { //AsyncTask 執行前的準備工作，例如畫面上顯示進度表

            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("圖片載入中...");
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) { //實際要執行的程式碼就是寫在這裡
            double progress = 0;
            Bitmap bitmap;
            for(String urlStr: params)
            {
                try {
                    URL url = new URL(urlStr);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    publishProgress((int)(progress+=100/(double)params.length));
                    if(progress>=100){
                        Thread.sleep(1000);
                        return bitmap;
                    }

                }catch (Exception e){
                    Log.e("TAG", "wtf");
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) { //用來顯示目前的進度
            super.onProgressUpdate(values);
            pd.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) { //執行完的結果 - Result 會傳入這裡
            super.onPostExecute(bitmap);
            pd.dismiss();
            imageView.setImageBitmap(bitmap);
        }

    }
}