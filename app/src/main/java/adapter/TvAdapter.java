package adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;
import creativeuiux.moviebooking2.PlayerActivity;
import creativeuiux.moviebooking2.R;
import modalclass.TvModel;


public class TvAdapter extends RecyclerView.Adapter<TvAdapter.MyViewHolder> {

    Context context;
    private List<TvModel> listtv;


    public TvAdapter(Context mainActivityContacts, List<TvModel> listtv) {
        this.listtv = listtv;
        this.context = mainActivityContacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thaters, parent, false);



        return new MyViewHolder(itemView);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TvModel modalClass = listtv.get(position);
        Glide.with(context)
                .load(modalClass.getPoster())
                .centerCrop()
                .placeholder(R.drawable.andhadhun)
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
                context.startActivity(i);

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
}
