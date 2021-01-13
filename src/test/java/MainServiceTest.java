import main.services.GifClient;
import main.services.MainService;
import main.services.OpenExchangeRatesClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= MainService.class)
class MainServiceTest {

    @Autowired
    private MainService mainService;

    @MockBean
    @Qualifier("gifClient")
    private GifClient gifClient;

    @MockBean
    @Qualifier("ratesClient")
    private OpenExchangeRatesClient openExchangeRatesClient;

    private final String apiGifKey = "VYckgqg8nmDJznidJwHEi4sllXRkcqcL";
    private final String appOpenExchangeRates = "ea10d7ab8e9e4fde837895c6cd94308c";
    private final String baseCurrency = "RUB";
    private final String ratesTag = "rates";
    private final String dataTag = "data";
    private final String imageUrlTag = "image_original_url";
    private final String increaseTag = "rich";
    private final String decreaseTag = "broke";
    private final String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    private String gifRichUrl;
    private String gifBrokeUrl;
    private String currencyCode;
    private Double todayRate;
    private Double yesterdayRate;

    private LinkedHashMap gifObject = new LinkedHashMap();
    private LinkedHashMap gifUrl = new LinkedHashMap();
    private LinkedHashMap ratesToday = new LinkedHashMap();
    private LinkedHashMap ratesYesterday = new LinkedHashMap();
    private LinkedHashMap currencyToday = new LinkedHashMap();
    private LinkedHashMap currencyYesterday = new LinkedHashMap();

    @BeforeEach
    public void setUpToTestData()
    {
        gifRichUrl = "https://media3.giphy.com/media/5885nYOgBHdCw/giphy.gif?cid=ecf05e47b3m02e2exsrkuovn75" +
                "uppty8hya1w5f1cxdre0xz&rid=giphy.gif";

        gifBrokeUrl = "https://media4.giphy.com/media/ZGH8VtTZMmnwzsYYMf/giphy.gif?" +
                "cid=ecf05e47ianfznqq5kyl9a1cmwrrf1m3bbo7y1374wvvasg0&rid=giphy.gif";

        currencyCode = "USD";

        Mockito.when(gifClient.getGif(apiGifKey, increaseTag)).thenReturn(ResponseEntity.ok(gifObject));
        Mockito.when(gifClient.getGif(apiGifKey, decreaseTag)).thenReturn(ResponseEntity.ok(gifObject));
        Mockito.when(openExchangeRatesClient.getLatestRate(appOpenExchangeRates, baseCurrency, currencyCode))
                .thenReturn(ResponseEntity.ok(ratesToday));
        Mockito.when(openExchangeRatesClient.getHistoryRate(yesterday, appOpenExchangeRates, baseCurrency, currencyCode))
                .thenReturn(ResponseEntity.ok(ratesYesterday));
    }

    @Test
    void getRichUrlTest()
    {
        todayRate = 1.34;
        yesterdayRate = 1.23;

        currencyToday.put(currencyCode, todayRate);
        ratesToday.put(ratesTag, currencyToday);

        currencyYesterday.put(currencyCode, yesterdayRate);
        ratesYesterday.put(ratesTag, currencyYesterday);

        gifUrl.put(imageUrlTag, gifRichUrl);
        gifObject.put(dataTag, gifUrl);

        String actualUrl = mainService.getUrl(currencyCode);
        Assertions.assertEquals(gifRichUrl, actualUrl);
    }

    @Test
    void getBrokeUrlTest()
    {
        todayRate = 1.34;
        yesterdayRate = 1.37;

        currencyToday.put(currencyCode, todayRate);
        ratesToday.put(ratesTag, currencyToday);

        currencyYesterday.put(currencyCode, yesterdayRate);
        ratesYesterday.put(ratesTag, currencyYesterday);

        gifUrl.put(imageUrlTag, gifBrokeUrl);
        gifObject.put(dataTag, gifUrl);

        String actualUrl = mainService.getUrl(currencyCode);
        Assertions.assertEquals(gifBrokeUrl, actualUrl);
    }

    @Test
    void getBadUrlTest()
    {
        currencyToday.put(null, null);
        ratesToday.put(ratesTag, currencyToday);

        currencyYesterday.put(null, null);
        ratesYesterday.put(ratesTag, currencyYesterday);

        String badUrl = "https://caho.ru/images/blog/003-ne-tak/ne-tak.png";
        String actualUrl = mainService.getUrl(currencyCode);

        Assertions.assertEquals(badUrl, actualUrl);
    }

    @Test
    void getGifIncreaseUrlTest()
    {
        gifUrl.put(imageUrlTag, gifRichUrl);
        gifObject.put(dataTag, gifUrl);
        String actualRichUrl = mainService.getGifUrl(increaseTag);

        Assertions.assertEquals(gifRichUrl, actualRichUrl);
    }

    @Test
    void getGifDecreaseUrlTest()
    {
        gifUrl.put(imageUrlTag, gifBrokeUrl);
        gifObject.put(dataTag, gifUrl);
        String actualBrokeUrl = mainService.getGifUrl(decreaseTag);

        Assertions.assertEquals(gifBrokeUrl, actualBrokeUrl);
    }

    @Test
    void getTodayOkRateTest()
    {
        todayRate = 1.34;
        currencyToday.put(currencyCode, todayRate);
        ratesToday.put(ratesTag, currencyToday);
        Double actualRate = mainService.getTodayRate(currencyCode);

        Assertions.assertEquals(todayRate, actualRate);
    }

    @Test
    void getTodayNullRateTest()
    {
        currencyToday.put(null, null);
        ratesToday.put(ratesTag, currencyToday);
        Double actualRate = mainService.getTodayRate(currencyCode);

        Assertions.assertNull(actualRate);
    }

    @Test
    void getYesterdayRateTest()
    {
        yesterdayRate = 1.35;
        currencyYesterday.put(currencyCode, yesterdayRate);
        ratesYesterday.put(ratesTag, currencyYesterday);
        Double actualRate = mainService.getYesterdayRate(currencyCode, yesterday);

        Assertions.assertEquals(yesterdayRate, actualRate);
    }
}
