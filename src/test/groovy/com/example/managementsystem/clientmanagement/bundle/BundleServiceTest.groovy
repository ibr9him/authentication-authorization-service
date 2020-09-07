package com.example.managementsystem.clientmanagement.bundle

import com.example.managementsystem.authentication.AuthenticationUser
import com.example.managementsystem.clientmanagement.bundle.dto.BundleCreationDto
import com.example.managementsystem.clientmanagement.bundle.dto.BundleDto
import com.example.managementsystem.clientmanagement.bundle.dto.BundleUpdatingDto
import com.example.managementsystem.util.exception.ResourceKeyValueAlreadyExistsException
import com.example.managementsystem.util.exception.ResourceNotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

class BundleServiceTest extends Specification {

    AuthenticationUser authenticationUser = new AuthenticationUser("USERNAME", "PASSWORD", Collections.emptyList())

    BundleMapper mockBundleMapper = Mock(BundleMapper.class)
    BundleRepository mockBundleRepository = Mock(BundleRepository.class)
    @Subject
    BundleService bundleService = new BundleService(mockBundleRepository, mockBundleMapper)

    void setup() {
        authenticationUser.setClient(UUID.fromString("c1932a1c-0666-4832-90f4-06e2f3acbf1e"))
    }

    def "should save a new bundle"() {
        given:
        def bundleDto = BundleCreationDto.builder()
                .name('name')
                .nameAr('name_ar')
                .build()

        when:
        bundleService.save(bundleDto, authenticationUser)

        then:
        1 * mockBundleRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockBundleRepository.findOneByNameArIgnoreCase('name_ar') >> Optional.empty()
        1 * mockBundleMapper.toEntity(_ as BundleCreationDto) >> new BundleEntity()
        1 * mockBundleRepository.save(_ as BundleEntity) >> { args ->
            def b = args.get(0) as BundleEntity
            b.id = UUID.randomUUID()
            b
        }
        1 * mockBundleMapper.toBundleDto(_ as BundleEntity)
    }

    def "should not save a new bundle if name already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def bundleDto = BundleCreationDto.builder().name('NAME').nameAr('NAME_AR').build()

        when:
        bundleService.save(bundleDto, authenticationUser)

        then:
        1 * mockBundleRepository.findOneByNameIgnoreCase('NAME') >> Optional.of(new BundleEntity())
        0 * mockBundleRepository.findOneByNameArIgnoreCase('NAME_AR')
        0 * mockBundleMapper.toEntity(_ as BundleCreationDto)
        0 * mockBundleRepository.save(_ as BundleEntity)
        1 * mockBundleMapper.toBundleDto(_ as BundleEntity) >> BundleDto.builder().build()
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not save a new bundle if nameAr already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def bundleDto = BundleCreationDto.builder().name('NAME').nameAr('NAME_AR').build()

        when:
        bundleService.save(bundleDto, authenticationUser)

        then:
        1 * mockBundleRepository.findOneByNameIgnoreCase('NAME') >> Optional.empty()
        1 * mockBundleRepository.findOneByNameArIgnoreCase('NAME_AR') >> Optional.of(new BundleEntity())
        0 * mockBundleMapper.toEntity(_ as BundleCreationDto)
        0 * mockBundleRepository.save(_ as BundleEntity)
        1 * mockBundleMapper.toBundleDto(_ as BundleEntity) >> BundleDto.builder().build()
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should update an existing bundle"() {
        given:
        def id = UUID.randomUUID()
        def bundleDto = BundleUpdatingDto.builder()
                .id(id)
                .name('name')
                .nameAr('name_ar')
                .period('3 months')
                .limitedPeriod(false)
                .price('100.00')
                .currency('SAR')
                .limitedToNumberOfUsers(false)
                .numberOfUsersLimit(1)
                .numberOfUsers(1)
                .limitedToNumberOfClients(false)
                .numberOfClientsLimit(1)
                .numberOfClients(1)
                .enabled(true)
                .build()

