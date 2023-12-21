package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class TicketDAOTest {
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static TicketDAO ticketDAO;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
	}

	@Test
	void saveTicketInDAO() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		Ticket ticket = new Ticket();
		ticket.setId(1);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setInTime(new Date());
		ticket.setOutTime(new Date());
		ticket.setPrice(23);
		boolean saveTicketReturn = ticketDAO.saveTicket(ticket);

		assertFalse(saveTicketReturn);

	}
	
	@Test
	void getTicketDAO() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		Ticket ticket = new Ticket();
		ticket.setId(1);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setInTime(new Date());
		ticket.setOutTime(new Date());
		ticket.setPrice(23);
		boolean saveTicketReturn = ticketDAO.saveTicket(ticket);
		
		Ticket ticket1 = ticketDAO.getTicket("ABCDEF");
		assertNotNull(ticket1);
		
	}
	
	@Test
	void updateTicketDAO() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		Ticket ticket = new Ticket();
		ticket.setId(1);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setInTime(new Date());
		ticket.setOutTime(new Date());
		ticket.setPrice(23);
		boolean saveTicketReturn = ticketDAO.saveTicket(ticket);
		Ticket ticket2 = new Ticket ();
		
		ticket2.setId(1);
		ticket2.setParkingSpot(parkingSpot);
		ticket2.setVehicleRegNumber("ABCDEF");
		ticket2.setInTime(new Date());
		ticket2.setOutTime(new Date());
		ticket2.setPrice(40);
		
		boolean updateTicketReturn = ticketDAO.updateTicket(ticket2);
		assertTrue(updateTicketReturn);
	}
	
	@Test
	void getNbTicketDAO() {
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		Ticket ticket = new Ticket();
		ticket.setId(1);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setInTime(new Date());
		ticket.setOutTime(new Date());
		ticket.setPrice(23);
		boolean saveTicketReturn = ticketDAO.saveTicket(ticket);
		
		Ticket ticket3 = new Ticket();		
		ticket3.setVehicleRegNumber("ABCDEF");
		int nbTicket = ticketDAO.getNbTicket(ticket3);
		assertTrue(nbTicket>=1);
	}
}