package com.app.quizzesapp.service;


import com.app.quizzesapp.exception.MyException;

import com.app.quizzesapp.model.Countries;
import com.app.quizzesapp.model.country.Country;
import com.app.quizzesapp.model.country.informations.Details;
import com.app.quizzesapp.repository.CountryRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class CountryService
{
    private final CountryRepository countryRepository;
    private static final String jsonFilePath = "countries.json";



    private HttpRequest requestGetAllCountries(final String path) throws URISyntaxException
    {
        return HttpRequest.newBuilder()
            .uri(new URI(path))
            .header("X-RapidAPI-Host", "restcountries-v1.p.rapidapi.com")
            .header("X-RapidAPI-Key", "fd28bf246dmshcd789bbdc72c73cp1e6d91jsn427a4bdf7163")
            .version(HttpClient.Version.HTTP_2)
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build();
    }

    private HttpRequest requestGetDistance(final String path) throws URISyntaxException
    {
        return HttpRequest.newBuilder()
                .uri(new URI(path))
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
    }

    public void saveCountries()
    {
        List<Country> countries = getAllCountriesFromApi();
        if (countries.size() == 0)
        {
            throw new MyException("COUNTRY LIST IS EMPTY");
        }
        else
        {
            for (Country c : countries)
            {
                c.setRegion(c.getRegion().toUpperCase());
                countryRepository.save(c);
            }
        }
    }

    public Optional<Details> getDistanceAndCuriosities(String town1, String town2)
    {
        final String apiPath = "https://www.dystans.org/route.json?stops="+town1.replaceAll(" ","+") + "%7C" + town2.replaceAll(" ","+");

        Details details = null;

        try {
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(requestGetDistance(apiPath),HttpResponse.BodyHandlers.ofString());

            Gson g = new GsonBuilder().setPrettyPrinting().create();
            StringBuilder stringBuilder = new StringBuilder(response.body().replaceAll("abstract","curiosities"));
            details = g.fromJson(stringBuilder.toString(), Details.class);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return Optional.of(details);
    }

    public List<Country> getAllCountriesFromApi()
    {
        final String countriesPath = "https://restcountries-v1.p.rapidapi.com/all";

        List<Country> countries = new ArrayList<>();
        try {
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(requestGetAllCountries(countriesPath),HttpResponse.BodyHandlers.ofString());



            String[] r = response.body().split("\\}\\,\\{");
            r[0] = r[0].replaceAll("\\[\\{","");
            r[249] = r[249].replaceAll("\\}\\]","");

            Gson g = new GsonBuilder().setPrettyPrinting().create();

            for (String s : r)
            {
                StringBuilder sb = new StringBuilder(s);
                sb.insert(0,"{");
                sb.append("}");
                Country c = g.fromJson(sb.toString(), Country.class);

                System.out.println(c);
                countries.add(c);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return countries;

    }


}
