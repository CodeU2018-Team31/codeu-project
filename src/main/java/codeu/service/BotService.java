package codeu.service;

import codeu.model.data.User;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A service that handles interactions with the app ChatBot, and dispatches actions on
 * such interactions as necessary.
 */
public class BotService {
    private CloseableHttpClient webClient; //The web client we will be using to make HTTP requests
    private JsonObjectParser parser; // A parser that allows us to make Maps out of JSON string responses
    private CryptService crypt; //The encrypt/decrypt service
    private ChatService chatService; //A chatService to allow for sending messages to conversations

    public BotService(){
        //Initialize with a new HTTP client, JSON parser and ChatService
        this(HttpClientBuilder.create().build(), new JsonObjectParser(new JacksonFactory()), new ChatService());
    }

    /**
     * This additional constructor providers a uniform way to initialize the dependencies of this service
     *
     * @param webClient The client to be used for making HTTP requests by the service
     * @param parser The JSON parser to be used for parsing HTTP responses
     * @param chatService The service that allows the BotService to send messages to conversations
     */
    public BotService(CloseableHttpClient webClient, JsonObjectParser parser, ChatService chatService){
        this.webClient = webClient;
        this.parser = parser;
        this.chatService = chatService;
        this.crypt = new CryptService();
    }

    /**
     * Performs Natural Language Processing on a query string, and extracts
     * the intent of the query in order to associate an action (if applicable) and a
     * response with it.
     *
     * @param query A query string to action on and/or respond to
     * @param issuer The user that initiated the request; any action issued as a result of this query will be associated
     *               with this parameter
     * @return The response of the chat bot to the query
     * @throws IOException If we fail to connect to an API we rely on for fulfilling the request
     * @throws HttpException If we receive a non-successful response from an API call
     */
    public String process(String query, User issuer) throws IOException, HttpException {
        //A JSON string representing the request body
        String requestBody = "{\n" +
                "\t\"v\": 20170712,\n" + //The version of the DialogFlow API
                "\t\"query\": \""+query+"\",\n" + //The provided query to process
                "\t\"lang\": \"en\",\n" + //Assume the language to be in English
                "\t\"sessionId\": \"0\"\n" + //A session ID to keep track of its corresponding requests. Required by API, but we dont need it.
                "}";

        HashMap response = this.request("query", requestBody);
        final int SUCCESS_CODE = 200;

        if(!response.containsKey("status") || ((BigDecimal)((Map)response.get("status")).get("code")).intValueExact() != SUCCESS_CODE){
            throw new HttpException("BotService-process: Request to process query has returned with errors");
        }

        Map queryResult = (Map)response.get("result");
        String action = (String) queryResult.get("action");
        String chatResponse = (String) queryResult.get("speech");

        if(action.equals("send_message")){
            try{
                sendMessageToConversation(queryResult, issuer);
            }
            catch(IllegalArgumentException ex){
                chatResponse = "Oh, I am sorry, it seems like that conversation does not exist yet!";
            }
        }

        return chatResponse;
    }

    /**
     * Handles making requests to the DialogFlow API
     *
     * @param endpoint The endpoint on the API to call
     * @param body A JSON string to send in the request
     * @return A HashMap representing the processed JSON response
     * @throws IOException If we fail to connect to the DialogFlow API
     */
    private HashMap request(String endpoint, String body) throws IOException {
        //Initialize a POST request to DialogFlow and set its body
        HttpPost request = new HttpPost("https://api.dialogflow.com/v1/"+endpoint);
        StringEntity parameters = new StringEntity(body, "UTF-8");
        request.setEntity(parameters);

        //Declare the use of JSON
        request.addHeader("Content-Type", "application/json");
        //Auth token to allow us to use the API
        request.addHeader("Authorization", "Bearer "+crypt.decrypt("sCpnr>?r>pEnAEp>nproq=>osnoAEA=D"));

        HttpResponse response = webClient.execute(request); //Make the HTTP request
        //Parse the response into a HashMap
        HashMap responseBody = parser.parseAndClose(response.getEntity().getContent(), StandardCharsets.UTF_8, HashMap.class);

        return responseBody;
    }

    /**
     * Handles processing a DialogFlow query result, in order to send a message to a conversation
     *
     * @param queryResult The DialogFlow query result to process. The query result has to have the following shape extract:
     *                    {
     *                      "parameters": {
     *                          "message": String,
     *                          "conversation": String
     *                      }
     *                    }
     * @param author The user who will be marked as the author of the message
     * @throws IllegalArgumentException The conversation name provided in the queryResult does not exist in store
     */
    private void sendMessageToConversation(Map queryResult, User author) throws IllegalArgumentException{
        Map parameters = (Map) queryResult.get("parameters");
        String message = (String) parameters.get("message");
        String conversation = (String) parameters.get("conversation");
        chatService.sendMessageToConversation(author, message, conversation);
    }
}
