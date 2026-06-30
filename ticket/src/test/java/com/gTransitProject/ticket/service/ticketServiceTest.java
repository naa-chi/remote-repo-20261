package com.gTransitProject.ticket.service;

import com.gTransitProject.ticket.model.ticketModel;
import com.gTransitProject.ticket.repo.ticketRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ticketServiceTest {

    @Mock
    private ticketRepo repo;

    @InjectMocks
    private ticketService service;

    @Test
    void getAll_shouldReturnListFromRepo() {
        ticketModel t1 = new ticketModel();
        ticketModel t2 = new ticketModel();
        when(repo.findAll()).thenReturn(List.of(t1, t2));

        List<ticketModel> result = service.getAll();

        assertThat(result).hasSize(2);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getById_whenExists_shouldReturnTicket() {
        ticketModel expected = new ticketModel();
        // setId removed
        when(repo.findById(1)).thenReturn(Optional.of(expected));

        ticketModel result = service.getById(1);

        assertThat(result).isSameAs(expected);
        verify(repo, times(1)).findById(1);
    }

    @Test
    void getById_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ticket not found with id: 99");
        verify(repo, times(1)).findById(99);
    }

    @Test
    void getByCode_whenExists_shouldReturnTicket() {
        ticketModel t1 = new ticketModel();
        t1.setCode("ABC123");
        ticketModel t2 = new ticketModel();
        t2.setCode("XYZ789");
        when(repo.findAll()).thenReturn(List.of(t1, t2));

        ticketModel result = service.getByCode("ABC123");

        assertThat(result).isSameAs(t1);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getByCode_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> service.getByCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ticket not found with code: UNKNOWN");
        verify(repo, times(1)).findAll();
    }

    @Test
    void getCityCodeOrigin_whenExists_shouldReturnTicket() {
        ticketModel t1 = new ticketModel();
        t1.setCityCodeOrigin("NYC");
        ticketModel t2 = new ticketModel();
        t2.setCityCodeOrigin("LAX");
        when(repo.findAll()).thenReturn(List.of(t1, t2));

        ticketModel result = service.getCityCodeOrigin("NYC");

        assertThat(result).isSameAs(t1);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getCityCodeOrigin_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> service.getCityCodeOrigin("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ticket not found with origin city code: UNKNOWN");
        verify(repo, times(1)).findAll();
    }

    @Test
    void getCityCodeDestination_whenExists_shouldReturnTicket() {
        ticketModel t1 = new ticketModel();
        t1.setCityCodeDestination("LON");
        ticketModel t2 = new ticketModel();
        t2.setCityCodeDestination("PAR");
        when(repo.findAll()).thenReturn(List.of(t1, t2));

        ticketModel result = service.getCityCodeDestination("LON");

        assertThat(result).isSameAs(t1);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getCityCodeDestination_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> service.getCityCodeDestination("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ticket not found with destination city code: UNKNOWN");
        verify(repo, times(1)).findAll();
    }

    @Test
    void getByClientId_whenExists_shouldReturnTicket() {
        ticketModel t1 = new ticketModel();
        t1.setClientId(100);
        ticketModel t2 = new ticketModel();
        t2.setClientId(200);
        when(repo.findAll()).thenReturn(List.of(t1, t2));

        ticketModel result = service.getByClientId(100);

        assertThat(result).isSameAs(t1);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getByClientId_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> service.getByClientId(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ticket not found with client ID: 999");
        verify(repo, times(1)).findAll();
    }

    @Test
    void getByTrainCode_whenExists_shouldReturnTicket() {
        ticketModel t1 = new ticketModel();
        t1.setTrainCode("TRAIN1");
        ticketModel t2 = new ticketModel();
        t2.setTrainCode("TRAIN2");
        when(repo.findAll()).thenReturn(List.of(t1, t2));

        ticketModel result = service.getByTrainCode("TRAIN1");

        assertThat(result).isSameAs(t1);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getByTrainCode_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> service.getByTrainCode("UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ticket not found with train code: UNKNOWN");
        verify(repo, times(1)).findAll();
    }

    @Test
    void create_shouldSaveAndReturn() {
        ticketModel input = new ticketModel();
        input.setCode("TICKET1");
        ticketModel saved = new ticketModel();
        saved.setCode("TICKET1");
        // setId removed

        when(repo.save(input)).thenReturn(saved);

        ticketModel result = service.create(input);

        assertThat(result).isSameAs(saved);
        verify(repo, times(1)).save(input);
    }

    @Test
    void delete_shouldCallRepoDeleteById() {
        service.delete(5);
        verify(repo, times(1)).deleteById(5);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_whenExists_shouldUpdateFieldsAndSave() {
        Integer id = 1;
        ticketModel existing = new ticketModel();
        existing.setCode("OLD");
        existing.setCityCodeOrigin("OLD_ORIGIN");
        existing.setCityCodeDestination("OLD_DEST");
        existing.setTrainCode("OLD_TRAIN");
        existing.setClientId(111);
        // Use java.sql.Date
        Date oldDep = Date.valueOf("2020-01-01");
        Date oldArr = Date.valueOf("2020-01-02");
        existing.setDepartureTime(oldDep);
        existing.setArrivalTime(oldArr);

        ticketModel updatedData = new ticketModel();
        updatedData.setCode("NEW_CODE");
        updatedData.setCityCodeOrigin("NEW_ORIGIN");
        updatedData.setCityCodeDestination("NEW_DEST");
        updatedData.setTrainCode("NEW_TRAIN");
        updatedData.setClientId(222);
        Date dep = Date.valueOf("2025-01-01");
        Date arr = Date.valueOf("2025-01-02");
        updatedData.setDepartureTime(dep);
        updatedData.setArrivalTime(arr);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        ticketModel result = service.update(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getCode()).isEqualTo("NEW_CODE");
        assertThat(existing.getCityCodeOrigin()).isEqualTo("NEW_ORIGIN");
        assertThat(existing.getCityCodeDestination()).isEqualTo("NEW_DEST");
        assertThat(existing.getTrainCode()).isEqualTo("NEW_TRAIN");
        assertThat(existing.getClientId()).isEqualTo(222);
        assertThat(existing.getDepartureTime()).isEqualTo(dep);
        assertThat(existing.getArrivalTime()).isEqualTo(arr);

        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(existing);
    }

    @Test
    void update_whenNotExists_shouldThrowRuntimeException() {
        Integer id = 99;
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, new ticketModel()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ticket not found with id: " + id);
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }
}