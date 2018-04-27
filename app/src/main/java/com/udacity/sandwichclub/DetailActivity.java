package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];

        Sandwich sandwich = null;

        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (IOException exception) {
            closeOnError();
            return;
        }

        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
      setTextViewOrHide(
          findViewById(R.id.also_known_label),
          findViewById(R.id.also_known_tv),
          TextUtils.join(", ", sandwich.getAlsoKnownAs())
      );
      setTextViewOrHide(
          findViewById(R.id.origin_label),
          findViewById(R.id.origin_tv),
          sandwich.getPlaceOfOrigin()
      );

      TextView description = findViewById(R.id.description_tv);
      TextView ingredients = findViewById(R.id.ingredients_tv);
      description.setText(sandwich.getDescription());
      ingredients.setText(TextUtils.join(", ", sandwich.getIngredients()));
    }

    private void setTextViewOrHide(View labelTv, View valueTv, String value) {
      if (!value.equals("")) {
        ((TextView) valueTv).setText(value);
      } else {
        valueTv.setVisibility(View.GONE);
        labelTv.setVisibility(View.GONE);
      }
    }
}
