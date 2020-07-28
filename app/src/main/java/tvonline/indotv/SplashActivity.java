package tvonline.indotv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends AppCompatActivity {
    public static String statususer,statusapp,apkupdate,interadmob,banneradmob,admobappid;
    SweetAlertDialog pDialog;
    Button startbutton;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        getStatusapp(Constant.BASEURL+"getstatus.php");

    }

    private void getStatusapp(String url){

        pDialog.show();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                    JSONObject jsonObject=response.getJSONObject("status");
                    statususer = response.getString("status");
                    banneradmob = response.getString("banneradmob");
                    interadmob=response.getString("interadmob");
                    apkupdate=response.getString("apkupdate");
                    statusapp=response.getString("statusapp");
                    admobappid=response.getString("admobappid");
                    Button button= findViewById(R.id.startbutton);
                    pDialog.hide();
                    button.setVisibility(View.VISIBLE);

                    if (statusapp.equals("0")){
                        update();
                        button.setText("UPDATE");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                update();
                            }
                        });

                    }
                    else{
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                interadmobload();

                            }
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                System.out.println(url);

            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }

    private void update() {
        new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Update App")
                .setContentText("App Need To Update")
                .setConfirmText("Update")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("Update From Playstore")
                                .setContentText("Please Wait, Open Playstore")
                                .setConfirmText("Go")
                                .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(
                                        "https://play.google.com/store/apps/details?id=" + apkupdate));
                                intent.setPackage("com.android.vending");
                                startActivity(intent);
//                                Do something after 100ms
                            }
                        }, 3000);



                    }
                })

                .show();
    }


    public  void interadmobload(){
        pDialog.show();
        MobileAds.initialize(this,
                admobappid);
        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(interadmob);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                pDialog.hide();
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                pDialog.hide();
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);

                // Code to be executed when the interstitial ad is closed.
            }
        });


    }
}
