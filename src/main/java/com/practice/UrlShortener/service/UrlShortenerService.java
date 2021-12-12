package com.practice.UrlShortener.service;

import com.practice.UrlShortener.controller.UrlEncoded;
import com.practice.UrlShortener.model.UrlData;
import com.practice.UrlShortener.repository.UrlShortenerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UrlShortenerService {
    private final UrlShortenerRepository urlShortenerRepository;

    private final long base62 = 62L;
    private final String base62Table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public UrlShortenerService(UrlShortenerRepository urlShortenerRepository) {
        this.urlShortenerRepository = urlShortenerRepository;
    }

    public Optional<UrlData> findUrlByEncodedId(String encodedId) {
        return urlShortenerRepository.findById(decodeBase62(encodedId));
    }

    public UrlEncoded findOrSaveUrl(String url, HttpServletResponse response) {
        UrlEncoded urlEncoded = new UrlEncoded();
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/";
        try {
            String formattedUrl = validateUrl(url);
            urlShortenerRepository.findByUrl(formattedUrl).ifPresentOrElse(
                    (urlData -> urlEncoded.setShortenUrl(baseUrl.concat(encodeBase62(urlData.getId())))),
                    (() -> {
                        UrlData urlData = new UrlData();
                        urlData.setUrl(formattedUrl);
                        UrlData saved = urlShortenerRepository.save(urlData);
                        urlEncoded.setShortenUrl(baseUrl.concat(encodeBase62(saved.getId())));
                    })
            );
            urlEncoded.setMessage("Success! You may copy the shorten URL above.");
        } catch (Exception e) {
            urlEncoded.setShortenUrl("");
            urlEncoded.setMessage("The URL may be invalid. Try something else.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return urlEncoded;
    }

    private String validateUrl(String url) {
        // format URL
        StringBuffer sb = new StringBuffer();
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            sb.append("http://");
        }
        sb.append(url);
        if(!url.endsWith("/")) {
            sb.append("/");
        }
        String resultURL = sb.toString();

        // url pattern reference: https://cpdev.tistory.com/127
        String urlPattern = "^(http|https):\\/\\/((\\d{1,3}:){3}\\d{1,3}|([\\w-]+(\\.[\\w-]+)+))(:\\d{2,5})?(\\/|(\\/[\\w#!:.?+=&%@!\\-\\/]+)+)?$";
        if(!Pattern.compile(urlPattern).matcher(resultURL).matches()) {
            throw new IllegalStateException("URL is not valid.");
        }

        return resultURL;
    }

    private String encodeBase62(long id) {
        StringBuffer sb = new StringBuffer();
        while(id > 0) {
            sb.append(base62Table.charAt((int)(id % base62)));
            id /= base62;
        }
        sb.reverse();
        return sb.toString();
    }

    private long decodeBase62(String shortenUrl) {
        long id = 0L;
        int index;
        for(int i=0; i < shortenUrl.length(); i++) {
            id *= base62;
            index = base62Table.indexOf(shortenUrl.charAt(i));
            if(index == -1) {
                return -1L;
            }
            id += index;
        }
        return id;
    }
}
