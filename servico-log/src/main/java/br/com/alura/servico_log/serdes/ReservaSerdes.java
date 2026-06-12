package br.com.alura.servico_log.serdes;

import br.com.alura.servico_log.dto.DadosReserva;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class ReservaSerdes extends Serdes.WrapperSerde<DadosReserva> {

    public ReservaSerdes() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(DadosReserva.class));
    }

    public static Serde<DadosReserva>  serdes() {
        JsonSerializer<DadosReserva> serializer = new JsonSerializer<>();
        JsonDeserializer<DadosReserva> desserializer = new JsonDeserializer<>(DadosReserva.class);
        return Serdes.serdeFrom(serializer, desserializer);
    }
}
