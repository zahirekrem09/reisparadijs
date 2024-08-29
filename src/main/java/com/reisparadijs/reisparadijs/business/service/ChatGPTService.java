package com.reisparadijs.reisparadijs.business.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Rob Jansen
 * @project ReisParadijs
 * @created 7 Augustus 2024 - 14:00
 */

public class ChatGPTService {
    private static final String API_KEY = "sk-proj-dbpr6Pz5dMg7fPWEWrgKT3BlbkFJAXOhIygXJJSQ0FpBcJTc";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";


    public static String ChatGPTLocationRequest(String location){
        StringBuilder question = new StringBuilder();
        question.append("Geef 1 plaats in Nederland die wordt geassocieerd met: ");
        question.append(location);
        question.append(" en die ik kan bezoeken als toerist. Geef alleen de naam van de gemeente in Nederland terug");
        // question.append(location);
        // question.append(" een bestaande plaats in Nederland is, geef dan de plaats voor de opgegeven locatie terug ");
        return ChatGPTQuestion(question.toString());
    }

    public static String ChatGPTQuestion(String question) {
        String answer = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(question)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

            answer = extractAnswer(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer; // Return het antwoord
    }


    private static String buildRequestBody(String question) {
        JsonObject json = new JsonObject();
        json.addProperty("model", "gpt-4o-mini");

        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", question);
        messages.add(message);

        json.add("messages", messages);
        json.addProperty("max_tokens", 250);
        return json.toString();
    }

    private static String extractAnswer(JsonObject jsonResponse) {
        JsonArray choices = jsonResponse.getAsJsonArray("choices");
        if (choices != null && choices.size() > 0) {
            JsonObject firstChoice = choices.get(0).getAsJsonObject();
            JsonObject message = firstChoice.getAsJsonObject("message");
            return message.get("content").getAsString();
        }
        return null;
    }

}
