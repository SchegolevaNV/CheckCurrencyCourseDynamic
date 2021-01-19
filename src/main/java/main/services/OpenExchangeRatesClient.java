package main.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;

@FeignClient(name = "converter", url = "${feign.openexchangerates.client.url}")
public interface OpenExchangeRatesClient {

    /** apiId - My api_id
     * base -  USD (because it's free of charge)
     * symbols - currency for compare */

    @GetMapping("/latest.json")
    ResponseEntity<LinkedHashMap> getLatestRate(@RequestParam ("app_id") String appId,
                                             @RequestParam String base,
                                             @RequestParam String symbols);

    @GetMapping("/historical/{date}.json")
    ResponseEntity<LinkedHashMap> getHistoryRate(@PathVariable("date") String date,
                                       @RequestParam ("app_id") String appId,
                                      @RequestParam String base,
                                      @RequestParam String symbols);
}
