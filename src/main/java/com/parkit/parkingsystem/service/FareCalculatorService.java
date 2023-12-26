package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	// Réduction de 5% sur le prix
	public void calculateFareWithDiscount(Ticket ticket, boolean discount) {
		this.calculateFare(ticket);
		// Réduction de 5% parce que utilisateur régulier (type double)
		if(discount == true) {
			ticket.setPrice(ticket.getPrice()*0.95);
		}
	}
	
	
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        // Some tests are failing here. Need to check if this logic is correct
        double duration = (outHour - inHour) / (60 * 60 * 1000);// Divisé par 1 min * 1h* millisecondes
        
        if (duration < 0.5) {
        	duration = 0;
        }//Tps de stationnement inférieur à 30 minutes

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}