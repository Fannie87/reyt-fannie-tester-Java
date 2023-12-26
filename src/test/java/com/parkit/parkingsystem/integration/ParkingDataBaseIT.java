package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	public static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
	}

	@Test
    void testParkingACar(){
		when(inputReaderUtil.readSelection()).thenReturn(1);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        
        
        // check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket ticket=ticketDAO.getTicket("ABCDEF");
        assertEquals(false,ticket.getParkingSpot().isAvailable());
    }

	@Test
	void testParkingLotExit() throws ParseException {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		Ticket ticket = new Ticket();
		ticket.setId(1);
		ticket.setParkingSpot(parkingSpot);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date inTime = simpleDateFormat.parse("20231210");
		ticket.setInTime(inTime);
		ticket.setVehicleRegNumber("ABCDEF");
		ticketDAO.saveTicket(ticket);
		parkingService.processExitingVehicle();

		// check that the fare generated and out time are populated correctly in
		// the database
		Ticket ticket1 = ticketDAO.getTicket("ABCDEF");
		assertNotNull(ticket1.getOutTime());

	}

	@Test
	void testParkingLotExitRecurringUser() throws ParseException {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		Ticket ticket = new Ticket();
		ticket.setId(1);
		ticket.setParkingSpot(parkingSpot);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date inTime = simpleDateFormat.parse("20231210");
		ticket.setInTime(inTime);
		ticket.setVehicleRegNumber("ABCDEF");
		ticketDAO.saveTicket(ticket);

		parkingService.processExitingVehicle();

		Ticket ticket1 = ticketDAO.getTicket("ABCDEF");

		// calcul prix avec discount
		double expectedFare = (ticket1.getOutTime().getTime() - ticket1.getInTime().getTime()) * Fare.CAR_RATE_PER_HOUR
				* 0.95 / (60 * 60 * 1000);
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		String expectedFareFormat = decimalFormat.format(expectedFare);
		String actualFareFormat = decimalFormat.format(ticket1.getPrice());

		assertEquals(expectedFareFormat, actualFareFormat);

	}

}
