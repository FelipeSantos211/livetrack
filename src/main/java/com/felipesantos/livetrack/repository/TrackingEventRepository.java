package com.felipesantos.livetrack.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipesantos.livetrack.model.TrackingEvent;

public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {

    List<TrackingEvent> findByTrackingIdOrderByEventTimeAsc(Long trackingId);

    TrackingEvent findTopByTracking_IdOrderByEventTimeDesc(Long trackingId);
}
