package codeu.service;

import com.google.api.client.json.JsonObjectParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class BotServiceTest {

    private CloseableHttpClient httpClient;
    private CloseableHttpResponse httpResponse;

    @Before
    public void setup(){
        httpClient = Mockito.mock(CloseableHttpClient.class);
        httpResponse = Mockito.mock(CloseableHttpResponse.class);
    }

    @Test
    public void testProcess() throws IOException, HttpException{
        HttpEntity entity = Mockito.mock(HttpEntity.class);
        JsonObjectParser parser = Mockito.mock(JsonObjectParser.class);
        InputStream contentStream = Mockito.mock(InputStream.class);

        HashMap responseBody = new HashMap();
        HashMap responseStatus = new HashMap();
        HashMap queryResult = new HashMap();

        responseStatus.put("code", new BigDecimal(200));
        responseBody.put("status", responseStatus);

        queryResult.put("action", "custom_action");
        queryResult.put("speech", "test speech response");
        responseBody.put("result", queryResult);

        BotService botService = new BotService(httpClient, parser);

        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(httpResponse);
        Mockito.when(httpResponse.getEntity()).thenReturn(entity);
        Mockito.when(entity.getContent()).thenReturn(contentStream);
        Mockito.when(parser.parseAndClose(contentStream, StandardCharsets.UTF_8, HashMap.class)).thenReturn(responseBody);

        String actualResponse = botService.process("What's the time in New York?");
        Assert.assertEquals("test speech response", actualResponse);
    }

    @Test(expected = IOException.class)
    public void testProcess_andClientThrowsIOException_andExpectIOException() throws IOException, HttpException{
        JsonObjectParser parser = Mockito.mock(JsonObjectParser.class);

        BotService botService = new BotService(httpClient, parser);

        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenThrow(new IOException());

        botService.process("What's the time in New York?");
    }

    @Test(expected = HttpException.class)
    public void testProcess_andServiceThrowsHttpException_andExpectHttpException() throws IOException, HttpException{
        HttpEntity entity = Mockito.mock(HttpEntity.class);
        JsonObjectParser parser = Mockito.mock(JsonObjectParser.class);
        InputStream contentStream = Mockito.mock(InputStream.class);

        HashMap responseBody = new HashMap();
        HashMap responseStatus = new HashMap();

        responseStatus.put("code", new BigDecimal(400)); //400 Bad Request
        responseBody.put("status", responseStatus);

        BotService botService = new BotService(httpClient, parser);

        Mockito.when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(httpResponse);
        Mockito.when(httpResponse.getEntity()).thenReturn(entity);
        Mockito.when(entity.getContent()).thenReturn(contentStream);
        Mockito.when(parser.parseAndClose(contentStream, StandardCharsets.UTF_8, HashMap.class)).thenReturn(responseBody);

        botService.process("What's the time in New York?");
    }
}
