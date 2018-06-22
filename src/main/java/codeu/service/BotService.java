package codeu.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class BotService {

    public String process(String query) {
        return this.request("", "");
    }

    private String request(String endpoint, String body) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target("https://api.dialogflow.com/v1/query");

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("v", "20170712");
        formData.add("query", "What time is it in New York?");
        formData.add("lang", "en");
        formData.add("sessionId", "0");

        Builder request = resource.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer fdf2bf0aff2440efbb3333e0f89a0217");

        Response response = request.post(Entity.form(formData));
        String responseBody = response.readEntity(String.class);
        System.out.println(responseBody);

        return responseBody;
    }
}
