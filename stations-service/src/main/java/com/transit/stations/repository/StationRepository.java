package com.transit.stations.repository;

import com.transit.stations.model.StationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<StationModel, Long> {
    Optional<StationModel> findByStationCode(String stationCode);
    List<StationModel> findByCityCode(String cityCode);

    @Query("SELECT s FROM StationModel s WHERE s.lineCode1 = :lineCode OR s.lineCode2 = :lineCode OR s.lineCode3 = :lineCode OR s.lineCode4 = :lineCode")
    List<StationModel> findByAnyLineCode(@Param("lineCode") Integer lineCode);
}