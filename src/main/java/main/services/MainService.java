package main.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class MainService {

    @Value("${api.gif.key}")
    private String apiKey;

    @Value("${app.openexchangerates.id}")
    private String appOpenExchangeRates;

    @Value("${base.currency}")
    private String baseCurrency;

    @Value("${tag.for.increase.course}")
    private String increaseTag;

    @Value("${tag.for.decrease.course}")
    private String decreaseTag;

    @Value("${bad.request.gif.url}")
    private String badUrl;

    @Value("${rates.tag}")
    private String ratesTag;

    @Value("${data.tag}")
    private String dataTag;

    @Value("${image.tag}")
    private String imageUrlTag;

    private final GifClient gifClient;
    private final OpenExchangeRatesClient openExchangeRatesClient;

    public MainService(GifClient gifClient, OpenExchangeRatesClient openExchangeRatesClient) {
        this.gifClient = gifClient;
        this.openExchangeRatesClient = openExchangeRatesClient;
    }

    public String getUrl(String currencyCode)
    {
        String gifUrl = badUrl;
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

        if (!currencyCode.equals(baseCurrency)) {
            Double rateToday = getTodayRate(currencyCode);
            Double rateYesterday = getYesterdayRate(currencyCode, yesterday);

            if (rateToday != null && rateYesterday != null) {
                gifUrl = (rateToday >= rateYesterday) ? getGifUrl(increaseTag) : getGifUrl(decreaseTag);
            }
        }
        return gifUrl;
    }

    public Double getTodayRate(String currencyCode) {
        LinkedHashMap todayRate = (LinkedHashMap) Objects.requireNonNull(openExchangeRatesClient
                .getLatestRate(appOpenExchangeRates, baseCurrency, currencyCode)
                .getBody())
                .get(ratesTag);
        return (Double) todayRate.get(currencyCode);
    }

    public Double getYesterdayRate(String currencyCode, String date) {
        LinkedHashMap yesterdayRate = (LinkedHashMap) Objects.requireNonNull(openExchangeRatesClient
                .getHistoryRate(date, appOpenExchangeRates, baseCurrency, currencyCode)
                .getBody())
                .get(ratesTag);
        return (Double) yesterdayRate.get(currencyCode);
    }

    public String getGifUrl(String tag) {
        LinkedHashMap objectData = (LinkedHashMap) Objects.requireNonNull(gifClient
                .getGif(apiKey, tag)
                .getBody())
                .get(dataTag);
        return (String) objectData.get(imageUrlTag);
    }
}

