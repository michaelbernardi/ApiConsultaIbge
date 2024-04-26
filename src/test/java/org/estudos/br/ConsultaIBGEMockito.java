package org.estudos.br;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ConsultaIBGEMockito {

    @Mock
    private HttpURLConnection connectionMock;

    private static final String JSON_RESPONSE = "{\"id\":520005005,\"nome\":\"Abadia de Goiás\",\"municipio\":{\"id\":5200050,\"nome\":\"Abadia de Goiás\",\"microrregiao\":{\"id\":52010,\"nome\":\"Goiânia\",\"mesorregiao\":{\"id\":5203,\"nome\":\"Centro Goiano\",\"UF\":{\"id\":52,\"sigla\":\"GO\",\"nome\":\"Goiás\",\"regiao\":{\"id\":5,\"sigla\":\"CO\",\"nome\":\"Centro-Oeste\"}}}},\"regiao-imediata\":{\"id\":520001,\"nome\":\"Goiânia\",\"regiao-intermediaria\":{\"id\":5201,\"nome\":\"Goiânia\",\"UF\":{\"id\":52,\"sigla\":\"GO\",\"nome\":\"Goiás\",\"regiao\":{\"id\":5,\"sigla\":\"CO\",\"nome\":\"Centro-Oeste\"}}}}}}";

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);

        InputStream inputStream = new ByteArrayInputStream(JSON_RESPONSE.getBytes(StandardCharsets.UTF_8));
        when(connectionMock.getInputStream()).thenReturn(inputStream);
    }

    @Test
    @DisplayName("Verifica se as informações de distrito estão corretas")
    public void testeInfosDistCorreta() throws IOException {
        try (MockedStatic<ConsultaIBGE> consultaIBGE = Mockito.mockStatic(ConsultaIBGE.class)) {
            consultaIBGE.when(() -> ConsultaIBGE.consultarEstado(anyString()))
                    .thenReturn(JSON_RESPONSE);

            int idDistrito = 520005005;

            String response = ConsultaIBGE.consultarDistrito(idDistrito);

            assertNotNull(response, "A resposta não deve ser nula");

            System.out.println(response);

            assertTrue(response.contains("Abadia de Goiás"), "Deve retornar Abadia de Goiás");
            assertTrue(response.contains("Goiânia"), "Deve retornar Goiânia");
            assertTrue(response.contains("Goiás"), "Deve retornar Goiás");
            assertTrue(response.contains("Centro-Oeste"), "Deve retornar Centro-Oeste");

            // Verifica itens obrigatórios na resposta
            assertTrue(response.contains("\"id\""), "A resposta deve conter o ID");
            assertTrue(response.contains("\"nome\""), "A resposta deve conter o nome");
            assertTrue(response.contains("\"municipio\""), "A resposta deve conter o município");
            assertTrue(response.contains("\"microrregiao\""), "A resposta deve conter a microrregião");
            assertTrue(response.contains("\"mesorregiao\""), "A resposta deve conter a mesorregião");
            assertTrue(response.contains("\"UF\""), "A resposta deve conter a UF");
            assertTrue(response.contains("\"regiao\""), "A resposta deve conter a região");
    }
}
}