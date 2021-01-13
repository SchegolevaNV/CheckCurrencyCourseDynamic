package main.controllers;

import main.services.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @RequestMapping("/{currencyCode}")
    public String index(@PathVariable String currencyCode, Model model)
    {
        String gifUrl = mainService.getUrl(currencyCode);
        model.addAttribute("url", gifUrl);
        return "index";
    }
}
