package br.com.claramoreira.batchscores.batchscores.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.client.RestTemplate;

import br.com.claramoreira.batchscores.batchscores.model.Wine;
import br.com.claramoreira.batchscores.batchscores.model.WineString;
import br.com.claramoreira.batchscores.batchscores.reader.WineReader;

@Configuration
public class WineStepConfig {

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	private ItemProcessor<Wine, WineString> processor;

	@Bean
	@Qualifier("wineStep")
	public Step wineStep() {
		return steps.get("wineStep").<Wine, WineString>chunk(2)
				.reader(new WineReader("https://api.sampleapis.com/coffee/hot", restTemplate())).processor(processor)
				.writer(flatFileItemWriter()).build();
	}

	@Bean
	public FlatFileItemWriter<WineString> flatFileItemWriter() {
		return new FlatFileItemWriterBuilder<WineString>().name("flatFileItemWriter")
				.resource(new FileSystemResource("c:/temp/data.txt")).lineAggregator(new PassThroughLineAggregator<>()).build();
	}
}
