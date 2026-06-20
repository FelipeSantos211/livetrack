package com.felipesantos.livetrack.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.felipesantos.livetrack.model.Tracking;
import com.felipesantos.livetrack.model.TrackingEvent;
import com.felipesantos.livetrack.repository.TrackingEventRepository;
import com.felipesantos.livetrack.repository.TrackingRepository;
import com.felipesantos.livetrack.websocket.TrackingWebSocketController;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingEventRepository eventRepository;
    private final TrackingRepository trackingRepository;
    private final TrackingWebSocketController wsController;


    public Tracking createTracking() {
        Tracking tracking = new Tracking();
        tracking.setStatus("ACTIVE");
        tracking.setCreatedAt(LocalDateTime.now());
        return trackingRepository.save(tracking);
    }

    public TrackingEvent addEvent(Long trackingId, TrackingEvent event) {

        Tracking tracking = trackingRepository.findById(trackingId)
                .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Tracking not found"
        ));

        event.setTracking(tracking);
        event.setEventTime(LocalDateTime.now());

        TrackingEvent saved = eventRepository.save(event);

        wsController.sendLocation(trackingId, saved);

        return saved;
    }

    public TrackingEvent getLastLocation(Long trackingId) {
        return eventRepository
                .findTopByTracking_IdOrderByEventTimeDesc(trackingId);
    }

    public List<TrackingEvent> getHistory(Long trackingId) {
        return eventRepository
                .findByTrackingIdOrderByEventTimeAsc(trackingId);
    }
}

