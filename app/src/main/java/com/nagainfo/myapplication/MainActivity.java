package com.nagainfo.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText serch_data;
    Button seach_btn;
    String search_val = "";
    Bitmap bit;
    String url = "http://www.omdbapi.com/?t=";
    ProgressDialog progressDialog;
    ImageView img;
    String title = "";
    String year = "";
    String img_url;
    TextView title_view;
    ImageLoader imageLoader;
    Data data;
    Bitmap bitmap_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final InsertData db = new InsertData(this);

        List<Data> saved_data = new ArrayList<Data>();

        seach_btn = (Button) findViewById(R.id.btn_search);
        serch_data = (EditText) findViewById(R.id.search);
        title_view = (TextView) findViewById(R.id.title_view);
        img = (ImageView) findViewById(R.id.im_view);
        seach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://www.omdbapi.com/?t=";
                search_val = serch_data.getText().toString();
                url = url + search_val;
                if (!isOnline()) {

//                    InsertData db =new InsertData(MainActivity.this);
                    List<Data> sav_data = db.getSavedData(search_val);
                    for (Data dt : sav_data) {
                        title_view.setText(dt.getTitle());
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(dt.getImg().getBytes(), 0, dt.getImg().length(), options);
                        img.setImageBitmap(bitmap);
                    }


                }
                getDataFromWeb(url);
            }
        });

    }

    private void getDataFromWeb(String url) {
        Log.i("url", url);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait");
        progressDialog.show();

        JsonObjectRequest job = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.hide();
                try {
                    data = new Data();
                    title = jsonObject.getString("Title").toString();
                    year = jsonObject.getString("Year").toString();
                    img_url = jsonObject.getString("Poster").toString();
                    data.setImg(img_url);
                    data.setTitle(title);
                    title_view.setText(title);
                    imageRequest(img_url);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("response", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error", volleyError.toString());
                progressDialog.hide();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(job);
    }

    private void imageRequest(String img_url) {

        ImageRequest imageRequest = new ImageRequest(img_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                img.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                InsertData db = new InsertData(MainActivity.this);
                db.addData(new Data(title, byteArray.toString()));

            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(imageRequest);
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
