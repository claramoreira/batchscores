package br.com.claramoreira.batchscores.batchscores.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import br.com.claramoreira.batchscores.batchscores.step.FirstStepConfig;

@EnableBatchProcessing
@Configuration
@Import(FirstStepConfig.class)
public class JobCsvXml {
	
	@Autowired
    private JobBuilderFactory jobs;
	
	@Bean
    public Job job(Step step3) {
        return jobs.get("jobCsvXml").incrementer(new RunIdIncrementer()).flow(step3).end().build();
    }


}
