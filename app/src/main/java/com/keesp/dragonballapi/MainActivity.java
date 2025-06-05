package com.keesp.dragonballapi;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CharacterAdapter adapter;
    private ArrayList<Character> characterList;
    private ArrayList<Character> originalList;
    private Spinner spinnerFilter;
    private final String CHARACTERS_URL = "https://dragonball-api.com/api/characters?limit=58";
    private final String PLANETS_URL = "https://dragonball-api.com/api/planets?limit=20";
    private boolean showingCharacters = true;

    private SearchView searchView;
    private ImageView img_goku;
    private ImageView img_no_internet;
    private NetworkChangeReceiver networkReceiver;
    private boolean dataLoaded = false;


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
        searchView = findViewById(R.id.search_view);
        img_goku = findViewById(R.id.goku);
        img_no_internet = findViewById(R.id.no_internet);

        spinnerFilter = findViewById(R.id.spinner_filter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SPINNER_DEBUG", "Opción seleccionada: " + position);
                if (position == 0) {
                    // Personajes
                    showingCharacters = true;
                    fetchCharacters();
                } else {
                    // Planetas
                    showingCharacters = false;
                    List<Planet> planetList = new ArrayList<>();
                    PlanetAdapter adapter = new PlanetAdapter(MainActivity.this, planetList);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2)); // o Linear
                    recyclerView.setAdapter(adapter);

                    fetchPlanets();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        networkReceiver = new NetworkChangeReceiver(isConnected -> {
            if (isConnected && !dataLoaded) {
                Toast.makeText(MainActivity.this, "Conexión restablecida. Cargando personajes...", Toast.LENGTH_SHORT).show();
                fetchCharacters();
            }
        });

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    private void fetchCharacters() {
        if (!isNetworkAvailable()) {
            img_goku.setVisibility(ImageView.VISIBLE);
            img_no_internet.setVisibility(ImageView.VISIBLE);
            return;
        }else{
            img_goku.setVisibility(ImageView.GONE);
            img_no_internet.setVisibility(ImageView.GONE);
        }

        characterList = new ArrayList<>();
        originalList = new ArrayList<>();
        adapter = new CharacterAdapter(this, characterList);
        recyclerView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CHARACTERS_URL, null,
                response -> {
                    try {
                        characterList.clear();
                        originalList.clear();
                        JSONArray items = response.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            Character character = new Character(
                                    item.getInt("id"),
                                    item.getString("name"),
                                    item.getString("image"),
                                    item.getString("description"),
                                    item.getString("race"),
                                    item.getString("ki"),
                                    item.getString("maxKi"),
                                    item.getString("gender")
                            );
                            characterList.add(character);
                            originalList.add(character);
                        }
                        adapter.notifyDataSetChanged();
                        dataLoaded = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);
    }

    private void filterList(String text) {
        characterList.clear();
        if (text.isEmpty()) {
            characterList.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (Character character : originalList) {
                if (character.getName().toLowerCase().contains(text)) {
                    characterList.add(character);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchPlanets() {
        if (!isNetworkAvailable()) {
            img_goku.setVisibility(ImageView.VISIBLE);
            img_no_internet.setVisibility(ImageView.VISIBLE);
            return;
        } else {
            img_goku.setVisibility(ImageView.GONE);
            img_no_internet.setVisibility(ImageView.GONE);
        }

        List<Planet> planetList = new ArrayList<>();
        PlanetAdapter adapter = new PlanetAdapter(this, planetList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // o Linear
        recyclerView.setAdapter(adapter);


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PLANETS_URL, null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("items");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            String description = obj.getString("description");
                            String image = obj.getString("image");
                            boolean isDestroyed = obj.getBoolean("isDestroyed");
                            ArrayList<Character> characters = new ArrayList<>();

                            planetList.add(new Planet(id, name, description, image, isDestroyed, characters));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }
        );
        queue.add(request);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }

}