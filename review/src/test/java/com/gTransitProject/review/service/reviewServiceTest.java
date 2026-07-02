package com.gTransitProject.review.service;

import com.gTransitProject.review.model.reviewModel;
import com.gTransitProject.review.repo.reviewRepo;
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
class reviewServiceTest {

    @Mock
    private reviewRepo repo;

    @InjectMocks
    private reviewService service;

    // ---------- getAll ----------
    @Test
    void getAll_shouldReturnListFromRepo() {
        reviewModel r1 = new reviewModel();
        reviewModel r2 = new reviewModel();
        when(repo.findAll()).thenReturn(List.of(r1, r2));

        List<reviewModel> result = service.getAll();

        assertThat(result).hasSize(2);
        verify(repo, times(1)).findAll();
    }

    // ---------- getById ----------
    @Test
    void getById_whenExists_shouldReturnReview() {
        reviewModel expected = new reviewModel();
        expected.setId(1);
        when(repo.findById(1)).thenReturn(Optional.of(expected));

        reviewModel result = service.getById(1);

        assertThat(result).isSameAs(expected);
        verify(repo, times(1)).findById(1);
    }

    @Test
    void getById_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Review not found with id: 99");
        verify(repo, times(1)).findById(99);
    }

    // ---------- getByClientId (custom repo method) ----------
    @Test
    void getByClientId_whenExists_shouldReturnReview() {
        reviewModel expected = new reviewModel();
        expected.setClientId("100");  // String, not int
        when(repo.findByClientId("100")).thenReturn(Optional.of(expected)); // service passes Integer

        reviewModel result = service.getByClientId(100);

        assertThat(result).isSameAs(expected);
        verify(repo, times(1)).findByClientId("100");
    }

    @Test
    void getByClientId_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findByClientId("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByClientId(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Review not found with client id: 999");
        verify(repo, times(1)).findByClientId("999");
    }

    // ---------- getByRating (custom repo method, returns List) ----------
    @Test
    void getByRating_shouldReturnListFromRepo() {
        reviewModel r1 = new reviewModel();
        reviewModel r2 = new reviewModel();
        when(repo.findByRating(5)).thenReturn(List.of(r1, r2));

        List<reviewModel> result = service.getByRating(5);

        assertThat(result).hasSize(2);
        verify(repo, times(1)).findByRating(5);
    }

    // ---------- getByRoute (uses findAll stream) ----------
    @Test
    void getByRoute_shouldReturnFilteredList() {
        reviewModel r1 = new reviewModel();
        r1.setRoute("RouteA");
        reviewModel r2 = new reviewModel();
        r2.setRoute("RouteB");
        reviewModel r3 = new reviewModel();
        r3.setRoute("RouteA");
        when(repo.findAll()).thenReturn(List.of(r1, r2, r3));

        List<reviewModel> result = service.getByRoute("RouteA");

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(r1, r3);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getByRoute_whenNoneMatch_shouldReturnEmptyList() {
        when(repo.findAll()).thenReturn(List.of());

        List<reviewModel> result = service.getByRoute("Unknown");

        assertThat(result).isEmpty();
        verify(repo, times(1)).findAll();
    }

    // ---------- getByDate (uses findAll stream, date is java.sql.Date) ----------
    @Test
    void getByDate_shouldReturnFilteredList() {
        Date date1 = Date.valueOf("2024-01-01");
        Date date2 = Date.valueOf("2024-01-02");

        reviewModel r1 = new reviewModel();
        r1.setReviewDate(date1);
        reviewModel r2 = new reviewModel();
        r2.setReviewDate(date2);
        reviewModel r3 = new reviewModel();
        r3.setReviewDate(date1);
        when(repo.findAll()).thenReturn(List.of(r1, r2, r3));

        // service expects a String parameter "2024-01-01"
        List<reviewModel> result = service.getByDate("2024-01-01");

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(r1, r3);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getByDate_whenNoneMatch_shouldReturnEmptyList() {
        when(repo.findAll()).thenReturn(List.of());

        List<reviewModel> result = service.getByDate("2024-01-01");

        assertThat(result).isEmpty();
        verify(repo, times(1)).findAll();
    }

    // ---------- getBySpecificTrainId (uses findAll stream, specificTrainId is String) ----------
    @Test
    void getBySpecificTrainId_whenExists_shouldReturnReview() {
        reviewModel r1 = new reviewModel();
        r1.setSpecificTrainId("10");  // String
        reviewModel r2 = new reviewModel();
        r2.setSpecificTrainId("20");
        when(repo.findAll()).thenReturn(List.of(r1, r2));

        reviewModel result = service.getBySpecificTrainId(10); // service passes Integer

        assertThat(result).isSameAs(r1);
        verify(repo, times(1)).findAll();
    }

    @Test
    void getBySpecificTrainId_whenNotExists_shouldThrowRuntimeException() {
        when(repo.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> service.getBySpecificTrainId(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Review not found with specific train id: 999");
        verify(repo, times(1)).findAll();
    }

    // ---------- createReview ----------
    @Test
    void createReview_shouldSaveAndReturn() {
        reviewModel input = new reviewModel();
        input.setClientId("1"); // String
        reviewModel saved = new reviewModel();
        saved.setId(1);
        saved.setClientId("1");

        when(repo.save(input)).thenReturn(saved);

        reviewModel result = service.createReview(input);

        assertThat(result).isSameAs(saved);
        verify(repo, times(1)).save(input);
    }

    // ---------- deleteReview ----------
    @Test
    void deleteReview_shouldCallRepoDeleteById() {
        service.deleteReview(5);
        verify(repo, times(1)).deleteById(5);
        verifyNoMoreInteractions(repo);
    }

    // ---------- updateReview ----------
    @Test
    void updateReview_whenExists_shouldUpdateFieldsAndSave() {
        String id = "1";
        reviewModel existing = new reviewModel();
        existing.setClientId("100");
        existing.setRating(4);

        reviewModel updatedData = new reviewModel();
        updatedData.setClientId("200");
        updatedData.setRating(5);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        reviewModel result = service.updateReview(id, updatedData);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getClientId()).isEqualTo("200");
        assertThat(existing.getRating()).isEqualTo(5);

        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(existing);
    }

    @Test
    void updateReview_whenNotExists_shouldThrowRuntimeException() {
        String id = "99";
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateReview(id, new reviewModel()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Review not found with id: " + id);
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }
}