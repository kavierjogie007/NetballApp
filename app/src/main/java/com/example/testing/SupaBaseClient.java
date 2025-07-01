package com.example.testing;

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
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5kZ3NnZmhscWRkbnd0YXF4a2VuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTEyMDY4ODMsImV4cCI6MjA2Njc4Mjg4M30.05iDZzTd7u8xCjXIYniCHt7STPzQavwJLd0G638H1Sc";
    private OkHttpClient client;
    public SupaBaseClient()
    {
    client =new OkHttpClient();
    }

    public String loginCoach(String username, String password) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Use PostgREST filter syntax
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
            return response.body().string(); // Will return a JSON array
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


