package com.example.natan.movietralierapp1.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.natan.movietralierapp1.Respository;
import com.example.natan.movietralierapp1.model.Result;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {

    LiveData<List<Result>> mData;

    private Respository mRespository;


    public DetailViewModel(@NonNull Application application) {
        super(application);
        //mRespository = new Respository(application);
    }

    public void insert(Result result) {
        mRespository.insert(result);
    }

    public LiveData<List<Result>> getAllFav() {
        return mData;
    }


}