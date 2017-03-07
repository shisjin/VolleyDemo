package com.shisjin.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sang.a9_2volley.R;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private TextView tv1;
    private String url = "http://www.csdn.net";
    private ImageView iv;
    private NetworkImageView niv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = ((MyApp) getApplication()).getQueue();
        tv1 = ((TextView) findViewById(R.id.tv1));
        iv = ((ImageView) findViewById(R.id.iv));
        niv = ((NetworkImageView) findViewById(R.id.niv));
        niv.setImageUrl("http://n.sinaimg.cn/auto/crawl/20161206/aVNZ-fxyiayt5814620.jpg", new ImageLoader(queue, new BitmapCache(this)));
    }

    public void btnClick1(View view) {
        StringRequest request = new StringRequest(Request.Method.GET, url,
        //StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tv1.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("google.sang", "onErrorResponse: "+error.getMessage());
                Cache.Entry entry = queue.getCache().get(url);
                if (entry == null) {
                    return;
                }
                byte[] data = entry.data;
                if (data != null && data.length > 0) {
                    tv1.setText(new String(data, 0, data.length));
                }
            }
        }); /*{
            //Post请求传参方式
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", "zs");
                return map;
            }
           */
       // };
        queue.add(request);
    }

    public void btnClick2(View view) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://www.tngou.net/api/food/classify", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                StringBuffer result = new StringBuffer();
                try {
                    JSONArray tngou = response.getJSONArray("tngou");
                    for (int i = 0; i < tngou.length(); i++) {
                        String name = tngou.getJSONObject(i).getString("name");
                        result.append(name).append("\n");
                    }
                    tv1.setText(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void btnClick3(View view) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "http://api.iclient.ifeng.com/ClientNews?id=SYLB10,SYDT10,SYRECOMMEND&page=1&newShowType=1&province=%E5%B9%BF%E4%B8%9C%E7%9C%81&city=%E5%B9%BF%E5%B7%9E%E5%B8%82&district=%E5%A4%A9%E6%B2%B3%E5%8C%BA&gv=5.2.0&av=5.2.0&uid=868192023562255&deviceid=868192023562255&proid=ifengnews&os=android_21&df=androidphone&vt=5&screen=1080x1920&publishid=6001", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void btnClick4(View view) {
        ImageRequest request = new ImageRequest("http://n.sinaimg.cn/auto/crawl/20161206/aVNZ-fxyiayt5814620.jpg", new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                iv.setImageBitmap(response);
            }
        }, 200, 200, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void btnClick5(View view) {
        ImageLoader loader = new ImageLoader(queue, new BitmapCache(this));
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(iv, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        loader.get("http://n.sinaimg.cn/auto/crawl/20161206/aVNZ-fxyiayt5814620.jpg", imageListener);
    }
}
