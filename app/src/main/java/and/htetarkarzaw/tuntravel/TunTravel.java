package and.htetarkarzaw.tuntravel;

/**
 * Created by Eiron on 6/20/17.
 */

public class TunTravel {
    public static class Trip {
        public static final String TRIPS = "Trips";
        public static final String TRIP_COUNTS = "tripCounts";
        public static final String REG_TRIP_TYPE = "regularTrip";
        public static final String ORDER_TRIP_TYPE = "orderTrip";
        public static final String PASSENGERS_LIST = "passengersList";

        public static class Keys{
            public static final String TRIP_TYPE = "tripType";
            public static final String TRIP_NAME = "tripName";
            public static final String CAR_NO = "carNo";
            public static final String CAR_KEY = "carKey";
            public static final String CONDUCTOR_NAME_1 = "conductorName1";
            public static final String CONDUCTOR_KEY_1 = "conductor1Key";
            public static final String CONDUCTOR_NAME_2 = "conductorName2";
            public static final String CONDUCTOR_KEY_2 = "conductor2Key";
            public static final String DRIVER_NAME = "driverName";
            public static final String DRIVER_KEY = "driverKey";
            public static final String GUIDE_NAME = "guideName";
            public static final String GUIDE_KEY = "guideKey";
            public static final String START_DATE = "startDate";
            public static final String STR_START_DATE = "strStartDate";
            public static final String STR_END_DATE = "strEndDate";
            public static final String END_DATE = "endDate";
            public static final String RENDER_CHARGE = "renderCharge";
            public static final String ROAD_FEE = "roadFee";
            public static final String COST_FOR_FOOD = "costForFood";
            public static final String GENERAL_EXPENSE = "generalExpense";
            public static final String DEBIT = "debit";
            public static final String PROFIT = "profit";
            public static final String CLIENT_NAME = "clientName";
            public static final String CLIENT_PH_NUM = "clientPhNum";
            public static final String CLIENT_COMPANY = "clientCompany";
            public static final String PASSENGERS_COUNT = "passengersCount";
        }
    }

    public static class Passenger{
        public static final String PASSENGERS = "passengers";
        public static final String PASSENGER_COUNTS = "passengerCounts";
        public static class Keys{
            public static final String PASSENGER_NAME = "passengerName";
            public static final String PASSENGER_NRC = "passengerNRC";
            public static final String PASSENGER_PH_NO = "passengerPhno";
            public static final String SEAT_NO = "seatNo";
            public static final String REMARK = "remark";
        }
    }

    public static class Car {
        public static final String CARS = "Cars";
        public static final String CARS_COUNTS = "CarCounts";
        public static final String CARS_IMAGES = "carImages";

        public static class Image_Names {
            public static final String IMAGE_ONE = "imageOne";
            public static final String IMAGE_TWO = "imageTwo";
            public static final String IMAGE_THREE = "imageThree";
            public static final String IMAGE_FOUR = "imageFour";
        }

        public static class Keys {
            public static final String CAR_NO = "carNo";
            public static final String CAR_OWNER_NAME = "carOwnerName";
            public static final String CAR_OWNER_PH_NUM = "carOwnerPhNum";
            public static final String COUNT_SEATS = "countSeats";
            public static final String CAR_TYPE = "carType";
            public static final String CAR_OWNER_ADDRESS = "carOwnerAddress";
            public static final String CURRENT_LINE = "currentLine";
            public static final String CAR_REMARK = "carRemark";
            public static final String START_TRIP_DATE = "startTripDate";
            public static final String END_TRIP_DATE = "endTripDate";
            public static final String CURRENT_TRIP = "currentTrip";
            public static final String CAR_OWNER_NRC = "carOwnerNRC";
        }
    }

    public static class Conductor {
        public static final String CONDUCTORS = "Conductors";
        public static final String CONDUCTOR_COUNTS = "ConductorCounts";
        public static final String CONDUCTOR_IMAGES = "conductorImages";

        public static class Image_Names {
            public static final String IMAGE_ONE = "imageOne";
        }

        public static class Keys {
            public static final String CONDUCTOR_NAME = "conductorName";
            public static final String CONDUCTOR_NRC = "conductorNRC";
            public static final String CONDUCTOR_LICENCE_NO = "conductorLicenceNo";
            public static final String CONDUCTOR_ADDRESS = "conductorAddress";
            public static final String CONDUCTOR_PH_NO = "conductorPhNo";
            public static final String CONDUCTOR_REMARK = "conductorRemark";
            public static final String START_TRIP_DATE = "startTripDate";
            public static final String END_TRIP_DATE = "endTripDate";
            public static final String CURRENT_TRIP = "currentTrip";
        }
    }

    public static class Driver {
        public static final String DRIVERS = "Drivers";
        public static final String DRIVER_COUNTS = "DriverCounts";
        public static final String DRIVER_IMAGES = "driverImages";

        public static class Image_Names {
            public static final String IMAGE_ONE = "imageOne";
        }

        public static class Keys {
            public static final String DRIVER_NAME = "driverName";
            public static final String DRIVER_NRC = "driverNRC";
            public static final String DRIVER_LICENCE_NO = "driverLicenceNo";
            public static final String DRIVER_ADDRESS = "driverAddress";
            public static final String DRIVER_PH_NO = "driverPhNo";
            public static final String DRIVER_REMARK = "driverRemark";
            public static final String START_TRIP_DATE = "startTripDate";
            public static final String END_TRIP_DATE = "endTripDate";
            public static final String CURRENT_TRIP = "currentTrip";
        }
    }

    public static class Guides {
        public static final String GUIDES = "Guides";
        public static final String GUIDE_COUNTS = "GuideCounts";
        public static final String GUIDE_IMAGES = "guideImages";

        public static class Image_Names {
            public static final String IMAGE_ONE = "imageOne";
        }

        public static class Keys {
            public static final String GUIDE_NAME = "guideName";
            public static final String GUIDE_NRC = "guideNRC";
            public static final String GUIDE_LICENCE_NO = "guideLicenceNo";
            public static final String GUIDE_ADDRESS = "guideAddress";
            public static final String GUIDE_PH_NO = "guidePhNo";
            public static final String GUIDE_REMARK = "guideRemark";
            public static final String START_TRIP_DATE = "startTripDate";
            public static final String END_TRIP_DATE = "endTripDate";
            public static final String CURRENT_TRIP = "currentTrip";
        }
    }
}
