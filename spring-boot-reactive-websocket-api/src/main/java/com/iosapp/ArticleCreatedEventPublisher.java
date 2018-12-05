package com.iosapp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.iosapp.utilities.ArticleCreatedEvent;

import reactor.core.publisher.FluxSink;

@Component
public class ArticleCreatedEventPublisher implements 
	ApplicationListener<ArticleCreatedEvent>, // 1 - ApplicationListener<ApplicationEvent> it tells the framework that we want to be notified, via the onApplicationEvent(ProfileCreatedEvent) method, of any new events published when a new Profile is created.
	Consumer<FluxSink<ArticleCreatedEvent>> { // 2 - A FluxSink<T> is a thing into which we can publish new items, however we may arrive at them. If you want to integrate the reactive world with non-reactive code in the outside world, use this construction.

	@Autowired
	Executor executor;
	
	private final BlockingQueue<ArticleCreatedEvent> queue = new LinkedBlockingQueue<>(); 
	
	@Override
	public void onApplicationEvent(ArticleCreatedEvent event) { // 3 - when an event is published in our service, it is spread to any and all interested listeners, including this component which then offers the item into the Queue<T> 
        this.queue.offer(event);
	}
	
	@Override
	public void accept(FluxSink<ArticleCreatedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                	ArticleCreatedEvent event = queue.take(); // 4 - The event loop couldn’t be simpler. We wait for new entries to appear in the BlockingQueue<T>
                    sink.next(event); // 5 - and as soon as they are, we tell our reactive stream about them by calling FluxSink<T>.next(T)
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
		
	}



}
