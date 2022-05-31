package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanWithoutIdDto;

public interface SeatingPlanService {

    /**
     * Creates a seating plans with all connected sectors and seats.
     *
     * @param seatingPlan The seating plan that will be created
     * @return ID of the created seating plan
     */
    public Long createSeatingPlan(SeatingPlanWithoutIdDto seatingPlan);
}