        when:
        def bundle = bundleService.update(bundleDto, authenticationUser)

        then:
        1 * mockBundleRepository.findOneByNameIgnoreCase('name') >> Optional.empty()
        1 * mockBundleRepository.findOneByNameArIgnoreCase('name_ar') >> Optional.empty()
        1 * mockBundleRepository.findById(id) >> Optional.of(new BundleEntity())
        1 * mockBundleRepository.save(_ as BundleEntity) >> { args -> args.get(0) }
        1 * mockBundleMapper.toBundleDto(_ as BundleEntity) >> { args ->
            def r = args.get(0) as BundleEntity
            BundleDto.builder()
                    .id(r.id)
                    .name(r.name)
                    .nameAr(r.nameAr)
                    .period(r.period)
                    .limitedPeriod(r.limitedPeriod)
                    .price(r.price)
                    .currency(r.currency)
                    .limitedToNumberOfUsers(r.limitedToNumberOfUsers)
                    .numberOfUsersLimit(r.numberOfUsersLimit)
                    .numberOfUsers(r.numberOfUsers)
                    .limitedToNumberOfClients(r.limitedToNumberOfClients)
                    .numberOfClientsLimit(r.numberOfClientsLimit)
                    .numberOfClients(r.numberOfClients)
                    .enabled(r.enabled)
                    .build()
        }

