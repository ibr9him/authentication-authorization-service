package com.example.managementsystem

import com.example.managementsystem.ClientManagementServiceApplication
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.common.util.JacksonJsonParser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.notNullValue

@ActiveProfiles("Stub")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientManagementServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration
//@Import(ClientManagementTestsConfiguration.class)
class ClientManagementE2ETests {

    private static final baseApiUrl = 'http://localhost:90900/api/v1/clients'
    private static token = ''

    @Before
    void setup() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>()
        params.add("grant_type", "password")
        params.add("username", "test_user")
        params.add("password", "test_user")

        String resultString = given()
                .contentType("application/x-www-form-urlencoded")
                .header("Authorization", "Basic cG9zdG1hbjpwb3N0bWFu")
                .params(params)
                .post("/api/oauth/token")
                .andReturn().getBody().asString()
//                .accept("application/json;charset=UTF-8"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))

//        String resultString = result.andReturn().getResponse().getContentAsString()

        JacksonJsonParser jsonParser = new JacksonJsonParser()
        token = jsonParser.parseMap(resultString).get("access_token").toString()
    }

    @Test
    void "should create a new client"() {
        given().contentType("application/json")
                .body('{"name":"1","nameAr":"2"}')
                .post("$baseApiUrl/")
                .then()
                .body('id', notNullValue())
                .body('name', equalTo("1"))
                .body('nameAr', equalTo("2"))
                .body('enabled', equalTo(true))
//        expect: 'A new client created and returned'
//        mockMvc.perform(post("$baseApiUrl/")
//                .header("Authorization", "Bearer $token")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content('{"id":"", "name":"","nameAr":""}'))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath('$.id').isNotEmpty())
//                .andExpect(jsonPath('$.name').value(""))
//                .andExpect(jsonPath('$.nameAr').value(""))
    }
//
//    def "should update an existing client"() {
//        given:
//        def client = new JsonSlurper().parseText(mockMvc.perform(post("$baseApiUrl/")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content('{"id":"", "name":"","nameAr":""}'))
//                .andReturn().getResponse().contentAsString)
//
//        expect: 'A existing client updated and returned'
//        mockMvc.perform(put("$baseApiUrl/")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .content(/{"id": "$client.id", "name":"NAME","nameAr":"NAME_AR"}/))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath('$.name').value("NAME"))
//                .andExpect(jsonPath('$.nameAr').value("NAME_AR"))
//    }
//
//    def "should retrieve a existing client by id"() {
//        given:
//        def client = new JsonSlurper().parseText(mockMvc.perform(post("$baseApiUrl/")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content('{"id":"", "name":"NAME","nameAr":"NAME_AR"}'))
//                .andReturn().getResponse().contentAsString)
//
//        expect: 'A existing client with the given id returned'
//        mockMvc.perform(get("$baseApiUrl/$client.id")
//                .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath('$.id').value(client['id']))
//                .andExpect(jsonPath('$.name').value("NAME"))
//                .andExpect(jsonPath('$.nameAr').value("NAME_AR"))
//    }
//
//    def "should retrieve a filtered clients page"() {
//        given:
//        mockMvc.perform(post("$baseApiUrl/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME1","nameAr":""}'))
//        mockMvc.perform(post("$baseApiUrl/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME2","nameAr":""}'))
//        mockMvc.perform(post("$baseApiUrl/").contentType(MediaType.APPLICATION_JSON_VALUE).content('{"id":"", "name":"NAME3","nameAr":""}'))
//
//        expect: 'Any client with "1" in its name returned'
//        mockMvc.perform(get("$baseApiUrl?page=0&size=10&sort=id,desc&q=1")
//                .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath('$.totalElements').value(1))
//                .andExpect(jsonPath('$.pageable.sort.sorted').value(true))
//                .andExpect(jsonPath('$.content[0].name').value("NAME1"))
//    }
//
//    def "should delete an existing client by id"() {
//        given:
//        def client = new JsonSlurper().parseText(mockMvc.perform(post("$baseApiUrl/")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content('{"id":"", "name":"","nameAr":""}'))
//                .andReturn().getResponse().contentAsString)
//
//        expect: 'A existing client delete'
//        mockMvc.perform(delete("$baseApiUrl/$client.id")
//                .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//
//        mockMvc.perform(get("$baseApiUrl/$client.id")
//                .accept(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isNotFound())
//    }
}
