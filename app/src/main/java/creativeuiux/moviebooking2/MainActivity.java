package creativeuiux.moviebooking2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import adapter.ComingSoon_RecycleviewAdapter;
import adapter.TvAdapter;
import modalclass.CoomingSoonModalClass;
import modalclass.TvModel;

public class MainActivity extends AppCompatActivity {


    Spinner sp_city;

    RecyclerView theaters_recycleview;
    RecyclerView comingsoon_recycleview;

    TvAdapter tvadapter;
    ComingSoon_RecycleviewAdapter comingsoon_recycleviewAdapter;

    private ArrayList<TvModel> listtv =new ArrayList<>();;
    private ArrayList<CoomingSoonModalClass> comingSoonArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner
        sp_city = (Spinner) findViewById(R.id.sp_city);

        //Recycleview
        theaters_recycleview = (RecyclerView) findViewById(R.id.theaters_recycleview);


        // use of spinner // city
        List<String> list = new ArrayList<String>();
        list.add("Vadodara");
        list.add("Ahmedabad");
        list.add("Anand");
        list.add("Borsad");

        //use of spinner data adapter
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinner_text,list);
        sp_city.setAdapter(dataAdapter);


        // Theater - adding data in array list

        gettvinter("https://fando.xyz/gettv.php?url=https://iptv-org.github.io/iptv/countries/id.m3u");



        //Use of Theater Adapter
        tvadapter = new TvAdapter(MainActivity.this,listtv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        theaters_recycleview.setLayoutManager(layoutManager1);
        theaters_recycleview.setAdapter(tvadapter);






    }

    private void gettvinter (String url){

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
                    }
                } catch (JSONException e) {

                    System.out.println("fando err " );
                    e.printStackTrace();
                }

                tvadapter.notifyDataSetChanged();
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
