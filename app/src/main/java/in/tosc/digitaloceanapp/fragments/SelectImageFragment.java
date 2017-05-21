package in.tosc.digitaloceanapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.tosc.digitaloceanapp.R;
import in.tosc.digitaloceanapp.activities.DropletCreateActivity;
import in.tosc.digitaloceanapp.adapters.ImageAdapter;
import in.tosc.digitaloceanapp.utils.RecyclerItemClickListener;
import in.tosc.doandroidlib.DigitalOcean;
import in.tosc.doandroidlib.api.DigitalOceanClient;
import in.tosc.doandroidlib.objects.Image;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SelectImageFragment extends Fragment {

    List<Image> imageList;
    ImageAdapter imageAdapter;
    RecyclerView imageRecyclerView;

    public SelectImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        DigitalOceanClient doClient = DigitalOcean.getDOClient(getContext().getSharedPreferences("DO", MODE_PRIVATE).getString("authToken",null));
        imageRecyclerView = (RecyclerView) view.findViewById(R.id.imageRecyclerVIew);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        imageRecyclerView.setAdapter(imageAdapter);




                doClient.getImages(1,100,"distribution").enqueue(new Callback<List<Image>>() {
                    @Override
                    public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                        imageList = response.body();
                        imageAdapter = new ImageAdapter(imageList,getContext());
                        imageRecyclerView.setAdapter(imageAdapter);
                        Log.e("Droplets fetched", String.valueOf(imageList.size()));
                    }

                    @Override
                    public void onFailure(Call<List<Image>> call, Throwable t) {
                        Log.e("Failed getting images",t.getLocalizedMessage());
                    }
                });



        imageRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(View view, int position) {
                view.setBackground(getContext().getDrawable(R.drawable.selector));
                DropletCreateActivity.getDroplet().setImage(imageList.get(position));

                Log.e("OnClick",imageList.get(position).getDistribution());
            }
        }));

        return view;
    }
}
