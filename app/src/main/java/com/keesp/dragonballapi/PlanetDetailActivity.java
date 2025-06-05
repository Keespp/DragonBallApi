package com.keesp.dragonballapi;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PlanetDetailActivity extends AppCompatActivity {

    private ImageView imagePlanet;
    private TextView textPlanetName, textPlanetDescription, isDestroyed;
    private RecyclerView recyclerViewCharacters;
    private CharacterAdapter characterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_detail);

        imagePlanet = findViewById(R.id.planet_image);
        textPlanetName = findViewById(R.id.planet_name);
        textPlanetDescription = findViewById(R.id.planet_description);
        recyclerViewCharacters = findViewById(R.id.recyclerViewCharacters);
        isDestroyed = findViewById(R.id.planet_status);

        recyclerViewCharacters.setLayoutManager(new GridLayoutManager(this,2));

        int planetId = getIntent().getIntExtra("planet_id", 1);
        fetchPlanetData(planetId);
    }

    private void fetchPlanetData(int planetId) {
        String url = "https://dragonball-api.com/api/planets/" + planetId;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        int id = response.getInt("id");
                        String name = response.getString("name");
                        boolean isDestroyed = response.getBoolean("isDestroyed");
                        String description = response.getString("description");
                        String imageUrl = response.getString("image");

                        JSONArray charactersJson = response.getJSONArray("characters");
                        List<Character> characterList = new ArrayList<>();

                        for (int i = 0; i < charactersJson.length(); i++) {
                            JSONObject obj = charactersJson.getJSONObject(i);
                            Character character = new Character(
                                    obj.getInt("id"),
                                    obj.getString("name"),
                                    obj.getString("image"),
                                    obj.getString("description"),
                                    obj.getString("race"),
                                    obj.getString("ki"),
                                    obj.getString("maxKi"),
                                    obj.getString("gender")
                            );
                            characterList.add(character);
                        }

                        textPlanetName.setText(name);
                        textPlanetDescription.setText(description);
                        if (isDestroyed) {
                            this.isDestroyed.setText("Planeta destruido");
                        } else {
                            this.isDestroyed.setText("Planeta vivo");
                        }
                        Glide.with(this).load(imageUrl).into(imagePlanet);

                        characterAdapter = new CharacterAdapter((Context) this, (ArrayList<Character>) characterList);
                        recyclerViewCharacters.setAdapter(characterAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al parsear datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }
}
