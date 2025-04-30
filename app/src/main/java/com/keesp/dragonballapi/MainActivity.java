package com.keesp.dragonballapi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CharacterAdapter adapter;
    private ArrayList<Character> characterList;
    private String url = "https://dragonball-api.com/api/characters";

    private int currentPage = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        characterList = new ArrayList<>();
        adapter = new CharacterAdapter(this, characterList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null &&
                        layoutManager.findLastCompletelyVisibleItemPosition() == characterList.size() - 1) {
                    currentPage++;
                    fetchCharacters();
                }
            }
        });

        fetchCharacters();
    }

    private void fetchCharacters() {
        isLoading = true;
        String paginatedUrl = url + "?page=" + currentPage;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, paginatedUrl, null,
                response -> {
                    try {
                        JSONArray items = response.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            characterList.add(new Character(
                                    item.getString("name"),
                                    item.getString("image"),
                                    item.getString("description"),
                                    item.getString("race"),
                                    item.getString("ki"),
                                    item.getString("maxKi"),
                                    item.getString("gender")
                            ));
                        }
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        isLoading = false;
                        Toast.makeText(MainActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    isLoading = false;
                    Toast.makeText(MainActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);
    }
}
