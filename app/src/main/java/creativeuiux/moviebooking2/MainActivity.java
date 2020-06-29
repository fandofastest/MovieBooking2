package creativeuiux.moviebooking2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import adapter.ComingSoon_RecycleviewAdapter;
import adapter.Theater_RecycleviewAdapter;
import modalclass.CoomingSoonModalClass;
import modalclass.TheaterModalClass;

public class MainActivity extends AppCompatActivity {


    Spinner sp_city;

    RecyclerView theaters_recycleview;
    RecyclerView comingsoon_recycleview;

    Theater_RecycleviewAdapter theater_recycleviewAdapter;
    ComingSoon_RecycleviewAdapter comingsoon_recycleviewAdapter;

    private ArrayList<TheaterModalClass> theaterArrayList;
    private ArrayList<CoomingSoonModalClass> comingSoonArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner
        sp_city = (Spinner) findViewById(R.id.sp_city);

        //Recycleview
        theaters_recycleview = (RecyclerView) findViewById(R.id.theaters_recycleview);
        comingsoon_recycleview = (RecyclerView) findViewById(R.id.comingsoon_recycleview);


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

        theaterArrayList = new ArrayList<>();
        theaterArrayList.add(new TheaterModalClass(R.drawable.venomposter,"Venom(2018)","Hindi, English","83%"));
        theaterArrayList.add(new TheaterModalClass(R.drawable.andhadhun,"Andhadhun","English","99%"));
        theaterArrayList.add(new TheaterModalClass(R.drawable.venomposter,"Venom(2018)","Hindi, English","78%"));
        theaterArrayList.add(new TheaterModalClass(R.drawable.andhadhun,"Andhadhun","Hindi","83%"));
        theaterArrayList.add(new TheaterModalClass(R.drawable.venomposter,"Venom(2018)","Hindi, English","90%"));
        theaterArrayList.add(new TheaterModalClass(R.drawable.andhadhun,"Andhadhun","English","67%"));

        //Use of Theater Adapter
        theater_recycleviewAdapter = new Theater_RecycleviewAdapter(MainActivity.this,theaterArrayList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        theaters_recycleview.setLayoutManager(layoutManager1);
        theaters_recycleview.setAdapter(theater_recycleviewAdapter);





        // Cooming Soon - adding data in array list

        comingSoonArrayList = new ArrayList<>();
        comingSoonArrayList.add(new CoomingSoonModalClass(R.drawable.starborn,"A Star is Born","Hindi, English","83%"));
        comingSoonArrayList.add(new CoomingSoonModalClass(R.drawable.creed2,"Creed II","English","99%"));
        comingSoonArrayList.add(new CoomingSoonModalClass(R.drawable.starborn,"A Star is Born","Hindi, English","78%"));
        comingSoonArrayList.add(new CoomingSoonModalClass(R.drawable.creed2,"Creed II","Hindi","83%"));
        comingSoonArrayList.add(new CoomingSoonModalClass(R.drawable.starborn,"A Star is Born","Hindi, English","90%"));
        comingSoonArrayList.add(new CoomingSoonModalClass(R.drawable.creed2,"Creed II","English","67%"));

        //Use of Coming Soon Adapter
        comingsoon_recycleviewAdapter = new ComingSoon_RecycleviewAdapter(MainActivity.this,comingSoonArrayList);

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(MainActivity.this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        comingsoon_recycleview.setLayoutManager(layoutManager2);
        comingsoon_recycleview.setAdapter(comingsoon_recycleviewAdapter);


    }
}
