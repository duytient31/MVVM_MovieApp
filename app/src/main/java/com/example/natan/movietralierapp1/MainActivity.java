package com.example.natan.movietralierapp1;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.natan.movietralierapp1.Adapter.Movie;
import com.example.natan.movietralierapp1.Adapter.RecyclerMovie;
import com.example.natan.movietralierapp1.Network.NetworkUtils;
import com.example.natan.movietralierapp1.ViewModel.MainViewModel;
import com.example.natan.movietralierapp1.database.RemoteNetworkCall;
import com.example.natan.movietralierapp1.model.Example;
import com.example.natan.movietralierapp1.model.Result;
import com.example.natan.movietralierapp1.service.ApiClient;
import com.example.natan.movietralierapp1.service.ApiInterface;
import com.facebook.stetho.Stetho;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView)
    RecyclerView mrecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private int selected = 0;

    public static final String EXTRA_ANIMAL_IMAGE_TRANSITION_NAME = "animal_image_transition_name";

    private RecyclerMovie mRecyclerMovie;
    MainViewModel viewModel;

    // onSaveinstance varibale

    private final static String MENU_SELECTED = "selected";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 2);


        mrecyclerView.setLayoutManager(mLayoutManager);
        mrecyclerView.setItemAnimator(new DefaultItemAnimator());
        mrecyclerView.setNestedScrollingEnabled(false);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        if (savedInstanceState == null) {
            populateUI(selected);

        }
        else
        {
            selected = savedInstanceState.getInt(MENU_SELECTED);
            populateUI(selected);
        }







      /*  viewModel.mLiveData().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> results) {
                setupRecyclerView(results);
            }
        });*/


    }

    private void populateUI(int i) {


        viewModel.mLiveData().removeObservers(this);
       // viewModel.mLiveDataFav().removeObservers(this);

        switch (i) {
            case 0:

                viewModel.mLiveData().observe(this, new Observer<List<Result>>() {
                    @Override
                    public void onChanged(@Nullable List<Result> results) {
                        setupRecyclerView(results);
                    }
                });

                break;

            case 1:

                viewModel.mLiveDataFav().observe(this, new Observer<List<Result>>() {
                    @Override
                    public void onChanged(@Nullable List<Result> results) {
                        setupRecyclerView(results);
                    }
                });


        }


    }

    private void setupRecyclerView(List<Result> results) {

        if (results != null) {

            mRecyclerMovie = new RecyclerMovie(MainActivity.this, results, new RecyclerMovie.ListItemClickListener() {
                @Override
                public void onListItemClick(Result movie) {

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("data", movie);
                    startActivity(intent);


                }
            });


            mrecyclerView.setAdapter(mRecyclerMovie);
            mRecyclerMovie.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "List Null", Toast.LENGTH_SHORT).show();
        }


    }

    //onsaveInstanceState

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MENU_SELECTED, selected);
        super.onSaveInstanceState(outState);
    }


    // For menu settings

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.highest_Rated:
                //build("top_rated");
                // loadDefault("popular");
                viewModel.getTopRated();
                selected = 0;

                break;

            case R.id.most_popular:
                //build("popular");
                //loadDefault("top_rated");
                viewModel.getPopular();
                selected = 0;
                break;

            case R.id.fav:


                viewModel.getFavData();
                selected = 1;
                populateUI(selected);

                break;
        }

        return super.onOptionsItemSelected(item);
    }


}