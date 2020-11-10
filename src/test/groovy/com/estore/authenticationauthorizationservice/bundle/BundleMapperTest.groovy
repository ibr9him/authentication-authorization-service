package com.estore.authenticationauthorizationservice.bundle

import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto
import spock.lang.Specification
import spock.lang.Subject

class BundleMapperTest extends Specification {

    @Subject
    BundleMapper bundleMapper = new BundleMapperImpl()

    def "should convert from bundle dto to entity"() {
        given:
        def id = UUID.randomUUID()
        def bundleDto = BundleDto.builder()
                .id(id)
                .name('NAME')
                .nameAr('NAME_AR')
                .period('3')
                .limitedPeriod(true)
                .price('100.00')
                .currency('SAR')
                .limitedToNumberOfUsers(true)
                .numberOfUsersLimit(5)
                .numberOfUsers(3)
                .limitedToNumberOfClients(true)
                .numberOfClientsLimit(5)
                .numberOfClients(2)
                .enabled(true)
                .build()

        when:
        def bundleEntity = bundleMapper.toEntity(bundleDto)

        then:
        bundleEntity.id == id
        bundleEntity.name == 'NAME'
        bundleEntity.nameAr == 'NAME_AR'
        bundleEntity.period == '3'
        bundleEntity.limitedPeriod
        bundleEntity.price == '100.00'
        bundleEntity.currency == 'SAR'
        bundleEntity.limitedToNumberOfUsers
        bundleEntity.numberOfUsersLimit == 5
        bundleEntity.numberOfUsers == 3
        bundleEntity.limitedToNumberOfClients
        bundleEntity.numberOfClientsLimit == 5
        bundleEntity.numberOfClients == 2
        bundleEntity.enabled
    }

    def "should convert from bundle creation dto to entity"() {
        given:
        def bundleDto = BundleDto.builder()
                .name('NAME')
                .nameAr('NAME_AR')
                .period('3')
                .limitedPeriod(true)
                .price('100.00')
                .currency('SAR')
                .limitedToNumberOfUsers(true)
                .numberOfUsersLimit(5)
                .numberOfUsers(3)
                .limitedToNumberOfClients(true)
                .numberOfClientsLimit(5)
                .numberOfClients(2)
                .enabled(true)
                .build()

        when:
        def bundleEntity = bundleMapper.toEntity(bundleDto)

        then:
        bundleEntity.name == 'NAME'
        bundleEntity.nameAr == 'NAME_AR'
        bundleEntity.period == '3'
        bundleEntity.limitedPeriod
        bundleEntity.price == '100.00'
        bundleEntity.currency == 'SAR'
        bundleEntity.limitedToNumberOfUsers
        bundleEntity.numberOfUsersLimit == 5
        bundleEntity.numberOfUsers == 3
        bundleEntity.limitedToNumberOfClients
        bundleEntity.numberOfClientsLimit == 5
        bundleEntity.numberOfClients == 2
        bundleEntity.enabled
    }

    def "should convert from entity to bundle dto"() {

        given:
        def id = UUID.randomUUID()
        def bundleEntity = new BundleEntity()
        bundleEntity.setId(id)
        bundleEntity.setName('NAME')
        bundleEntity.setNameAr('NAME_AR')
        bundleEntity.setPeriod('3')
        bundleEntity.setLimitedPeriod(true)
        bundleEntity.setPrice('100.00')
        bundleEntity.setCurrency('SAR')
        bundleEntity.setLimitedToNumberOfUsers(true)
        bundleEntity.setNumberOfUsersLimit(5)
        bundleEntity.setNumberOfUsers(3)
        bundleEntity.setLimitedToNumberOfClients(true)
        bundleEntity.setNumberOfClientsLimit(5)
        bundleEntity.setNumberOfClients(2)
        bundleEntity.setEnabled(true)

        when:
        def bundleDto = bundleMapper.toBundleDto(bundleEntity)

        then:
        bundleDto.id == id
        bundleDto.name == 'NAME'
        bundleDto.nameAr == 'NAME_AR'
        bundleDto.period == '3'
        bundleDto.limitedPeriod
        bundleDto.price == '100.00'
        bundleDto.currency == 'SAR'
        bundleDto.limitedToNumberOfUsers
        bundleDto.numberOfUsersLimit == 5
        bundleDto.numberOfUsers == 3
        bundleDto.limitedToNumberOfClients
        bundleDto.numberOfClientsLimit == 5
        bundleDto.numberOfClients == 2
        bundleDto.enabled
    }
}
