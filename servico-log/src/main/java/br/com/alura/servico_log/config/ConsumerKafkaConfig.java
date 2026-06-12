package br.com.alura.servico_log.config;

import br.com.alura.servico_log.dto.DadosReserva;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
//@EnableKafkaStreams
public class ConsumerKafkaConfig {

//    @Value(value = "${spring.kafka.bootstrap-servers:localhost:9092}")
    @Value(value = "${spring.kafka.bootstrap-servers:kafka:9092}")
    private String bootstrapAddress;

//    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
//    public KafkaStreamsConfiguration kafkaStreamsConfig() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-demo-6");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
////        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
////        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, ReservaSerdes.class);
//        return new KafkaStreamsConfiguration(props);
//    }

    @Bean
    public ConsumerFactory<String, DadosReserva> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);
        props.put(
                JsonDeserializer.VALUE_DEFAULT_TYPE,
                DadosReserva.class.getName()
        );
        props.put(
                JsonDeserializer.TRUSTED_PACKAGES,
                "*");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DadosReserva> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory <String, DadosReserva> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return  factory;
    }
}
