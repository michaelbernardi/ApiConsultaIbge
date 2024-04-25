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
    @DisplayName("Teste para distrito inválido")
    public void testConsultarDistritoInvalido() {
        // Arrange
        int id = -1; // ID de distrito inválido
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            // Act
            ConsultaIBGE.consultarDistrito(id);
        });
        // Assert
        assertEquals("ID de distrito inválido: -1", exception.getMessage());
    }

    @Test
    @DisplayName("Teste para estado inválido")
    public void testConsultarEstadoInvalido() {
        // Arrange
        String uf = "XX"; // Estado inválido

        // Act & Assert
        assertThrows(IOException.class, () -> {
            // Act
            ConsultaIBGE.consultarEstado(uf);
        }, "Deveria lançar IOException para um estado inválido.");
    }


}