package br.com.claramoreira.batchscores.batchscores.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import br.com.claramoreira.batchscores.batchscores.model.Person;

@Profile("spring")
public class SpringBatchConfig {

	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	@Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;


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
	public StaxEventItemWriter<Person> staxEventItemWriter(Jaxb2Marshaller marshaller) {
		StaxEventItemWriter<Person> staxEventItemWriter = new StaxEventItemWriter<>();
		staxEventItemWriter.setResource(new FileSystemResource("C:/temp/person_" + dateFormat.format(new Date()) + ".xml"));
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
	
	@Bean
    protected Step step1(ItemReader<Person> reader,
      ItemProcessor<Person, Person> processor,
      ItemWriter<Person> writer) {
        return steps.get("step1").<Person, Person> chunk(10)
          .reader(reader).processor(processor).writer(writer).build();
    }
	
	@Bean
	public BeanWrapperFieldSetMapper<Person> beanWrapperFieldSetMapper() {
		BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setPrototypeBeanName("person");
		return fieldSetMapper;
	}


    @Bean(name = "firstBatchJob")
    public Job firstBatchJob(@Qualifier("step1") Step step1) {
        return jobs.get("firstBatchJob").start(step1).build();
    }

    
}
