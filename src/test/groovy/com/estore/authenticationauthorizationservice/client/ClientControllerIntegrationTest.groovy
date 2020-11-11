package com.estore.authenticationauthorizationservice.client

import com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto
import com.estore.authenticationauthorizationservice.client.dto.ClientDto
import com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@ActiveProfiles("Test")
@WebMvcTest(controllers = [ClientController.class])
class ClientControllerIntegrationTest extends Specification {

    def baseApiUrl = '/v1/clients'

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private ClientService mockClientService

    def "should save a client"() {
        given:
        def clientToSave = '{"name":"NAME", "nameAr":"NAME_AR", "contactInfo": {}, "properties": {}}'

        when:
        def response = mockMvc.perform(post("$baseApiUrl/")
                .content(clientToSave)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse()

        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockClientService.save(_ as ClientCreationDto) >> ClientDto.builder().id(UUID.randomUUID()).build()

        expect:
        response.status == CREATED.value()
        content.id != null
    }

    def "should not save a client when id is given in the request"() {
        given:
        def clientToSave = '{"id":"' + UUID.randomUUID() + '", "name":"NAME", "nameAr":"NAME_AR"}'

        when:
        def response = mockMvc.perform(post("$baseApiUrl/")
                .content(clientToSave)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockClientService.save(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should not save client when empty json is given in the request"() {
        given:
        def clientToSave = '{}'

        when:
        def response = mockMvc.perform(post("$baseApiUrl/")
                .content(clientToSave)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockClientService.save(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should update a client"() {
        given:
        def clientToUpdate = '{"id":"1", "name":"NAME", "nameAr":"NAME_AR"}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(clientToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockClientService.update(_) >> ClientBaseDto.builder().id("1").name("NAME").nameAr("NAME_AR").build()

        expect:
        response.status == OK.value()
        content.id == "1"
        content.name == "NAME"
        content.nameAr == "NAME_AR"
    }

    def "should not update a client when id isn't given in the request"() {
        given:
        def clientToUpdate = '{"id":""}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(clientToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockClientService.update(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should not update a client when id does not exist"() {
        given:
        def clientToUpdate = '{"id":"ID", "name":"", "nameAr":""}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(clientToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.update(_) >> { throw new ResourceNotFoundException() }

        expect:
        response.status == NOT_FOUND.value()
    }

    def "should not update client when empty json is given in the request"() {
        given:
        def clientToUpdate = '{}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(clientToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockClientService.update(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should return a client given its id"() {
        given:
        def clientId = UUID.randomUUID()

        when:
        def response = mockMvc.perform(get("$baseApiUrl/$clientId")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockClientService.get("1") >> ClientDto.builder().id(clientId).build()

        expect:
        response.status == OK.value()
        content.id == "1"
    }

    def "should return not found 404 if client id does not exist"() {
        given:
        def clientId = "1"

        when:
        def response = mockMvc.perform(get("$baseApiUrl/$clientId")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.get("1") >> { throw new ResourceNotFoundException() }

        expect:
        response.status == NOT_FOUND.value()
    }

    def "should return a clients page"() {
        when:
        def response = mockMvc.perform(get("$baseApiUrl")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.getAll(_ as Pageable, "") >> { new PageImpl<>(List.of(ClientDto.builder().build())) }

        expect:
        response.status == OK.value()
    }

    def "should return a clients page filter by search query"() {
        when:
        def response = mockMvc.perform(get("$baseApiUrl")
                .queryParam("q", "test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.getAll(_ as Pageable, "test") >> { new PageImpl<>(List.of(new ClientEntity())) }

        expect:
        response.status == OK.value()
    }

    def "should delete a client given its id"() {
        given:
        def clientToDelete = '1'

        when:
        def response = mockMvc.perform(delete("$baseApiUrl/$clientToDelete")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.get("1")
        1 * mockClientService.delete(_)

        expect:
        response.status == OK.value()
    }

    def "should not delete a client when id does not exist"() {
        given:
        def clientToDelete = 'ID'

        when:
        def response = mockMvc.perform(delete("$baseApiUrl/$clientToDelete")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.get("ID") >> { throw new ResourceNotFoundException() }
        0 * mockClientService.delete(_)

        expect:
        response.status == NOT_FOUND.value()
    }
}
