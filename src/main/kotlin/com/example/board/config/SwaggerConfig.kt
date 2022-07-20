package com.example.board.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.ResponseMessage
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    private var version:String? = null
    private var title:String? = null

    @Bean
    fun apiV1() : Docket? {
        version = "V1"
        title = "kotlinSpringBoard API $version"

        var responseMessage = ArrayList<ResponseMessage>()
        responseMessage.add(
            ResponseMessageBuilder()
            .code(200)
            .message("OK")
            .build())

        responseMessage.add(
            ResponseMessageBuilder()
            .code(404)
            .message("Not Found")
            .build())

        responseMessage.add(
            ResponseMessageBuilder()
            .code(500)
            .message("Internal Server Error")
            .build())

        return Docket(DocumentationType.SWAGGER_2)
            .groupName(version!!)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.board.controller"))
            .paths(PathSelectors.ant("/**"))
            .build()
            .apiInfo(apiInfo(title!!, version!!))
            .globalResponseMessage(RequestMethod.GET, responseMessage)
    }

    private fun apiInfo(title:String, version:String) : ApiInfo {
        return ApiInfo(
            title,
            "Swagger로 생성한 Api Docs",
            version,
            "www.example.swagger",
            Contact("Contact Me", "www.example.com", "foo@example.com"),
            "Licenses",
            "www.example.com",
            ArrayList()
        )
    }
}