        expect:
        bundle.id == id
        bundle.name == 'name'
        bundle.nameAr == 'name_ar'
        bundle.period == '3 months'
        !bundle.limitedPeriod
        bundle.price == '100.00'
        bundle.currency == 'SAR'
        !bundle.limitedToNumberOfUsers
        bundle.numberOfUsersLimit == 1
        bundle.numberOfUsers == 1
        !bundle.limitedToNumberOfClients
        bundle.numberOfClientsLimit == 1
        bundle.numberOfClients == 1
        bundle.enabled
    }

    def "should not update a non existing bundle and throw ResourceNotFoundException"() {
        given:
        def id = UUID.randomUUID()
        def bundleDto = BundleUpdatingDto.builder().id(id).name('NAME').nameAr('NAME_AR').build()

        when:
        bundleService.update(bundleDto, authenticationUser)

        then:
        1 * mockBundleRepository.findOneByNameIgnoreCase('NAME') >> Optional.empty()
        1 * mockBundleRepository.findOneByNameArIgnoreCase('NAME_AR') >> Optional.empty()
        1 * mockBundleRepository.findById(id) >> Optional.empty()
        0 * mockBundleRepository.save(_ as BundleEntity)
        0 * mockBundleMapper.toBundleDto(_ as BundleEntity)
        thrown(ResourceNotFoundException)
    }

    def "should not update an existing bundle if name already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def id = UUID.randomUUID()
        def bundleDto = BundleUpdatingDto.builder().id(id).name('NAME').nameAr('NAME_AR').build()

        when:
        bundleService.update(bundleDto, authenticationUser)

        then:
        1 * mockBundleRepository.findOneByNameIgnoreCase('NAME') >> Optional.of(new BundleEntity())
        0 * mockBundleRepository.findOneByNameArIgnoreCase('NAME_AR')
        0 * mockBundleRepository.findById(id)
        0 * mockBundleRepository.save(_ as BundleEntity)
        1 * mockBundleMapper.toBundleDto(_ as BundleEntity) >> BundleDto.builder().build()
        0 * mockBundleMapper.toBundleDto(_ as BundleEntity)
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should not update an existing bundle if nameAr already exists and throw ResourceKeyValueAlreadyExistsException"() {
        given:
        def id = UUID.randomUUID()
        def bundleDto = BundleUpdatingDto.builder().id(id).name('NAME').nameAr('NAME_AR').build()

        when:
        bundleService.update(bundleDto, authenticationUser)

        then:
        1 * mockBundleRepository.findOneByNameIgnoreCase('NAME') >> Optional.empty()
        1 * mockBundleRepository.findOneByNameArIgnoreCase('NAME_AR') >> Optional.of(new BundleEntity())
        0 * mockBundleRepository.findById(id)
        0 * mockBundleRepository.save(_ as BundleEntity)
        1 * mockBundleMapper.toBundleDto(_ as BundleEntity) >> BundleDto.builder().build()
        0 * mockBundleMapper.toBundleDto(_ as BundleEntity)
        thrown(ResourceKeyValueAlreadyExistsException)
    }

    def "should get a page of bundles for client"() {
        given:
        def searchQuery = ''
        def pageable = PageRequest.of(0, 10)

        when:
        def bundlesPage = bundleService.getAll(pageable, searchQuery, authenticationUser)

        then:
        1 * mockBundleRepository.findAll(pageable) >> {
            def bundle1 = new BundleEntity()
            def bundle2 = new BundleEntity()
            new PageImpl<>(List.of(bundle1, bundle2))
        }
        2 * mockBundleMapper.toBundleDto(_ as BundleEntity) >> BundleDto.builder().build()

        expect:
        bundlesPage.size == 2
        bundlesPage.number == 0
        bundlesPage.totalElements == 2
    }

    def "should get a page of bundles filtered by search query"() {
        given:
        def searchQuery = 'TEST'
        def pageable = PageRequest.of(0, 10)

        when:
        def bundlesPage = bundleService.getAll(pageable, searchQuery, authenticationUser)

        then:
        1 * mockBundleRepository.findAllBySearchQuery(searchQuery, pageable) >> {
            def bundle1 = new BundleEntity()
            def bundle2 = new BundleEntity()
            new PageImpl<>(List.of(bundle1, bundle2))
        }
        2 * mockBundleMapper.toBundleDto(_ as BundleEntity) >> BundleDto.builder().build()

        expect:
        bundlesPage.size == 2
        bundlesPage.number == 0
        bundlesPage.totalElements == 2
    }

    def "should get a existing bundle by id"() {
        given:
        def bundleId = UUID.randomUUID()

        when:
        def bundle = bundleService.get(bundleId, authenticationUser)

        then:
        1 * mockBundleRepository.findById(bundleId) >> { args ->
            def r = new BundleEntity()
            r.id = bundleId
            Optional.of(r)
        }
        1 * mockBundleMapper.toBundleDto(_ as BundleEntity) >> { args ->
            def u = args.get(0) as BundleEntity
            BundleDto.builder().id(u.id).build()
        }

        expect:
        bundle.id == bundleId
    }

    def "should not get a non existing bundle by id and throw ResourceNotFoundException"() {
        given:
        def bundleId = UUID.randomUUID()

        when:
        bundleService.get(bundleId, authenticationUser)

        then:
        1 * mockBundleRepository.findById(bundleId) >> Optional.empty()
        0 * mockBundleMapper.toBundleDto(_ as BundleEntity)
        thrown(ResourceNotFoundException)
    }

    def "should delete an existing bundle by id"() {
        given:
        def bundleId = UUID.randomUUID()

        when:
        bundleService.delete(bundleId, authenticationUser)

        then:
        1 * mockBundleRepository.findById(bundleId) >> { args ->
            def r = new BundleEntity()
            r.setId(bundleId)
            Optional.of(r)
        }
        1 * mockBundleRepository.delete(_ as BundleEntity) >> { args ->
            args.get(0).id == bundleId
        }
    }

    def "should not delete a non existing bundle by id and throw ResourceNotFoundException"() {
        given:
        def bundleId = UUID.randomUUID()

        when:
        bundleService.delete(bundleId, authenticationUser)

        then:
        1 * mockBundleRepository.findById(bundleId) >> Optional.empty()
        0 * mockBundleRepository.delete(_ as BundleEntity)
        thrown(ResourceNotFoundException)
    }
}
