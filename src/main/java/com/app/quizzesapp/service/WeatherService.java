package com.app.quizzesapp.service;

import com.app.quizzesapp.exception.MyException;
import com.app.quizzesapp.model.weather.Weather;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Service
@Transactional
public class WeatherService
{

    public Optional<Weather> getWeatherByCapitol(String capital)
    {
        if (capital == null)
        {
            throw new MyException("CAPITAL VALUE IS NULL");
        }

        if (capital.equals("City of Victoria"))
        {
            capital = "victoria";
        }
        else if (capital.equals("Ulan Bator"))
        {
            capital = "Ulaanbaatar";
        }else if (capital.equals("Tórshavn"))
        {
            capital = "Torshavn";
        }else if (capital.equals("Nicosia"))
        {
            capital = "Cyprus";
        }else if (capital.equals("St. Peter Port"))
        {
            capital = "Saint Peter Port";
        }else if (capital.equals("Pristina"))
        {
            capital = "Republic of Kosovo";
        }

        capital = capital.replace(" ","+");

        Weather weather = null;
        try {

            HttpResponse<String> response = HttpClient.newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(requestGetWeather("https://community-open-weather-map.p.rapidapi.com/weather?callback=test&id=2172797&units=%22metric%22+or+%22imperial%22&mode=xml%2C+html&q=",capital), HttpResponse.BodyHandlers.ofString());

            String r = response.body().replaceAll("test","").replaceAll("\\(","").replaceAll("\\)","");
            Gson g = new Gson();

            weather =  g.fromJson(r,Weather.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return Optional.of(weather);

    }

    private HttpRequest requestGetWeather(final String path, String capital) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(path + capital))
                .header("X-RapidAPI-Host", "community-open-weather-map.p.rapidapi.com")
                .header("X-RapidAPI-Key", "fd28bf246dmshcd789bbdc72c73cp1e6d91jsn427a4bdf7163")
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
    }

}
