package com.example.managementsystem.clientmanagement.structure

import com.example.managementsystem.clientmanagement.client.ClientEntity
import com.example.managementsystem.clientmanagement.structure.StructureLevelEntity
import com.example.managementsystem.clientmanagement.structure.StructureLevelRepository
import com.example.managementsystem.config.DataAccessConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityManager

@ActiveProfiles("Test")
@DataJpaTest
@Import(DataAccessConfiguration.class)
class StructureLevelRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    StructureLevelRepository structureLevelRepository

    def cleanup() {
        println('Cleaning up after a test!')
        structureLevelRepository.deleteAll()
    }

    def "should save a structure level to the database with auditing info"() {
        given:
        def structureLevel = new StructureLevelEntity()
        structureLevel.setName("NAME")
        structureLevel.setNameAr("NAME_AR")

        when:
        def persistedStructureLevel = structureLevelRepository.save(structureLevel)

        then:
        def retrievedClient = entityManager.find(StructureLevelEntity, persistedStructureLevel.id)
        retrievedClient.id == persistedStructureLevel.id
        retrievedClient.createdBy == 'Dummy User'
        retrievedClient.createdDate != null
        retrievedClient.lastModifiedBy == 'Dummy User'
        retrievedClient.lastModifiedDate != null
    }

    def "should retrieve structure level by id and clientId"() {
        given:
        def setupClient = new ClientEntity()
        setupClient.setName("TEST")
        setupClient.setNameAr("TEST_AR")
        entityManager.persist(setupClient)
        def client = entityManager.createQuery("select c from ClientEntity c where c.name='TEST'", ClientEntity.class).getSingleResult()
        def setupStructureLevel = new StructureLevelEntity()
        setupStructureLevel.setName("NAME")
        setupStructureLevel.setNameAr("NAME_AR")
        setupStructureLevel.setClient(client)
        entityManager.persist(setupStructureLevel)
        def structureLevel = entityManager.createQuery("select s from StructureLevelEntity s where s.name='NAME'", StructureLevelEntity.class).getSingleResult()

        when:
        def persistedStructureLevel = structureLevelRepository.findOneByIdAndClient_Id(structureLevel.id, client.id).get()

        then:
        persistedStructureLevel.id == structureLevel.id
        persistedStructureLevel.name == "NAME"
        persistedStructureLevel.nameAr == "NAME_AR"
        persistedStructureLevel.client.id == client.id
    }

    def "should retrieve structure level with parent and children"() {
        given:
        def setupClient = new ClientEntity()
        setupClient.setName("TEST")
        setupClient.setNameAr("TEST_AR")
        entityManager.persist(setupClient)
        def client = entityManager.createQuery("select c from ClientEntity c where c.name='TEST'", ClientEntity.class).getSingleResult()

        def setupParentStructureLevel = new StructureLevelEntity()
        setupParentStructureLevel.setName("PARENT")
        setupParentStructureLevel.setNameAr("PARENT_AR")
        setupParentStructureLevel.setClient(client)
        entityManager.persist(setupParentStructureLevel)
        def parentStructureLevel = entityManager.createQuery("select s from StructureLevelEntity s where s.name='PARENT'", StructureLevelEntity.class).getSingleResult()

        def setupChildStructureLevel1 = new StructureLevelEntity()
        setupChildStructureLevel1.setName("CHILD1")
        setupChildStructureLevel1.setNameAr("CHILD1_AR")
        setupChildStructureLevel1.setClient(client)
        entityManager.persist(setupChildStructureLevel1)
        def childStructureLevel1 = entityManager.createQuery("select s from StructureLevelEntity s where s.name='CHILD1'", StructureLevelEntity.class).getSingleResult()

        def setupChildStructureLevel2 = new StructureLevelEntity()
        setupChildStructureLevel2.setName("CHILD2")
        setupChildStructureLevel2.setNameAr("CHILD2_AR")
        setupChildStructureLevel2.setClient(client)
        entityManager.persist(setupChildStructureLevel2)
        def childStructureLevel2 = entityManager.createQuery("select s from StructureLevelEntity s where s.name='CHILD2'", StructureLevelEntity.class).getSingleResult()

        def targetStructureLevel2 = new StructureLevelEntity()
        targetStructureLevel2.setName("TARGET")
        targetStructureLevel2.setNameAr("TARGET_AR")
        targetStructureLevel2.setClient(client)
        targetStructureLevel2.setParent(parentStructureLevel)
        targetStructureLevel2.setChildren(Set.of(childStructureLevel1, childStructureLevel2))
        entityManager.persist(targetStructureLevel2)
        def structureLevel = entityManager.createQuery("select s from StructureLevelEntity s where s.name='TARGET'", StructureLevelEntity.class).getSingleResult()

        when:
        def persistedStructureLevel = structureLevelRepository.findOneByIdAndClient_Id(structureLevel.id, client.id).get()

        then:
        persistedStructureLevel.id == structureLevel.id
        persistedStructureLevel.parent.name == 'PARENT'
        persistedStructureLevel.children.size() == 2
    }
}
