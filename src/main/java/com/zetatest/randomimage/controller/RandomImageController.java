package com.zetatest.randomimage.controller;

import com.zetatest.randomimage.entity.Image;
import com.zetatest.randomimage.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@RestController
public class RandomImageController {
    private final int MIN = 100;
    private final int MAX = 1000;

    @Value("${app.picsum.base-url}")
    private String baseURL;

    private Random random = new Random();

    @Autowired private RestTemplate restTemplate;
    @Autowired private ImageRepository imageRepository;

    private int getRandomNumber() {
        return MIN + random.nextInt(MAX);
    }

    private HttpHeaders getResponseHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_JPEG);
        return responseHeaders;
    }

    private byte[] getImage(String imageURL) {
        return restTemplate.getForObject(imageURL, byte[].class);
    }

    private String getImageURL() {
        ResponseEntity<Object> response = restTemplate.exchange( baseURL + getRandomNumber(), HttpMethod.GET, null, Object.class );
        return response.getHeaders().getLocation() == null ? "" : response.getHeaders().getLocation().toString();
    }

    @GetMapping("/")
    public ResponseEntity<byte[]> home() {
        String location = getImageURL();
        byte[] imageBytes = getImage(location);
        return ResponseEntity.ok().headers(getResponseHeaders()).body(imageBytes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable String id) {
        Image image = imageRepository.findByImageId(id).orElseGet(() -> {
            return imageRepository.saveAndFlush(new Image(id, getImageURL()));
        });
        byte[] imageBytes = getImage(image.getUrl());
        return ResponseEntity.ok().headers(getResponseHeaders()).body(imageBytes);
    }

    @GetMapping("/images")
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }
}
