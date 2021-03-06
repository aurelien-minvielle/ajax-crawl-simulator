package fr.fierdecoder.ajaxcrawlsimulator.web.application;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.crawler.Crawler;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.CrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.crawl.state.MemoryCrawlStateFactory;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.MemorySimulationsRepository;
import fr.fierdecoder.ajaxcrawlsimulator.simulator.simulation.repository.SimulationRepository;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SimulationRepository.class).to(MemorySimulationsRepository.class).in(Singleton.class);
        bind(CrawlStateFactory.class).to(MemoryCrawlStateFactory.class).in(Singleton.class);
        bind(Crawler.class).to(CrawlerStub.class).in(Singleton.class);
    }
}
