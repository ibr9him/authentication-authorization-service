package com.estore.authenticationauthorizationservice.client

import com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.client.dto.ClientUpdatingDto
import com.estore.authenticationauthorizationservice.util.exception.ResourceKeyValueAlreadyExistsException
import com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

class ClientServiceTest extends Specification {

    ClientRepository mockClientRepository = Mock(ClientRepository.class)
    ClientMapper mockClientMapper = Mock(ClientMapper.class)
    @Subject
    ClientService clientService = new ClientService(mockClientRepository, mockClientMapper)

    def "should save a new client"() {
        given:
        def clientDto = ClientCreationDto.builder().name('name').nameAr('nameAr').build()

        when:
        clientService.save(clientDto)

        then:
        1 * mockClientRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockClientRepository.findOneByNameArIgnoreCase('nameAr') >> Optional.empty()
        1 * mockClientMapper.toEntity(_ as ClientCreationDto) >> new ClientEntity()
        1 * mockClientRepository.save(_ as ClientEntity) >> {
            def mockClient = new ClientEntity()
            mockClient.setId(UUID.randomUUID())
            mockClient
        }
        1 * mockClientMapper.toClientDto(_ as ClientEntity)
    }

    def "should not save a new client if name already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def clientDto = ClientCreationDto.builder().name('name').nameAr('nameAr').build()

        when:
        clientService.save(clientDto)

        then:
        1 * mockClientRepository.findOneByNameIgnoreCase('name') >> Optional.of(new ClientEntity())
        0 * mockClientRepository.findOneByNameArIgnoreCase('nameAr')
        0 * mockClientMapper.toEntity(_ as ClientCreationDto)
        0 * mockClientRepository.save(_ as ClientEntity)
        0 * mockClientMapper.toClientDto(_ as ClientEntity)
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not save a new client if nameAr already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def clientDto = ClientCreationDto.builder().name('name').nameAr('nameAr').build()

        when:
        clientService.save(clientDto)

        then:
        1 * mockClientRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockClientRepository.findOneByNameArIgnoreCase('nameAr') >> Optional.of(new ClientEntity())
        0 * mockClientMapper.toEntity(_ as ClientCreationDto)
        0 * mockClientRepository.save(_ as ClientEntity)
        0 * mockClientMapper.toClientDto(_ as ClientEntity)
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should update an existing client"() {
        given:
        def id = UUID.randomUUID()
        ClientUpdatingDto clientDto = ClientUpdatingDto.builder()
                .id(id)
                .name('name')
                .nameAr('nameAr')
                .contactInfo('{}')
                .properties('{}')
                .enabled(true)
                .build()

        when:
        def updatedClient = clientService.update(clientDto)

        then:
        1 * mockClientRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockClientRepository.findOneByNameArIgnoreCase('nameAr') >> Optional.empty()
        1 * mockClientRepository.findById(id) >> Optional.of(new ClientEntity())
        1 * mockClientRepository.save(_ as ClientEntity) >> { args -> args.get(0) }
        1 * mockClientMapper.toClientDto(_ as ClientEntity) >> { args ->
            def c = args.get(0) as ClientEntity
            ClientDto.builder().id(c.id).name(c.name).nameAr(c.nameAr).contactInfo(c.contactInfo).properties(c.properties).enabled(c.enabled).build()
        }

