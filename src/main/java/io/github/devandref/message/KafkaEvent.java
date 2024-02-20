package io.github.devandref.message;

import io.github.devandref.dto.ProposalDTO;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class KafkaEvent {

    @Channel("proposal")
    private Emitter<ProposalDTO> proposalRequestEmitter;

    public void sendNewKafkaEvent(ProposalDTO proposalDTO) {
        Log.info("--- Enviando Nova Proposta para Tópico Kafka ---");
        proposalRequestEmitter.send(proposalDTO).toCompletableFuture().join();
    }


}
