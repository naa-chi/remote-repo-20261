package com.gTransitProject.client.service;

import com.gTransitProject.client.exception.resourceNotFoundException;
import com.gTransitProject.client.model.client;
import com.gTransitProject.client.repo.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AuthClientService authClientService;

    @InjectMocks
    private ClientService clientService;

    @Test
    void getAllClients_shouldReturnListFromRepo() {
        client c1 = new client();
        client c2 = new client();
        when(clientRepository.findAll()).thenReturn(List.of(c1, c2));

        List<client> result = clientService.getAllClients();

        assertThat(result).hasSize(2);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void saveClient_whenAuthValid_shouldSetStatusApprovedAndSave() {
        client input = new client();
        input.setAuthCode("VALID_CODE");
        client saved = new client();
        // No setId needed
        saved.setAuthCode("VALID_CODE");
        saved.setStatus("APROBADO");

        when(authClientService.validateAuth("VALID_CODE")).thenReturn(true);
        when(clientRepository.save(input)).thenReturn(saved);

        client result = clientService.saveClient(input);

        assertThat(result).isSameAs(saved);
        assertThat(result.getStatus()).isEqualTo("APROBADO");
        verify(authClientService, times(1)).validateAuth("VALID_CODE");
        verify(clientRepository, times(1)).save(input);
    }

    @Test
    void saveClient_whenAuthInvalid_shouldSetStatusRejectedAndSave() {
        client input = new client();
        input.setAuthCode("INVALID_CODE");
        client saved = new client();
        saved.setAuthCode("INVALID_CODE");
        saved.setStatus("RECHAZADO");

        when(authClientService.validateAuth("INVALID_CODE")).thenReturn(false);
        when(clientRepository.save(input)).thenReturn(saved);

        client result = clientService.saveClient(input);

        assertThat(result).isSameAs(saved);
        assertThat(result.getStatus()).isEqualTo("RECHAZADO");
        verify(authClientService, times(1)).validateAuth("INVALID_CODE");
        verify(clientRepository, times(1)).save(input);
    }

    @Test
    void getClientById_whenExists_shouldReturnClient() {
        client expected = new client();
        // No ID set
        when(clientRepository.findById(1)).thenReturn(Optional.of(expected));

        client result = clientService.getClientById(1);

        assertThat(result).isSameAs(expected);
        verify(clientRepository, times(1)).findById(1);
    }

    @Test
    void getClientById_whenNotExists_shouldThrowResourceNotFoundException() {
        when(clientRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getClientById(99))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Cliente no encontrado");
        verify(clientRepository, times(1)).findById(99);
    }

    @Test
    void updateClient_whenExists_shouldUpdateFieldsAndSave() {
        Integer id = 1;

        client existing = new client();
        existing.setClientName("Old Name");
        existing.setRequestCity("Old Req");
        existing.setProviderCity("Old Prov");
        existing.setRequestedResource("Old Res");
        existing.setOfferedReward("Old Reward");
        existing.setAuthCode("OLD_CODE");
        existing.setStatus("OLD_STATUS");

        client updatedData = new client();
        updatedData.setClientName("New Name");
        updatedData.setRequestCity("New Req");
        updatedData.setProviderCity("New Prov");
        updatedData.setRequestedResource("New Res");
        updatedData.setOfferedReward("New Reward");
        updatedData.setAuthCode("NEW_CODE");
        updatedData.setStatus("NEW_STATUS");

        when(clientRepository.findById(id)).thenReturn(Optional.of(existing));
        when(clientRepository.save(existing)).thenReturn(existing);

        client result = clientService.updateClient(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getClientName()).isEqualTo("New Name");
        assertThat(existing.getRequestCity()).isEqualTo("New Req");
        assertThat(existing.getProviderCity()).isEqualTo("New Prov");
        assertThat(existing.getRequestedResource()).isEqualTo("New Res");
        assertThat(existing.getOfferedReward()).isEqualTo("New Reward");
        assertThat(existing.getAuthCode()).isEqualTo("NEW_CODE");
        assertThat(existing.getStatus()).isEqualTo("NEW_STATUS");

        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(existing);
    }

    @Test
    void updateClient_whenNotExists_shouldThrowResourceNotFoundException() {
        Integer id = 99;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.updateClient(id, new client()))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Cliente no encontrado");
        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, never()).save(any());
    }

    @Test
    void deleteClient_shouldCallRepoDeleteById() {
        clientService.deleteClient(5);
        verify(clientRepository, times(1)).deleteById(5);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    void findByAuthCode_whenExists_shouldReturnClient() {
        client expected = new client();
        expected.setAuthCode("ABC123");
        when(clientRepository.findByAuthCode("ABC123")).thenReturn(Optional.of(expected));

        client result = clientService.findByAuthCode("ABC123");

        assertThat(result).isSameAs(expected);
        verify(clientRepository, times(1)).findByAuthCode("ABC123");
    }

    @Test
    void findByAuthCode_whenNotExists_shouldThrowResourceNotFoundException() {
        when(clientRepository.findByAuthCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.findByAuthCode("UNKNOWN"))
                .isInstanceOf(resourceNotFoundException.class)
                .hasMessage("Codigo no encontrado");
        verify(clientRepository, times(1)).findByAuthCode("UNKNOWN");
    }
}