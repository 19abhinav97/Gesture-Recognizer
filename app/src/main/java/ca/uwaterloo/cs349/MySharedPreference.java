package ca.uwaterloo.cs349;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MySharedPreference {
    SharedPreferences sharedpreferences;
    final String pre_name = "pre_name";
    SharedPreferences.Editor editor;

    public MySharedPreference(Context context) {
        sharedpreferences = context.getSharedPreferences(pre_name, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public void setLibrary(ArrayList<GestureItem> arrayList) {
        String json = new Gson().toJson(arrayList);
        editor.putString("libraries", json);
        editor.commit();
    }


    public ArrayList<GestureItem> getLibraries() {
        String serializedObject = sharedpreferences.getString("libraries", null);
        if (serializedObject == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<GestureItem>>() {}.getType();
        return new Gson().fromJson(serializedObject, type);
    }
}
