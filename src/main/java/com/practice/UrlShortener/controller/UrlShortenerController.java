package com.practice.UrlShortener.controller;

import com.practice.UrlShortener.model.UrlData;
import com.practice.UrlShortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("favicon.ico") @ResponseBody public void returnNoFavicon() { }

    @GetMapping("/{urlShorten}")
    public String getDecodeUrl(@PathVariable("urlShorten") String urlShorten) {
        Optional<UrlData> urlDataOptional = urlShortenerService.findUrlByEncodedId(urlShorten);
        if(urlDataOptional.isEmpty()) {
            throw new IllegalStateException("URL not found");
        }
        return "redirect:" + urlDataOptional.get().getUrl();
    }

    @PostMapping("/url-enc")
    @ResponseBody
    public UrlEncoded postEncodeUrl(@RequestParam(value = "url") String url, HttpServletResponse response) {
        return urlShortenerService.findOrSaveUrl(url, response);
    }
}
