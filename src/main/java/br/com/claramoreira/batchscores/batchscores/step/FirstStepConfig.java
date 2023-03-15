package br.com.claramoreira.batchscores.batchscores.step;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import br.com.claramoreira.batchscores.batchscores.model.Person;
import br.com.claramoreira.batchscores.batchscores.processor.PersonItemProcessor;

@Configuration
public class FirstStepConfig {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

	
	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public Person person() {
		return new Person();
	}

	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public ItemProcessor<Person, Person> itemProcessor() {
		return new PersonItemProcessor();
	}
	
	@Autowired
	private StepBuilderFactory steps;
	


	@Bean
	public Step step3() {
		return steps.get("step3").<Person, Person>chunk(2).reader(fileItemReader(beanWrapperFieldSetMapper())).processor(itemProcessor()).writer(staxEventItemWriter(jaxb2Marshaller()))
				.build();
	}

	@Bean
	public FlatFileItemReader<Person> fileItemReader(BeanWrapperFieldSetMapper<Person> beanWrapperFieldSetMapper) {
		FlatFileItemReader<Person> fileItemReader = new FlatFileItemReader<>();
		fileItemReader.setResource(new ClassPathResource("person.csv"));
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("id", "firstName", "lastName");
		DefaultLineMapper<Person> defaultLineMapper = new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		fileItemReader.setLineMapper(defaultLineMapper);
		return fileItemReader;
	}

	@Bean
	public BeanWrapperFieldSetMapper<Person> beanWrapperFieldSetMapper() {
		BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setPrototypeBeanName("person");
		return fieldSetMapper;
	}

	@Bean(destroyMethod = "")
	public StaxEventItemWriter<Person> staxEventItemWriter(Jaxb2Marshaller marshaller) {
		StaxEventItemWriter<Person> staxEventItemWriter = new StaxEventItemWriter<>();
		staxEventItemWriter
				.setResource(new FileSystemResource("C:/temp/person_" + dateFormat.format(new Date()) + ".xml"));
		staxEventItemWriter.setMarshaller(marshaller);
		staxEventItemWriter.setRootTagName("personInfo");
		return staxEventItemWriter;
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(Person.class);
		return jaxb2Marshaller;
	}

}
