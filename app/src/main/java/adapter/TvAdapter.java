package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import tvonline.indotv.PlayerActivity;
import tvonline.indotv.R;
import modalclass.TvModel;

import static tvonline.indotv.SplashActivity.admobappid;
import static tvonline.indotv.SplashActivity.interadmob;


public class TvAdapter extends RecyclerView.Adapter<TvAdapter.MyViewHolder> {

    Context context;
    private List<TvModel> listtv;
    SweetAlertDialog pDialog;

    InterstitialAd mInterstitialAd;


    public TvAdapter(Context mainActivityContacts, List<TvModel> listtv) {
        this.listtv = listtv;
        this.context = mainActivityContacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thaters, parent, false);

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);


        return new MyViewHolder(itemView);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TvModel modalClass = listtv.get(position);
        Glide.with(context)
                .load(modalClass.getPoster())
                .centerCrop()
                .placeholder(R.drawable.tv)
                .into(holder.poster_image);
        holder.poster_name.setText(modalClass.getNama());
        holder.language.setText(modalClass.getLanguage());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TvModel tvModel=listtv.get(position);
                Intent i = new Intent(context, PlayerActivity.class);
                i.putExtra("title",tvModel.getNama());
                i.putExtra("poster",tvModel.getPoster());
                i.putExtra("url",tvModel.getUrl());
                i.putExtra("lang",tvModel.getLanguage());
                i.putExtra("cat",tvModel.getCat());

                interadmobload(i);



            }
        });




    }

    @Override
    public int getItemCount() {

        return listtv.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView poster_name, poster_like, language;
        ImageView poster_image;
        LinearLayout item;


        public MyViewHolder(View view) {
            super(view);


            poster_name = (TextView) view.findViewById(R.id.poster_name);
            poster_like = (TextView) view.findViewById(R.id.poster_like);
            language = (TextView) view.findViewById(R.id.language);
            poster_image = (ImageView) view.findViewById(R.id.poster1);
            item=view.findViewById(R.id.item);


        }

    }

    public  void interadmobload(Intent intent){
        pDialog.show();
        MobileAds.initialize(context,
                admobappid);
        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(context);
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
                context.startActivity(intent);


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
                context.startActivity(intent);


                // Code to be executed when the interstitial ad is closed.
            }
        });


    }


}
