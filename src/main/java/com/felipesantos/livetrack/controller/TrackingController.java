package com.felipesantos.livetrack.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felipesantos.livetrack.dto.TrackingDTO;
import com.felipesantos.livetrack.dto.TrackingEventDTO;
import com.felipesantos.livetrack.model.TrackingEvent;
import com.felipesantos.livetrack.service.TrackingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService service;

    @PostMapping
    public TrackingDTO createTracking() {
        return service.createTracking();
    }

    @PostMapping("/{id}/event")
    public TrackingEventDTO addEvent(
            @PathVariable Long id,
            @RequestBody TrackingEvent event
    ) {
        return service.addEvent(id, event);
    }

    @GetMapping("/{id}")
    public TrackingEventDTO getLast(@PathVariable Long id) {
        return service.getLastLocation(id);
    }

    @GetMapping("/{id}/history")
    public List<TrackingEventDTO> history(@PathVariable Long id) {
        return service.getHistory(id);
    }
}
