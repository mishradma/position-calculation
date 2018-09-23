package com.org.mycompany.positions.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.LastModifiedFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.messaging.MessageChannel;

import com.org.mycompany.positions.services.PositionFileProcessorService;
import com.org.mycompany.positions.services.TransactionFileProcessorService;

/**
 * @author Dayanand Mishra
 *
 */
@Configuration
public class PollerConfig {
	private static final Logger LOG = LoggerFactory.getLogger(PollerConfig.class);
	@Value(value = "${dir.to.poll:.}")
	private String directoryToPoll;

	@Bean
	@InboundChannelAdapter(channel = "transactionFileInputChannel", poller = @Poller(fixedDelay = "1000"))
	public MessageSource<File> transactionFileReadingMessageSource() {

		CompositeFileListFilter<File> filters = new CompositeFileListFilter<>();
		filters.addFilter(new SimplePatternFileListFilter("*_transactions.txt"));
		filters.addFilter(new LastModifiedFileListFilter());

		FileReadingMessageSource source = new FileReadingMessageSource();
		source.setAutoCreateDirectory(true);
		source.setDirectory(fileToPoll());
		source.setFilter(filters);
		return source;

	}

	private File fileToPoll() {
		File file = new File(directoryToPoll);
		LOG.info("System is configured to poll directory" + file.getAbsolutePath());
		return file;
	}

	@Bean
	@InboundChannelAdapter(channel = "positionFileInputChannel", poller = @Poller(fixedDelay = "1000"))
	public MessageSource<File> positionFileReadingMessageSource() {
		CompositeFileListFilter<File> filters = new CompositeFileListFilter<>();
		filters.addFilter(new SimplePatternFileListFilter("*_positions.txt"));
		filters.addFilter(new LastModifiedFileListFilter());
		FileReadingMessageSource source = new FileReadingMessageSource();
		source.setAutoCreateDirectory(true);
		source.setDirectory(fileToPoll());
		source.setFilter(filters);
		return source;
	}

	@Bean
	public FileToStringTransformer fileToStringTransformer() {
		FileToStringTransformer fileToStringTransformer = new FileToStringTransformer();
		fileToStringTransformer.setDeleteFiles(true);
		return fileToStringTransformer;
	}

	@Bean
	public IntegrationFlow processPositionFileFlow() {
		return IntegrationFlows.from("positionFileInputChannel").transform(fileToStringTransformer())
				.handle(PositionFileProcessorService.SERVICE_ACTIVATOR, "process").get();
	}

	@Bean
	public IntegrationFlow processTransactionFileFlow() {
		return IntegrationFlows.from("transactionFileInputChannel").transform(fileToStringTransformer())
				.handle(TransactionFileProcessorService.SERVICE_ACTIVATOR, "process").get();
	}

	@Bean
	public MessageChannel positionFileInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel transactionFileInputChannel() {
		return new DirectChannel();
	}

}
