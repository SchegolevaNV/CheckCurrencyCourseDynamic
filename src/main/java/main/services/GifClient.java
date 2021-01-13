package main.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;

@FeignClient(name = "gif", url = "${feign.gif.client.url}")
public interface GifClient {

    @GetMapping("")
    ResponseEntity<LinkedHashMap> getGif(@RequestParam String apiKey,
                                         @RequestParam String tag);
}
