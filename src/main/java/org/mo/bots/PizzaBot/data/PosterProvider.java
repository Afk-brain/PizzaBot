package org.mo.bots.PizzaBot.data;

import com.google.gson.Gson;
import org.mo.bots.PizzaBot.objects.ClientGroup;
import org.mo.bots.PizzaBot.objects.Product;
import org.mo.bots.PizzaBot.objects.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PosterProvider implements DataProvider {

    private final String token = System.getenv("PosterTestToken");
    private final HttpClient client = HttpClient.newHttpClient();
    Gson gson = new Gson();

    @Override
    public User getUser(long id, String phone) {
        try {
            Map<String, String> params = new HashMap<>();
            if(id != 0) {
                params.put("id", id + "");
            } else {
                params.put("phone", phone);
            }
            String answer = request("clients.getClient", params);
            answer = answer.replaceAll("\\{\"response\":\\[", "");
            answer = answer.replaceAll("]}", "");
            User user =  gson.fromJson(answer, User.class);
            user.chatId = MySql.getTgId(user.phone);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product getProductById(int id) {
        return null;
    }

    @Override
    public List<ClientGroup> getClientGroups() {
        try {
            String answer = request("clients.getGroups", new HashMap<>());
            answer = answer.replaceAll("\\{\"response\":\\[", "");
            answer = answer.replaceAll("]}", "");
            String[] parts = answer.split("}.\\{");
            List<ClientGroup> groups = new ArrayList<>();
            for(int i = 0;i < parts.length;i++) {
                if(!parts[i].startsWith("{")) parts[i] = "{" + parts[i];
                if(!parts[i].endsWith("}")) parts[i] = parts[i] + "}";
                groups.add(gson.fromJson(parts[i], ClientGroup.class));
            }
            return groups;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getUsersInGroup(long id) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("group_id", id + "");
            String answer = request("clients.getClients", params);
            answer = answer.replaceAll("\\{\"response\":\\[", "");
            answer = answer.replaceAll("]}", "");
            String[] parts = answer.split("}.\\{");
            List<User> groups = new ArrayList<>();
            for(int i = 0;i < parts.length;i++) {
                if(!parts[i].startsWith("{")) parts[i] = "{" + parts[i];
                if(!parts[i].endsWith("}")) parts[i] = parts[i] + "}";
                System.out.println(parts[i]);
                User user = gson.fromJson(parts[i], User.class);
                user.chatId = MySql.getTgId(user.phone);
                groups.add(user);
            }
            return groups;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private URI buildURI(String method, String params) throws URISyntaxException {
        URI uri = new URI("https://joinposter.com/api/" + method + "?token=" + token + params);
        System.out.println(uri);
        return uri;
    }

    private String request(String method, Map<String, String> params) throws Exception{
        String stringParams = "&";
        for(Map.Entry<String, String> entry : params.entrySet()) {
            stringParams += entry.getKey() + "=" + entry.getValue() + "&";
        }
        HttpRequest request = HttpRequest.newBuilder(buildURI(method, stringParams)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        System.out.println(result);
        return result;
    }


}
