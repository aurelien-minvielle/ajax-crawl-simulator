package fr.fierdecoder.ajaxcrawlsimulator.crawl;

import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.HtmlWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.RedirectionWebPage;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.page.WebPage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Crawler {
    private final PageReader pageCrawler = new PageReader();
    private final CrawlPerimeter crawlPerimeter;
    private final Queue<String> urlQueue = new LinkedList<>();
    private PagesRegistry registry = new PagesRegistry();

    public Crawler(CrawlPerimeter crawlPerimeter) {
        this.crawlPerimeter = crawlPerimeter;
    }

    public PagesRegistry crawl() throws IOException {
        urlQueue.add(crawlPerimeter.getEntryUrl());
        while (!urlQueue.isEmpty()) {
            crawlNextUrl();
        }
        return registry;
    }

    private void crawlNextUrl() throws IOException {
        String url = urlQueue.poll();
        if (mustBeCrawled(url)) {
            WebPage page = pageCrawler.readPage(url);
            if (page.isHtmlWebPage()) {
                HtmlWebPage htmlPage = (HtmlWebPage) page;
                urlQueue.addAll(htmlPage.getLinks());
            } else if (page.isRedirection()) {
                RedirectionWebPage redirection = (RedirectionWebPage) page;
                urlQueue.add(redirection.getTargetUrl());
            }
            registry = registry.register(page);
        }
    }

    private boolean mustBeCrawled(String url) {
        return !registry.containsUrl(url) && crawlPerimeter.contains(url);
    }
}
