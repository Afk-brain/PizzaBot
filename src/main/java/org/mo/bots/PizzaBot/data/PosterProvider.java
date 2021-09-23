package org.mo.bots.PizzaBot.data;

import com.google.gson.Gson;
import org.mo.bots.PizzaBot.objects.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class PosterProvider implements DataProvider {

    private final String token = System.getenv("PosterToken");
    private final HttpClient client = HttpClient.newHttpClient();
    Gson gson = new Gson();

    @Override
    public User getUserByPhone(String phone) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("phone", phone);
            User user = request("clients.getClient", params, User.class);
            System.out.println(user.firstname);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private URI buildURI(String method, String params) throws URISyntaxException {
        URI uri = new URI("https://joinposter.com/api/" + method + "?token=" + token + params);
        System.out.println(uri);
        return uri;
    }

    private <T> T request(String method, Map<String, String> params, Class<T> tClass) throws Exception{
        String stringParams = "&";
        for(Map.Entry<String, String> entry : params.entrySet()) {
            stringParams += entry.getKey() + "=" + entry.getValue() + "&";
        }
        HttpRequest request = HttpRequest.newBuilder(buildURI("clients.getClients", stringParams)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        System.out.println(result);
        result = result.replaceAll("\\{\"response\":\\[", "");
        result = result.replaceAll("]}", "");
        return gson.fromJson(result, tClass);
    }
}
