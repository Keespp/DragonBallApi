package com.keesp.dragonballapi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class DetailActivity extends AppCompatActivity {

    private ImageView characterImage, originPlanetImage;
    private TextView nameText, kiText, maxKiText, raceText, genderText,
            affiliationText, descriptionText, planetNameText, planetDescText, planetStatusText,characterTransformations;
    private RecyclerView transformationRecycler;
    private TransformationAdapter transformationAdapter;
    private List<Transformation> transformationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Inicializar vistas
        characterImage = findViewById(R.id.character_image);
        originPlanetImage = findViewById(R.id.planet_image);
        nameText = findViewById(R.id.character_name);
        kiText = findViewById(R.id.character_ki);
        maxKiText = findViewById(R.id.character_max_ki);
        characterTransformations = findViewById(R.id.character_transformations);

        raceText = findViewById(R.id.character_race);
        genderText = findViewById(R.id.character_gender);
        affiliationText = findViewById(R.id.character_affiliation);
        descriptionText = findViewById(R.id.character_description);
        planetNameText = findViewById(R.id.planet_name);
        planetDescText = findViewById(R.id.planet_description);
        planetStatusText = findViewById(R.id.planet_status);

        transformationRecycler = findViewById(R.id.transformations_recycler);
        transformationAdapter = new TransformationAdapter(this, transformationList);
        transformationRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        transformationRecycler.setAdapter(new TransformationAdapter(this, transformationList));


        int characterId = getIntent().getIntExtra("character_id", -1);
        if (characterId != -1) {
            fetchCharacterDetails(characterId);
        } else {
            Toast.makeText(this, "ID de personaje no válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCharacterDetails(int id) {
        String url = "https://dragonball-api.com/api/characters/" + id;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        nameText.setText(response.getString("name"));
                        kiText.setText("Ki: " + response.getString("ki"));
                        maxKiText.setText("Max Ki: " + response.getString("maxKi"));
                        raceText.setText("Raza: " + response.getString("race"));
                        genderText.setText("Género: " + response.getString("gender"));
                        affiliationText.setText("Afiliación: " + response.getString("affiliation"));
                        descriptionText.setText(response.getString("description"));

                        Glide.with(this)
                                .load(response.getString("image"))
                                .into(characterImage);

                        // Cargar info del planeta de origen
                        if (!response.isNull("originPlanet")) {
                            JSONObject planet = response.getJSONObject("originPlanet");
                            planetNameText.setText(planet.getString("name"));
                            planetDescText.setText(planet.getString("description"));
                            boolean isDestroyed = planet.getBoolean("isDestroyed");
                            planetStatusText.setText("¿Destruido?: " + (isDestroyed ? "Sí" : "No"));

                            Glide.with(this)
                                    .load(planet.getString("image"))
                                    .into(originPlanetImage);
                        }

                        if (!response.isNull("transformations")) {
                            JSONArray transformationsArray = response.getJSONArray("transformations");
                            for (int i = 0; i < transformationsArray.length(); i++) {
                                JSONObject transformationObject = transformationsArray.getJSONObject(i);
                                String transName = transformationObject.getString("name");
                                String transImage = transformationObject.getString("image");
                                String transKi = transformationObject.getString("ki");
                                transformationList.add(new Transformation(transName,transKi,transImage));
                            }
                            if (transformationList.isEmpty()) {
                                characterTransformations.setVisibility(View.GONE);
                            }
                            transformationAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}
