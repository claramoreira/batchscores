package br.com.claramoreira.batchscores.batchscores.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import br.com.claramoreira.batchscores.batchscores.step.WineStepConfig;

@EnableBatchProcessing
@Configuration
@Import(WineStepConfig.class)
public class JobWine {

	@Autowired
    private JobBuilderFactory jobs;
	
	@Bean
	@Qualifier("wineJob")
    public Job job(Step wineStep) {
        return jobs.get("wineJob").incrementer(new RunIdIncrementer()).start(wineStep).build();
    }

}
