package com.zand.system.transactionrestservice.doc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Zand system's transaction rest service - OpenAPI 3.0 documentation",
                description = """
                        This service exposed endpoint to do the fund transfer and account balance .
                        """,
                license = @License(
                        name = "MIT Licence",
                        url = "")),
        servers = @Server(url = "http://localhost:8085")
)
public class OpenApiConfiguration {
}