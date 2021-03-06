package fr.fierdecoder.ajaxcrawlsimulator.crawl.crawler;

import com.google.inject.Inject;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.connector.PageReader;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.perimeter.CrawlPerimeter;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlState;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.page.WebPage;
import org.slf4j.Logger;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

public class NetworkCrawler implements Crawler {
    private static final Logger LOGGER = getLogger(NetworkCrawler.class);
    private final PageReader pageReader;

    @Inject
    public NetworkCrawler(PageReader pageReader) {
        this.pageReader = pageReader;
    }

    @Override
    public void crawl(CrawlPerimeter crawlPerimeter, CrawlState state) {
        state.addUrlToCrawl(crawlPerimeter.getEntryUrl());
        newSingleThreadExecutor().submit(() -> launchCrawl(crawlPerimeter, state));
    }

    private void launchCrawl(CrawlPerimeter crawlPerimeter, CrawlState state) {
        while (state.hasUrlToCrawl()) {
            String url = state.getUrlToCrawl();
            Collection<String> newUrls = crawlUrlIfNeeded(url, state, crawlPerimeter);
            state.addUrlsToCrawl(newUrls);
        }
        LOGGER.info("Crawl terminated");
        state.maskAsFinished();
    }

    private Collection<String> crawlUrlIfNeeded(String url, CrawlState state, CrawlPerimeter crawlPerimeter) {
        if (mustBeCrawled(url, state, crawlPerimeter)) {
            LOGGER.info("Crawling {}", url);
            return crawlUrl(url, state);
        }
        return emptyList();
    }

    private Collection<String> crawlUrl(String url, CrawlState state) {
        WebPage page = pageReader.readPage(url);
        state.addPage(page);
        if (page.isHtml()) {
            LOGGER.info("Url {} returned a HTML page with title {}", url, page.getTitle().get());
            return page.getLinks();
        } else if (page.isRedirection()) {
            LOGGER.info("Url {} returned a redirection to {}", url, page.getTargetUrl().get());
            return newArrayList(page.getTargetUrl().get());
        } else if (page.isUnreachable()) {
            LOGGER.info("Url {} is unreachable", url);
            return emptyList();
        } else if (page.isBinary()) {
            LOGGER.info("Url {} returned a binary file");
            return emptyList();
        } else if (page.isText()) {
            LOGGER.info("Url {} returned a text file");
            return emptyList();
        }
        throw new IllegalStateException("Unknown page type");
    }

    private boolean mustBeCrawled(String url, CrawlState repository, CrawlPerimeter crawlPerimeter) {
        return crawlPerimeter.contains(url) && !repository.containsPage(url);
    }
}
