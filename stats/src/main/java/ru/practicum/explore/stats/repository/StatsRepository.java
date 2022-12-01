package ru.practicum.explore.stats.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.stats.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDateTime;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(value = "select app, uri, count(distinct ip) hits " +
            " from stats " +
            " where time_stamp between ?1 and ?2 and upper(uri) in (?3)" +
            " group by app, uri",
            nativeQuery = true)
    List<Object[]> findStatsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select app, uri, count(*) hits " +
            " from stats " +
            " where time_stamp between ?1 and ?2 and upper(uri) in (?3)" +
            " group by app, uri",
            nativeQuery = true)
    List<Object[]> findStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
