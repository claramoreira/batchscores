package br.com.claramoreira.batchscores.batchscores.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import br.com.claramoreira.batchscores.batchscores.step.StepConfig;

@EnableBatchProcessing
@Configuration
@Import(StepConfig.class)
public class JobConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Bean
    public Job job(Step step, Step step2) {
        return jobs.get("job")
                .start(step)
                .next(step2)
                .build();
    }

}