package com.athaydes.rawhttp.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class MethodLine implements StartLine {

    private final String method;
    private final URI uri;
    private final String httpVersion;

    public MethodLine(String method, URI uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String getHttpVersion() {
        return httpVersion;
    }

    public MethodLine withHost(String host) {
        try {
            if (!host.matches("[a-z]{1,6}://.*")) {
                host = "http://" + host;
            }
            URI hostURI = URI.create(host);
            URI newURI = new URI(hostURI.getScheme(),
                    hostURI.getUserInfo(),
                    hostURI.getHost(),
                    hostURI.getPort(),
                    uri.getPath(), uri.getQuery(), uri.getFragment());
            return new MethodLine(method, newURI, httpVersion);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid host format" + Optional.ofNullable(
                    e.getMessage()).map(s -> ": " + s).orElse(""));
        }
    }

    @Override
    public String toString() {
        URI pathURI;
        String path = (uri.getPath() == null || uri.getPath().trim().isEmpty())
                ? "/" : uri.getPath();
        try {
            // only path and query are sent to the server
            pathURI = new URI(null, null, null, -1, path, uri.getQuery(), null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return method + " " + pathURI + " " + httpVersion;
    }
}