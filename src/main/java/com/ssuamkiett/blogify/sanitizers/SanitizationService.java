package com.ssuamkiett.blogify.sanitizers;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class SanitizationService {

    private final PolicyFactory policyFactory;

    public SanitizationService() {
        String urlPattern = "http://localhost:8080/api/v1/.*";
        Pattern pattern = Pattern.compile(urlPattern);

        policyFactory = new HtmlPolicyBuilder()
                .allowElements("a", "h1", "h2", "h3", "h4", "h5", "p", "b")
                .allowAttributes("href").onElements("a")
                .allowUrlProtocols("http", "https")
                .allowAttributes("href")
                .matching(pattern)
                .onElements("a")
                .toFactory();
    }

    public String sanitize(String input) {
        return policyFactory.sanitize(input);
    }
}
