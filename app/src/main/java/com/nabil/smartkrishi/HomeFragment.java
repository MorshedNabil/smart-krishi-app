package com.nabil.smartkrishi;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    ArrayList<NewsItem> newsListHome = new ArrayList<>();
    SunPathView sunView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View myView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = myView.findViewById(R.id.news_recycler);
        sunView = myView.findViewById(R.id.dotted_curve);

        fetchNewsData();
        NewsAdapter myAdapter = new NewsAdapter(newsListHome);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        // ========================= Sun UI ===========================
        float progress = updateSunUI("06:30", "18:30");
        animateSun(progress);

        return myView;
    }


    // =============================== News Maker Function =======================
    private void fetchNewsData() {
        String url = "https://gist.githubusercontent.com/MorshedNabil/ad07f12a4511bc46429b4784666ee1c4/raw/260e5d0e27c2bafd55783e6e8c60dad3c5f71d17/myNews.json";
        RequestQueue queue = Volley.newRequestQueue(getActivity());


        JsonArrayRequest jsonNewsRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    newsListHome.clear();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject newsObject = response.getJSONObject(i);
                        //Log.d("JSONReq", "JSON object: " + response.toString());

                        // Extract data from the JSON object
                        String title = newsObject.getString("news_title");
                        String description = newsObject.getString("news_description");
                        String date = newsObject.getString("news_date");
                        String imageUrl = newsObject.getString("news_image_url");

                        // Create a NewsItem object
                        NewsItem item = new NewsItem(title, description, date, imageUrl);

                        // Add the object to your list
                        newsListHome.add(item);
                    }

                    // CRITICAL FIX: Notify the adapter AFTER the list is updated
                    if (recyclerView.getAdapter() != null) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    Log.e("JSONReqError", "JSON parsing error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("VolleyError", "Volley error: " + error.toString());
                // Optionally, show a toast message to the user
                Toast.makeText(getActivity(), "Failed to load news", Toast.LENGTH_SHORT).show();
            }
        }){

            // *** THIS IS THE FIX FOR THE 403 FORBIDDEN ERROR ***
            @Override
            public java.util.Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };

        queue.add(jsonNewsRequest);
    }
// ======================================================================

    private float updateSunUI(String sunriseTime, String sunsetTime) {
        // Parse strings like "06:30"
        LocalTime rise = LocalTime.parse(sunriseTime);
        LocalTime set = LocalTime.parse(sunsetTime);
        LocalTime now = LocalTime.now();

        if (now.isBefore(rise)) {
            return 0f;
        } else if (now.isAfter(set)) {
            return 1f;
        } else {
            long totalDuration = Duration.between(rise, set).toMinutes();
            long currentPassed = Duration.between(rise, now).toMinutes();
            float ratio = (float) currentPassed / totalDuration;
            return ratio;
        }
    }

    public void animateSun(float targetProgress) {
        // ValueAnimator smoothly transitions a value from start to end
        ValueAnimator animator = ValueAnimator.ofFloat(0f, targetProgress);
        animator.setDuration(4000); // 2 seconds for a smooth glide
        animator.setInterpolator(new DecelerateInterpolator()); // Slows down as it reaches the end

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                sunView.setProgress(animatedValue);
            }
        });

        animator.start();
    }
}