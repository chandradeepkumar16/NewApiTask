package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.news.Authentication.LoginActivity;
import com.example.news.Authentication.SignupActivity;
import com.example.news.ModelClasses.ArticlesItem;
import com.example.news.ModelClasses.MainResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView newsRecycler;
    private Retrofit retrofit;
    private NewsInterface newsInterface;
    private final String generalCategory="general";

    private SearchView searchView;
    List<ArticlesItem> news;
    NewsAdapter adapter;
    private FirebaseAuth mAuth;

//    private BottomNavigationView bottomNavigationView;
//    private ProgressBar progressBar;


    private ActionBar actionBar;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRecycler=findViewById(R.id.newsrecycle);
        searchView=findViewById(R.id.searchview);
        searchView.clearFocus();


        mAuth=FirebaseAuth.getInstance();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                filterlist(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterlist(newText);
                return true;
            }
        });


        newsRecycler.setVisibility(View.INVISIBLE);
        actionBar=getSupportActionBar();
        actionBar.setTitle("General");
        setNewsRetrofit(generalCategory);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        logoutfun();
        return true;
    }

    private void logoutfun() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }


    private void setNewsRetrofit(String category)
    {

        retrofit= new Retrofit.Builder().baseUrl("https://newsapi.org/").addConverterFactory(GsonConverterFactory.create()).build();
        newsInterface=retrofit.create(NewsInterface.class);
        Call<MainResponse> responsecall=newsInterface.getNewsData(category);
        responsecall.enqueue(new Callback<MainResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if(response.isSuccessful())
                {
                    MainResponse mainResponse=response.body();
                    news=mainResponse.getArticles();

                    newsRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    adapter= new NewsAdapter(MainActivity.this,news);
                    newsRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    newsRecycler.setVisibility(View.VISIBLE);
                }
                else
                {
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {

            }
        });
    }



    private void filterlist(String text) {

        List<ArticlesItem> filteredlist=new ArrayList<>();

        for(ArticlesItem articlesItem:news){
            if(articlesItem.getDescription().toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(articlesItem);
            }
        }

        if(filteredlist.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{

            adapter.setData(filteredlist);
        }

    }
}