        expect:
        updatedClient.name == 'name'
        updatedClient.nameAr == 'nameAr'
        updatedClient.contactInfo == '{}'
        updatedClient.properties == '{}'
        updatedClient.enabled
    }

    def "should not update an existing client if name already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def id = UUID.randomUUID()
        ClientUpdatingDto clientDto = ClientUpdatingDto.builder().id(id).name('name').nameAr('nameAr').build()

        when:
        clientService.update(clientDto)

        then:
        1 * mockClientRepository.findOneByNameIgnoreCase('name') >> {
            def entity = new ClientEntity()
            entity.setId(UUID.randomUUID())
            Optional.of(entity)
        }
        0 * mockClientRepository.findOneByNameArIgnoreCase('nameAr') >> Optional.empty()
        0 * mockClientRepository.findById(id) >> Optional.empty()
        0 * mockClientRepository.save(_ as ClientEntity)
        0 * mockClientMapper.toClientDto(_ as ClientEntity)
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not update an existing client if nameAr already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def id = UUID.randomUUID()
        ClientUpdatingDto clientDto = ClientUpdatingDto.builder().id(id).name('name').nameAr('nameAr').build()

        when:
        clientService.update(clientDto)

        then:
        1 * mockClientRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockClientRepository.findOneByNameArIgnoreCase('nameAr') >> Optional.of(new ClientEntity())
        0 * mockClientRepository.findById(id) >> Optional.empty()
        0 * mockClientRepository.save(_ as ClientEntity)
        0 * mockClientMapper.toClientDto(_ as ClientEntity)
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not update a non existing client and throw ResourceNotFoundException"() {
        given:
        def id = UUID.randomUUID()
        ClientUpdatingDto clientDto = ClientUpdatingDto.builder().id(id).name('name').nameAr('nameAr').build()

        when:
        clientService.update(clientDto)

        then:
        1 * mockClientRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockClientRepository.findOneByNameArIgnoreCase('nameAr') >> Optional.empty()
        1 * mockClientRepository.findById(id) >> Optional.empty()
        0 * mockClientRepository.save(_ as ClientEntity)
        0 * mockClientMapper.toClientDto(_ as ClientEntity)
        thrown(ResourceNotFoundException)
    }

    def "should get a client given by id"() {
        given:
        def clientId = UUID.randomUUID()

        when:
        def client = clientService.get(clientId)

        then:
        1 * mockClientRepository.findById(clientId) >> {
            def mockClient = new ClientEntity()
            mockClient.setId(clientId)
            mockClient.setName("NAME")
            mockClient.setNameAr("NAME_AR")
            Optional.of(mockClient)
        }
        1 * mockClientMapper.toClientDto(_ as ClientEntity) >> { args ->
            def c = args.get(0) as ClientEntity
            ClientDto.builder().id(c.id).name(c.name).nameAr(c.nameAr).build()
        }

        expect:
        client.id == clientId
    }

    def "should not get a non existing client by id and throw ResourceNotFoundException"() {
        given:
        def clientId = UUID.randomUUID()

        when:
        clientService.get(clientId)

        then:
        1 * mockClientRepository.findById(_ as UUID) >> Optional.empty()
        thrown(ResourceNotFoundException)
    }

    def "should get a clients page"() {
        given:
        def searchQuery = ''
        def pageable = PageRequest.of(0, 2)

        when:
        def clientsPage = clientService.getAll(pageable, searchQuery)

        then:
        1 * mockClientRepository.findAll(pageable) >> {
            def client1 = new ClientEntity()
            client1.setId(UUID.randomUUID())
            def client2 = new ClientEntity()
            client2.setId(UUID.randomUUID())
            new PageImpl<>(List.of(client1, client2))
        }
        2 * mockClientMapper.toClientDto(_ as ClientEntity) >> ClientDto.builder().build()

        expect:
        clientsPage.size == 2
        clientsPage.totalElements == 2
    }

    def "should get a clients page filtered by search query"() {
        given:
        def searchQuery = 'NAME'
        def pageable = PageRequest.of(0, 2)

        when:
        def clientsPage = clientService.getAll(pageable, 'NAME')

        then:
        1 * mockClientRepository.findBySearchQuery(pageable, searchQuery) >> {
            def client1 = new ClientEntity()
            client1.setId(UUID.randomUUID())
            def client2 = new ClientEntity()
            client2.setId(UUID.randomUUID())
            new PageImpl<>(List.of(client1, client2))
        }
        2 * mockClientMapper.toClientDto(_ as ClientEntity)

        expect:
        clientsPage.size == 2
        clientsPage.totalElements == 2
    }

    def "should delete an existing client"() {
        given:
        def id = UUID.randomUUID()
        def client = ClientDto.builder().id(id).build()
        def clientEntity = new ClientEntity()

        when:
        clientService.delete(id)

        then:
        1 * mockClientRepository.findById(client.id) >> Optional.of(clientEntity)
        1 * mockClientRepository.delete(clientEntity)
    }
}
