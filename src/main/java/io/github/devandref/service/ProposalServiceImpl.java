package io.github.devandref.service;

import io.github.devandref.dto.ProposalDTO;
import io.github.devandref.dto.ProposalDetailsDTO;
import io.github.devandref.entity.ProposalEntity;
import io.github.devandref.message.KafkaEvent;
import io.github.devandref.repository.ProposalRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;

public class ProposalServiceImpl implements ProposalService {

    @Inject
    private ProposalRepository proposalRepository;

    @Inject
    private KafkaEvent kafkaMessages;

    @Override
    public ProposalDetailsDTO findFullProposal(long id) {
        ProposalEntity proposal = proposalRepository.findById(id);
        return ProposalDetailsDTO.builder()
                .proposalId(proposal.getId())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .country(proposal.getCountry())
                .priceTonne(proposal.getPriceTonne())
                .customer(proposal.getCustomer())
                .tonnes(proposal.getTonnes())
                .build();
    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        ProposalDTO proposal = buildAndSaveNewProposal(proposalDetailsDTO);
        kafkaMessages.sendNewKafkaEvent(proposal);
    }

    @Override
    public void removeProposal(long id) {
        proposalRepository.deleteById(id);
    }

    @Transactional
    private ProposalDTO buildAndSaveNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
            ProposalEntity proposal = new ProposalEntity();
            proposal.setCountry(LocalDate.now().toString());
            proposal.setProposalValidityDays(proposalDetailsDTO.getProposalValidityDays());
            proposal.setCountry(proposalDetailsDTO.getCountry());
            proposal.setCustomer(proposalDetailsDTO.getCustomer());
            proposal.setPriceTonne(proposalDetailsDTO.getPriceTonne());
            proposal.setTonnes(proposalDetailsDTO.getTonnes());

            proposalRepository.persist(proposal);
            return ProposalDTO.builder()
                    .proposalId(proposalRepository.findByCustomer(proposal.getCustomer()).get().getId())
                    .priceTonne(proposal.getPriceTonne())
                    .customer(proposal.getCustomer())
                    .build();

    }
}
