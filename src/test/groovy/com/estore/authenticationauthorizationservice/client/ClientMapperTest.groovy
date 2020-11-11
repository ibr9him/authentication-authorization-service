package com.estore.authenticationauthorizationservice.client

import com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.client.dto.ClientUpdatingDto
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant

class ClientMapperTest extends Specification {

    @Subject
    ClientMapper clientMapper = new ClientMapperImpl()

    def "should convert from entity to dto"() {
        given:
        def id = UUID.randomUUID()
        def createdDate = Instant.now()
        def clientEntity = new ClientEntity()
        clientEntity.id = id
        clientEntity.name = 'NAME'
        clientEntity.nameAr = 'NAME_AR'
        clientEntity.contactInfo = '{}'
        clientEntity.properties = '{}'
        clientEntity.createdDate = createdDate
        clientEntity.enabled = true

        when:
        def clientDto = clientMapper.toClientDto(clientEntity)

        then:
        assert clientDto instanceof ClientDto
        clientDto.id == id
        clientDto.name == 'NAME'
        clientDto.nameAr == 'NAME_AR'
        clientDto.contactInfo == '{}'
        clientDto.properties == '{}'
        clientDto.createdDate == createdDate
        clientDto.enabled
    }

    def "should convert from creation dto to entity"() {
        given:
        def clientDto = ClientCreationDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .enabled(true)
                .build()

        when:
        def clientEntity = clientMapper.toEntity(clientDto)

        then:
        assert clientEntity instanceof ClientEntity
        clientEntity.name == 'NAME'
        clientEntity.nameAr == 'NAME_AR'
        clientEntity.contactInfo == '{}'
        clientEntity.properties == '{}'
        clientEntity.enabled
    }

    def "should convert from updating dto to entity"() {
        def id = UUID.randomUUID()
        given:
        def clientDto = ClientUpdatingDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .enabled(true)
                .build()

        when:
        def clientEntity = clientMapper.toEntity(clientDto)

        then:
        assert clientEntity instanceof ClientEntity
        clientEntity.id == id
        clientEntity.name == 'NAME'
        clientEntity.nameAr == 'NAME_AR'
        clientEntity.contactInfo == '{}'
        clientEntity.properties == '{}'
        clientEntity.enabled
    }

    def "should convert from details dto to entity"() {
        given:
        def createdDate = Instant.now()
        def id = UUID.randomUUID()
        def clientDto = ClientDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .contactInfo('{}')
                .properties('{}')
                .enabled(true)
                .createdDate(createdDate)
                .build()

        when:
        def clientEntity = clientMapper.toEntity(clientDto)

        then:
        assert clientEntity instanceof ClientEntity
        clientEntity.id == id
        clientEntity.name == 'NAME'
        clientEntity.nameAr == 'NAME_AR'
        clientEntity.contactInfo == '{}'
        clientEntity.properties == '{}'
        clientEntity.createdDate == createdDate
        clientEntity.enabled
    }
}
