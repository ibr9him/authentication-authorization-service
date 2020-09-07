package com.example.managementsystem.config;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Profile(value = {"!Test", "!Stub"})
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .paths(PathSelectors.ant("/v1*"))
                .apis(RequestHandlerSelectors.basePackage("com.example.managementsystem"))
                .build()
                .apiInfo(apiInfoDetails())
                .useDefaultResponseMessages(false)
                .directModelSubstitute(Pageable.class, SwaggerPageable.class)
                .directModelSubstitute(JsonNode.class, String.class)
                .globalOperationParameters(getGlobalParameters());
    }

    private List<Parameter> getGlobalParameters() {
        Parameter authorizationHeader = new ParameterBuilder()
                .name("Authorization").modelRef(new ModelRef("string"))
                .parameterType("header").defaultValue("Bearer TOKEN").required(true)
                .build();

        return Arrays.asList(authorizationHeader);
    }

    private ApiInfo apiInfoDetails() {
        return new ApiInfo(
                "Management System Application",
                "Management System Application for small medium businesses",
                "1.0.0-beta",
                "TERMS OF SERVICES",
                new Contact("Ibrahim Mohammed Al-Hote", "linkedIn page", "ibrahimalhoto@gmail.com"),
                "API USE LICENCE",
                "LICENCE URL",
                Collections.emptyList()
//    Collection< VendorExtension > vendorExtensions) {
//    this.title = title;
//    this.description = description;
//    this.version = version;
//    this.termsOfServiceUrl = termsOfServiceUrl;
//    this.contact = contact;
//    this.license = license;
//    this.licenseUrl = licenseUrl;
//    this.vendorExtensions = newArrayList(vendorExtensions
        );
    }

    @Data
    class SwaggerPageable {
        @ApiModelProperty(value = "Number of records per page", example = "20", dataType = "int")
        private int size;

        @ApiModelProperty(value = "Results page you want to retrieve (0..N)", example = "0", dataType = "int")
        private int page;

        @ApiModelProperty(value = "Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.", example = "id,asc", dataType = "String")
        private String sort;
    }
}
