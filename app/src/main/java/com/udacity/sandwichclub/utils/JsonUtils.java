package com.udacity.sandwichclub.utils;

import android.util.JsonReader;

import com.udacity.sandwichclub.model.Sandwich;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

  public static Sandwich parseSandwichJson(String json) throws IOException {
    JsonReader reader = new JsonReader(new StringReader(json));
    try {
      return parseSandwich(reader);
    } finally {
      reader.close();
    }
  }

  private static List<String> parseStringList(JsonReader reader) throws IOException {
    ArrayList<String> values = new ArrayList<>();
    reader.beginArray();

    while (reader.hasNext()) {
      values.add(reader.nextString());
    }

    reader.endArray();
    return values;
  }

  private static Sandwich parseSandwich(JsonReader reader) throws IOException {
    Sandwich sandwich = new Sandwich();

    //        reader.peek() != JsonToken.NULL

    reader.beginObject();
    while (reader.hasNext()) {
      String key = reader.nextName();
      if (key.equals("name")) {
        // parse name object, which has mainName and array of strings alsoKnownAs
        reader.beginObject();
        while (reader.hasNext()) {
          String nameKey = reader.nextName();
          if (nameKey.equals("mainName")) {
            sandwich.setMainName(reader.nextString());
          } else if (nameKey.equals("alsoKnownAs")) {
            List<String> alsoKnownAs = parseStringList(reader);
            sandwich.setAlsoKnownAs(alsoKnownAs);
          }
        }
        reader.endObject();
      } else if (key.equals("placeOfOrigin")) {
        sandwich.setPlaceOfOrigin(reader.nextString());
      } else if (key.equals("description")) {
        sandwich.setDescription(reader.nextString());
      } else if (key.equals("image")) {
        sandwich.setImage(reader.nextString());
      } else if (key.equals("ingredients")) {
        List<String> ingredients = parseStringList(reader);
        sandwich.setIngredients(ingredients);
      } else {
        reader.skipValue();
      }
    }
    reader.endObject();
    return sandwich;
  }
}
