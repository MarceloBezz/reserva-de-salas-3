package br.com.alura.batch.config.batch;

import br.com.alura.batch.service.ReservaCleanup;
import br.com.alura.batch.service.ReservaRelatorio;
import br.com.alura.batch.service.ReservaReminder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
//@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job manutencaoDiariaJob(Step removerReservasExpiradasStep, Step enviarLembretesReservaStep,
                   Step gerarRelatorioReservasStep, JobRepository jobRepository) {
        return new JobBuilder("job-diario", jobRepository)
                .start(removerReservasExpiradasStep)
                .next(enviarLembretesReservaStep)
                .next(gerarRelatorioReservasStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step removerReservasExpiradasStep(JobRepository repository, PlatformTransactionManager transactionManager,
                                             ReservaCleanup service) {
        return new StepBuilder("removerReservasExpiradasStep", repository)
                .tasklet((contribution, context) -> {
                    service.removerReservasExpiradas().block();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step enviarLembretesReservaStep(JobRepository repository, PlatformTransactionManager transactionManager,
                                           ReservaReminder service) {
        return new StepBuilder("enviarLembretesReservaStep", repository)
                .tasklet((contribution, context) -> {
                    service.enviarLembretes().block();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step gerarRelatorioReservasStep(JobRepository repository, PlatformTransactionManager transactionManager,
                                           ReservaRelatorio service) {
        return new StepBuilder("gerarRelatorioReservasStep", repository)
                .tasklet((contribution, context) -> {
                    service.gerarRelatorios().block();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
