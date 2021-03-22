package br.com.laersondev.ghreposapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AutoEvictCacheService {

	private static final int TEN_MINUTES = 600000;

	@Autowired
	CacheManager cacheManager;

	public AutoEvictCacheService() {
		super();
	}

	@Scheduled(fixedRate = TEN_MINUTES)
	public void clearAllCaches() {
		this.cacheManager.getCacheNames().stream().forEach(cacheName -> this.cacheManager.getCache(cacheName).clear());
	}
}
