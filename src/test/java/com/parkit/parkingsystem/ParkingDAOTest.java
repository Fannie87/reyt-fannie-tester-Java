package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDAOTest {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;


	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

	}
	
	@Test
	void getNextAvailableSlotDAO() {
		ParkingType parkingType = ParkingType.CAR;
		int getNextAvailableSlotReturn = parkingSpotDAO.getNextAvailableSlot(parkingType);
		assertTrue(getNextAvailableSlotReturn>=1);
	}
	
	@Test
	void updateParkingDAO() {
		ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR, true);
		boolean updateParkingReturn = parkingSpotDAO.updateParking(parkingSpot);
		assertTrue(updateParkingReturn);
	}
	
}