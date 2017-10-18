package com.ohrats.bbb.ohrats;

import java.io.Serializable;

/**
 * Created by Josh on 10/16/2017.
 */

public enum LocationTypes implements Serializable {
    OneTwoFamilyDwelling("1-2 Family Dwelling"), ThreePlusFamilyAptBuilding("3+ family Apt. Building"),
    CatchBasinSewer("Catch Basin/Sewer"), CommericialBuilding("Commericial Building"),
    ConstructionSite("Construction Site"), ThreePlusMixedUseBuilding("3+ Mixed Use Building"),
    DayCareNursery("DayCare/Nursery"), GovBuilding("Government Building"), Hospital("Hospital"),
    OfficeBuilding("Office Building"), Other("Other(Explain Below)"), ParkingLotGarage("Parking Lot/ Garage"),
    PublicGarden("Public Garden"), PublicStairs("Public Stairs"), School("School/Pre-school"),
    SingleRoomOccupancy("Single Room Occupancy (SRO)"), OneTwoFamilyMixedUse("1-2 Family Mixed Use"),
    SummerCamp("Summer Camp"), VacantLot("Vacant Lot"), VacantBuilding("Vancant Building");


    private String representation;

    LocationTypes(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }
}
