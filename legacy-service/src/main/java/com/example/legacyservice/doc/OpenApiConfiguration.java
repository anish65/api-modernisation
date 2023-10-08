package com.example.legacyservice.doc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Zand core banking service - OpenAPI 3.0 documentation",
                description = """
                        This service exposed core banking endpoint with throttling.
                        """,
                license = @License(
                        name = "MIT Licence",
                        url = "")),
        servers = @Server(url = "http://localhost:8088")
)
public class OpenApiConfiguration {
}