package tvonline.indotv;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.TvAdapter;
import cn.pedant.SweetAlert.SweetAlertDialog;
import modalclass.TvModel;

public class MainActivity extends AppCompatActivity {


    Spinner sp_city;
    RecyclerView theaters_recycleview;
    TvAdapter tvadapter;
    SweetAlertDialog pDialog;

    androidx.appcompat.widget.SearchView searchView;
    private ArrayList<TvModel> listtv =new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Spinner
        sp_city = (Spinner) findViewById(R.id.sp_city);
        searchView=findViewById(R.id.searcview);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        //Recycleview
        theaters_recycleview = (RecyclerView) findViewById(R.id.theaters_recycleview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(query);
                gettvinter(Constant.BASEURL+"getsearch.php?q="+query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        // use of spinner // city
        List<String> list = new ArrayList<String>();
        list.add("International");
        list.add("Sports");
        list.add("Indonesia");
        list.add("Business");
        list.add("Entertainment");
        list.add("Music");

        //use of spinner data adapter
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinner_text,list);
        sp_city.setAdapter(dataAdapter);
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0){

                    gettvinter(Constant.BASEURL+"gettv.php?url=https://iptv-org.github.io/iptv/countries/int.m3u");
                }
                else if (position==1){
                    gettvinter(Constant.BASEURL+"gettv.php?url=https://iptv-org.github.io/iptv/categories/sport.m3u");
                }
                else if (position==2){
                    gettvinter(Constant.BASEURL+"gettv.php?url=https://iptv-org.github.io/iptv/countries/id.m3u");
                }
                else if (position==3){
                    gettvinter(Constant.BASEURL+"gettv.php?url=https://iptv-org.github.io/iptv/categories/business.m3u");
                }
                else if (position==4){
                    gettvinter(Constant.BASEURL+"gettv.php?url=https://iptv-org.github.io/iptv/categories/entertainment.m3u");
                }
                else if (position==5){
                    gettvinter(Constant.BASEURL+"gettv.php?url=https://iptv-org.github.io/iptv/categories/music.m3u");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Theater - adding data in array list
        gettvinter(Constant.BASEURL+"gettv.php?url=https://iptv-org.github.io/iptv/countries/id.m3u");
        //Use of Theater Adapter
        tvadapter = new TvAdapter(MainActivity.this,listtv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        theaters_recycleview.setLayoutManager(layoutManager1);
        theaters_recycleview.setAdapter(tvadapter);

    }

    private void gettvinter (String url){
        listtv.clear();
        theaters_recycleview.removeAllViews();

        pDialog.show();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("LIVETV");
                    for (int i =0; i < jsonArray.length() ;i++){
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                       TvModel tvModel= new TvModel();
                        tvModel.setNama(jsonObject.getString("title"));
                        tvModel.setCat(jsonObject.getString("group"));
                        tvModel.setLanguage(jsonObject.getString("language"));
                        tvModel.setUrl(jsonObject.getString("media_url"));
                        tvModel.setPoster(jsonObject.getString("thumb_square"));
                        listtv.add(tvModel);
                        System.out.println("suksess " );
                    }
                } catch (JSONException e) {
                    System.out.println("fando err " );
                    e.printStackTrace();
                }
                tvadapter.notifyDataSetChanged();
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                System.out.println("fando  rr" );
            }
        });


        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }




}
