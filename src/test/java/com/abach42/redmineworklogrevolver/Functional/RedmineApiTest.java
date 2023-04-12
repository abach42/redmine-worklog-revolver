package com.abach42.redmineworklogrevolver.Functional;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RedmineApiTest {

    HttpGet httpGet = new HttpGet();

    public int callApiStatusCode(URI apiUrl) throws ClientProtocolException, IOException {
        Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        httpGet.setURI(apiUrl);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();

        response.close();
        httpClient.close();

        return statusCode;
    }
    
    @Test
    @DisplayName("Is redmine API available?")
    public void isApiAvailable() throws ClientProtocolException, IOException, URISyntaxException {
        assertThat(
            callApiStatusCode(new URI("https://frs.plan.io/issues.json")))
                .isNotEqualTo(440).isNotEqualTo(500);
    }
}
