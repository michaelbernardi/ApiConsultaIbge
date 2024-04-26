package org.estudos.br;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultaIBGETest {
    private static final String ESTADOS_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";
    private static final String DISTRITOS_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/distritos/";

    @Test
    @DisplayName("Teste para consulta única de um estado")
    public void testConsultarEstado() throws IOException {
        // Arrange
        String uf = "SP"; // Define o estado a ser consultado

        // Act
        String resposta = ConsultaIBGE.consultarEstado(uf); // Chama o método a ser testado

        // Assert
        // Verifica se a resposta não está vazia
        assert !resposta.isEmpty();

        // Verifica se o status code é 200 (OK)
        HttpURLConnection connection = (HttpURLConnection) new URL(ESTADOS_API_URL + uf).openConnection();
        int statusCode = connection.getResponseCode();
        assertEquals(200, statusCode, "O status code da resposta da API deve ser 200 (OK)");
    }

    @Test
    @DisplayName("Teste para consulta única de um distrito")
    public void testConsultarDistrito() throws IOException {
        // Arrange
        Integer id = 520005005; // Define o distrito a ser consultado

        // Act
        String resposta = ConsultaIBGE.consultarDistrito(id); // Chama o método a ser testado

        // Assert
        // Verifica se a resposta não está vazia
        assert !resposta.isEmpty();

        // Verifica se o status code é 200 (OK)
        HttpURLConnection connection = (HttpURLConnection) new URL(DISTRITOS_API_URL + id).openConnection();
        int statusCode = connection.getResponseCode();
        assertEquals(200, statusCode, "O status code da resposta da API deve ser 200 (OK)");
    }
    @Test
    @DisplayName("Teste para consultar um estado com sigla inválida (menos de 2 caracteres)")
    public void testConsultarEstadoComSiglaMenorQueDois() {
        // Arrange
        String uf = "S"; // Estado com sigla inválida

        // Act & Assert
        assertThrows(IOException.class, () -> ConsultaIBGE.consultarEstado(uf),
                "Deve lançar IOException para uma sigla de estado com menos de 2 caracteres");
    }

    @Test
    @DisplayName("Teste para consultar um distrito com ID negativo")
    public void testConsultarDistritoComIdNegativo() {
        // Arrange
        int id = -100; // ID negativo

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ConsultaIBGE.consultarDistrito(id),
                "Deve lançar IllegalArgumentException para um ID de distrito negativo");
    }

    @Test
    @DisplayName("Teste para distrito inválido")
    public void testConsultarDistritoInvalido() {
        // Arrange
        int id = 0; // ID de distrito inválido

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            // Act
            ConsultaIBGE.consultarDistrito(id);
        });

        // Assert
        assertEquals("ID de distrito inválido: 0", exception.getMessage());
    }
    @Test
    @DisplayName("Teste para estado inválido")
    public void testConsultarEstadoInvalido() throws IOException {
        // Arrange
        String uf = "XX"; // Estado inválido

        // Act
        String resposta = ConsultaIBGE.consultarEstado(uf);

        // Assert
        assertTrue(resposta.isEmpty(), "A resposta deve estar vazia para um estado inválido");
    }
}