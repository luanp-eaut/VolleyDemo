package eaut.it.mobileappdev.volleydemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button xmlButton, jsonButton, imageButton;
    ImageView imageView;

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

        textView = findViewById(R.id.textView);
        xmlButton = findViewById(R.id.buttonXml);
        jsonButton = findViewById(R.id.buttonJson);
        imageButton = findViewById(R.id.buttonImage);
        imageView = findViewById(R.id.imageView);

        String xmlUrl = "https://www.w3schools.com/xml/note.xml";
        String jsonUrl = "https://filesamples.com/samples/code/json/sample2.json";
        String imageUrl = "https://cdn.pixabay.com/photo/2022/12/02/16/52/the-path-7631282_1280.jpg";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        xmlButton.setOnClickListener(view -> {
            StringRequest stringRequest = getXmlStringRequest(xmlUrl);
            requestQueue.add(stringRequest);
        });

        jsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonObjectRequest = getJsonObjectRequest(jsonUrl);
                requestQueue.add(jsonObjectRequest);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageRequest imageRequest = getImageRequest(imageUrl);
                requestQueue.add(imageRequest);
            }
        });
    }

    private StringRequest getXmlStringRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> textView.setText(response), error -> textView.setText(error.toString()));
        return stringRequest;
    }

    private JsonObjectRequest getJsonObjectRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> textView.setText(response.toString()), error -> textView.setText(error.toString()));
        return jsonObjectRequest;
    }

    private ImageRequest getImageRequest(String url) {
        int max_height = 1500;
        int max_width = 1500;

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                textView.setText("Photo from pixabay");
                imageView.setImageBitmap(response);
            }
        }, max_width, max_height, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText(error.toString());
            }
        });

        return imageRequest;
    }

    private JsonObjectRequest getPostRequest(String url, Map<String, String> params) {
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(params),
                response -> Log.d("Response", response.toString()),
                error -> Log.e("Error", error.toString())
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        return postRequest;
    }
}