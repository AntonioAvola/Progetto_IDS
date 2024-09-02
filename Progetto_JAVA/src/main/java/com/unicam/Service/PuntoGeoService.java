package com.unicam.Service;

import org.springframework.web.client.RestTemplate;

public class PuntoGeoService {

    public String reverseGeocode(double lat, double lon) {
        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + lat + "&lon=" + lon;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        return response;
    }
}
