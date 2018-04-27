package com.udacity.sandwichclub.utils;

import android.util.JsonReader;

import com.udacity.sandwichclub.model.Sandwich;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class JsonUtils {

  // referenced https://developer.android.com/reference/android/util/JsonReader

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

  private static NameJson parseNameJson(JsonReader reader) throws IOException {
    NameJson nameJson = new NameJson();

    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case "mainName":
          nameJson.setMainName(reader.nextString());
          break;
        case "alsoKnownAs":
          List<String> alsoKnownAs = parseStringList(reader);
          nameJson.setAlsoKnownAs(alsoKnownAs);
          break;
        default:
          reader.skipValue();
      }
    }
    reader.endObject();
    return nameJson;
  }

  private static Sandwich parseSandwich(JsonReader reader) throws IOException {
    Sandwich sandwich = new Sandwich();

    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case "name":
          NameJson nameJson = parseNameJson(reader);
          sandwich.setMainName(nameJson.getMainName());
          sandwich.setAlsoKnownAs(nameJson.getAlsoKnownAs());
          break;
        case "placeOfOrigin":
          sandwich.setPlaceOfOrigin(reader.nextString());
          break;
        case "description":
          sandwich.setDescription(reader.nextString());
          break;
        case "image":
          sandwich.setImage(reader.nextString());
          break;
        case "ingredients":
          List<String> ingredients = parseStringList(reader);
          sandwich.setIngredients(ingredients);
          break;
        default:
          reader.skipValue();
      }
    }
    reader.endObject();
    return sandwich;
  }

  private static class NameJson {
    private String mainName;
    private List<String> alsoKnownAs;

    String getMainName() {
      return mainName;
    }

    void setMainName(String mainName) {
      this.mainName = mainName;
    }

    List<String> getAlsoKnownAs() {
      return alsoKnownAs;
    }

    void setAlsoKnownAs(List<String> alsoKnownAs) {
      this.alsoKnownAs = alsoKnownAs;
    }
  }
}
