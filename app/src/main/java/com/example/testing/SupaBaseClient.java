package com.example.testing;

import com.google.gson.Gson;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class SupaBaseClient {
    private static final String SUPABASE_BASE_URL = "https://ndgsgfhlqddnwtaqxken.supabase.co/rest/v1/";
    private static final String SUPABASE_COACH_URL = SUPABASE_BASE_URL + "coach";
    private static final String SUPABASE_PLAYER_URL = SUPABASE_BASE_URL + "player";
    //Required for authorization to access your Supabase instance
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5kZ3NnZmhscWRkbnd0YXF4a2VuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTEyMDY4ODMsImV4cCI6MjA2Njc4Mjg4M30.05iDZzTd7u8xCjXIYniCHt7STPzQavwJLd0G638H1Sc";
    //Used to send HTTP requests
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;
    private Gson gson;
    public SupaBaseClient()
    {
    client =new OkHttpClient();
    gson = new Gson();
    }

    // Login Coach - returns Coach object or null
    public Coach loginCoach(String username, String password) {
        String filterUrl = SUPABASE_COACH_URL + "?coach_username=eq." + username + "&coach_password=eq." + password;

        Request request = new Request.Builder()
                .url(filterUrl)
                .get()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Login failed: " + response);
            }

            String responseBody = response.body().string();
            Coach[] coaches = gson.fromJson(responseBody, Coach[].class);
            return coaches.length > 0 ? coaches[0] : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Register Coach - returns Coach object or null
    public Coach registerCoach(Coach coach) {
        String json = gson.toJson(coach); // Converts Coach object to JSON
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(SUPABASE_COACH_URL)
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
            {
                throw new IOException("Register failed: " + response);
            }

            String responseBody = response.body().string();
            Coach[] coaches = gson.fromJson(responseBody, Coach[].class);
            return coaches.length > 0 ? coaches[0] : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get Coach by ID - returns Coach object or null
    public Coach getCoachById(long coachId) {
        String url = SUPABASE_COACH_URL + "?coach_ID=eq." + coachId;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch coach: " + response);
            }
            String responseBody = response.body().string();
            Coach[] coaches = gson.fromJson(responseBody, Coach[].class);
            return coaches.length > 0 ? coaches[0] : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update Coach Profile - returns updated Coach object or null
    public Coach updateCoachProfile(long coachId, Coach updatedCoach) {
        String json = gson.toJson(updatedCoach);
        RequestBody body = RequestBody.create(json, JSON);

        // Filter by coach_ID
        String updateUrl = SUPABASE_COACH_URL + "?coach_ID=eq." + coachId;

        Request request = new Request.Builder()
                .url(updateUrl)
                .method("PATCH", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Update failed: " + response);
            }
            String responseBody = response.body().string();
            Coach[] coaches = gson.fromJson(responseBody, Coach[].class);
            return coaches.length > 0 ? coaches[0] : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


