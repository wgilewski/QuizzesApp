package com.app.quizzesapp.service;

import com.app.quizzesapp.exception.MyException;
import com.app.quizzesapp.model.localization.Localization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;


@Service
@RequiredArgsConstructor
public class LocalizationService
{

    public Optional<String> findMyIp()
    {
        String systemipaddress = null;
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));

            systemipaddress = sc.readLine().trim();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return Optional.of(systemipaddress);
    }

    private HttpRequest requestGetMyLocalization(final String path) throws URISyntaxException
    {
            return HttpRequest.newBuilder()
                    .uri(new URI(path))
                    .header("X-RapidAPI-Host", "jkosgei-free-ip-geolocation-v1.p.rapidapi.com")
                    .header("X-RapidAPI-Key", "fd28bf246dmshcd789bbdc72c73cp1e6d91jsn427a4bdf7163")
                    .version(HttpClient.Version.HTTP_2)
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();
    }

    public Optional<Localization> findMyLocalization()
    {
        String apiPath;
        Optional<String> myIp = findMyIp();
        if (myIp.isPresent())
        {
            apiPath = "https://jkosgei-free-ip-geolocation-v1.p.rapidapi.com/" + findMyIp().get() + "?api-key=test";
        }
        else throw new MyException("IP IS NULL");

        Localization localization = null;
        try {
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(requestGetMyLocalization(apiPath),HttpResponse.BodyHandlers.ofString());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            localization = gson.fromJson(response.body(),Localization.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return Optional.of(localization);
    }
}
