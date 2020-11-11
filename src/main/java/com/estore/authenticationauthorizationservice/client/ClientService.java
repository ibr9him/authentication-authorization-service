package com.estore.authenticationauthorizationservice.client;

import com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientUpdatingDto;
import com.estore.authenticationauthorizationservice.util.exception.ResourceKeyValueAlreadyExistsException;
import com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientDto save(ClientCreationDto clientDto) {
        log.info("Checking activity fields ...");

        getByName(clientDto.getName()).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("name", s.getName());
        });
        getByNameAr(clientDto.getNameAr()).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("nameAr", s.getNameAr());
        });

        log.info("Saving client....");
        ClientEntity existingClientEntity = clientRepository.save(clientMapper.toEntity(clientDto));
        log.info("Saving client done. Client id: {}", existingClientEntity.getId());
        return clientMapper.toClientDto(existingClientEntity);
    }

    @Transactional
    public ClientDto update(ClientUpdatingDto clientDto) {
        log.info("Checking client fields ...");

        getByName(clientDto.getName())
                .filter(client -> client.getId() != clientDto.getId())
                .ifPresent(client -> {
                    throw new ResourceKeyValueAlreadyExistsException("name", client.getName());
                });
        getByNameAr(clientDto.getNameAr())
                .filter(client -> client.getId() != clientDto.getId())
                .ifPresent(client -> {
                    throw new ResourceKeyValueAlreadyExistsException("nameAr", client.getNameAr());
                });

        log.info("Updating client....");
        return clientRepository.findById(clientDto.getId())
                .map((existingClientEntity) -> {
                    log.info("Old client values: {}", existingClientEntity);
                    log.info("New client values: {}", clientDto);
                    existingClientEntity.setName(clientDto.getName().trim());
                    existingClientEntity.setNameAr(clientDto.getNameAr().trim());
                    existingClientEntity.setContactInfo(clientDto.getContactInfo());
                    existingClientEntity.setProperties(clientDto.getProperties());
                    existingClientEntity.setEnabled(clientDto.isEnabled());
                    existingClientEntity = clientRepository.save(existingClientEntity);
                    log.info("Updating client done.");
                    return existingClientEntity;
                })
                .map(clientMapper::toClientDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> getAll(Pageable pageable, String searchQuery) {
        Page<ClientDto> clientPage;
        if (!searchQuery.isEmpty()) {
            log.info("Getting all clients filtered by query: {}, and page: {} ....", searchQuery, pageable);
            clientPage = clientRepository.findBySearchQuery(pageable, searchQuery).map(clientMapper::toClientDto);
        } else {
            log.info("Getting clients page: {} ....", pageable);
            clientPage = clientRepository.findAll(pageable).map(clientMapper::toClientDto);
        }
        log.info("Getting all clients done found: {}.", clientPage.getTotalElements());
        return clientPage;
    }

    @Transactional(readOnly = true)
    public ClientDto get(UUID id) {
        log.info("Getting client by id: {} ....", id);
        return clientRepository.findById(id)
                .map(clientEntity -> {
                    log.info("Getting client by id found: {}.", clientEntity);
                    return clientEntity;
                })
                .map(clientMapper::toClientDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void delete(UUID clientId) {
        log.info("Deleting client with id: {} ....", clientId);
        clientRepository.findById(clientId)
                .map(existingUser -> {
                    clientRepository.delete(existingUser);
                    log.info("Deleting user: {} done.", existingUser);
                    return existingUser;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    private Optional<ClientEntity> getByName(String name) {
        log.info("Getting client by name: {} ...", name);
        Optional<ClientEntity> client = clientRepository.findOneByNameIgnoreCase(name);

        if (client.isPresent()) log.info("Getting client by name found: {}.", client);
        else log.error("Getting client by name: {} not found.", name);

        return client;
    }

    private Optional<ClientEntity> getByNameAr(String nameAr) {
        log.info("Getting client by nameAr: {} ...", nameAr);
        Optional<ClientEntity> client = clientRepository.findOneByNameArIgnoreCase(nameAr);

        if (client.isPresent()) log.info("Getting client by nameAr found: {}.", client);
        else log.error("Getting client by nameAr: {} not found.", nameAr);

        return client;
    }
}
