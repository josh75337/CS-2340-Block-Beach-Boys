package com.ohrats.bbb.ohrats;

import java.io.Serializable;

/**
 * Created by Josh on 10/16/2017.
 */

//suppressed because enums are used when creating new sightings.
@SuppressWarnings("DefaultFileTemplate")
public enum LocationTypes implements Serializable {
    @SuppressWarnings("unused")OneTwoFamilyDwelling("1-2 Family Dwelling"),
    @SuppressWarnings("unused")ThreePlusFamilyAptBuilding("3+ family Apt. Building"),
    @SuppressWarnings("unused")CatchBasinSewer("Catch Basin/Sewer"),
    @SuppressWarnings("unused")CommercialBuilding("Commercial Building"),
    @SuppressWarnings("unused")ConstructionSite("Construction Site"),
    @SuppressWarnings("unused")ThreePlusMixedUseBuilding("3+ Mixed Use Building"),
    @SuppressWarnings("unused")DayCareNursery("DayCare/Nursery"),
    @SuppressWarnings("unused")GovBuilding("Government Building"),
    @SuppressWarnings("unused")Hospital("Hospital"),
    @SuppressWarnings("unused")OfficeBuilding("Office Building"),
    @SuppressWarnings("unused")Other("Other(Explain Below)"),
    @SuppressWarnings("unused")ParkingLotGarage("Parking Lot/ Garage"),
    @SuppressWarnings("unused")PublicGarden("Public Garden"),
    @SuppressWarnings("unused")PublicStairs("Public Stairs"),
    @SuppressWarnings("unused")School("School/Pre-school"),
    @SuppressWarnings("unused")SingleRoomOccupancy("Single Room Occupancy (SRO)"),
    @SuppressWarnings("unused")OneTwoFamilyMixedUse("1-2 Family Mixed Use"),
    @SuppressWarnings("unused")SummerCamp("Summer Camp"),
    @SuppressWarnings("unused")VacantLot("Vacant Lot"),
    @SuppressWarnings("unused")VacantBuilding("Vacant Building");


    private final String representation;

    LocationTypes(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }
}
