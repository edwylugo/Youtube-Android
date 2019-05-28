package edwy.lugo.youtube.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import edwy.lugo.youtube.R;
import edwy.lugo.youtube.adapter.AdapterVideo;
import edwy.lugo.youtube.api.YoutubeService;
import edwy.lugo.youtube.helper.RetrofitConfig;
import edwy.lugo.youtube.helper.YoutubeConfig;
import edwy.lugo.youtube.listener.RecyclerItemClickListener;
import edwy.lugo.youtube.model.Item;
import edwy.lugo.youtube.model.Resultado;
import edwy.lugo.youtube.model.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    //Widgets
    private RecyclerView recyclerVideos;
    private List<Item>  videos = new ArrayList<>();
    private Resultado resultado;
    private AdapterVideo adapterVideo;
    private MaterialSearchView searchView;

    //Retrofit
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializar componentes
        recyclerVideos = findViewById(R.id.recyclerVideos);
        searchView = findViewById(R.id.searchView);

        //Configurações iniciais
        retrofit = RetrofitConfig.getRetrofit();

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        setSupportActionBar(toolbar);

        //Recupera Vídeos
        recuperarVideos("");


        //Configura métodos para SearchView;
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                    recuperarVideos("");
            }
        });

    }

    private void recuperarVideos(String pesquisa){


        String q = pesquisa.replaceAll(" ", "+");
        YoutubeService youtubeService = retrofit.create(YoutubeService.class);

        youtubeService.recuperarVideos(
                "snippet","date","20", YoutubeConfig.CHAVE_YOUTUBE_API,YoutubeConfig.CANAL_ID, q
        ).enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                Log.d("resultado","resultado: " + response.toString());

                if (response.isSuccessful()) {
                    resultado = response.body();
                    videos = resultado.items;
                    configurarRecyclerView();

//                    Log.d("resultado","regionCode: " + resultado.regionCode);
//                    Log.d("resultado","totalResults: " + resultado.pageInfo.totalResults);
//                    Log.d("resultado","title: " + resultado.items.get(0).snippet.title);
//                    Log.d("resultado","channelId: " + resultado.items.get(0).snippet.channelId);
//                    Log.d("resultado","channelTitle: " + resultado.items.get(0).snippet.channelTitle);
//                    Log.d("resultado","description: " + resultado.items.get(0).snippet.description);
//                    Log.d("resultado","liveBroadcastContent: " + resultado.items.get(0).snippet.liveBroadcastContent);
//                    Log.d("resultado","kind: " + resultado.items.get(0).id.kind);
//                    Log.d("resultado","videoId: " + resultado.items.get(0).id.videoId);


                }

            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

            }
        });

    }

    public void configurarRecyclerView(){
        adapterVideo = new AdapterVideo(videos,this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideo);

        //Configura evento de clique
        recyclerVideos.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerVideos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Item video = videos.get(position);
                String idVideo = video.id.videoId;

                Intent i = new Intent(MainActivity.this,PlayerActivity.class);
                i.putExtra("idVideo",idVideo);
                startActivity(i);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);

        return true;
    }

}
