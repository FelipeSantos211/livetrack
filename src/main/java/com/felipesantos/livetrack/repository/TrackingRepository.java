package com.felipesantos.livetrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipesantos.livetrack.model.Tracking;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {

}
