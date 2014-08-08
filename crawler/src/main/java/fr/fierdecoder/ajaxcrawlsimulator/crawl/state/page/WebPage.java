package fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page;

import java.util.Objects;

public class WebPage {
    private final WebPageType type;
    private final String url;
    private final int httpStatus;
    private final String body;

    public WebPage(WebPageType type, String url, int httpStatus, String body) {
        this.type = type;
        this.url = url;
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getBody() {
        return body;
    }

    public boolean isHtml() {
        return type == WebPageType.HTML;
    }

    public HtmlWebPage asHtml() {
        return (HtmlWebPage) this;
    }

    public boolean isRedirection() {
        return type == WebPageType.REDIRECTION;
    }

    public RedirectionWebPage asRedirection() {
        return (RedirectionWebPage) this;
    }

    public boolean isUnreachable() {
        return type == WebPageType.UNREACHABLE;
    }

    public boolean isText() {
        return type == WebPageType.TEXT;
    }

    public boolean isBinary() {
        return type == WebPageType.BINARY;
    }

    public WebPageType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebPage webPage = (WebPage) o;

        return Objects.equals(type, webPage.type)
                && Objects.equals(httpStatus, webPage.httpStatus)
                && Objects.equals(body, webPage.body)
                && Objects.equals(type, webPage.type)
                && Objects.equals(url, webPage.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url, httpStatus, body);
    }

    @Override
    public String toString() {
        return "WebPage{" +
                "type=" + type +
                ", url='" + url + '\'' +
                ", httpStatus=" + httpStatus +
                ", body='" + body + '\'' +
                '}';
    }
}