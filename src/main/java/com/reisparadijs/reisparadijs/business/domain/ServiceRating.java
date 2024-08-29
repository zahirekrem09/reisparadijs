package com.reisparadijs.reisparadijs.business.domain;

public class ServiceRating {

    private int id;
    private int rating;
    private ServiceReview serviceReview;
    private ReservationAccommodation reservationAccommodation;

    public enum ServiceReview {
        CLEANLINESS("Cleanliness"),
        ACCURACY("Accuracy"),
        CHECK_IN("Check-in"),
        COMMUNICATION("Communication"),
        LOCATION("Location"),
        VALUE("Value");

        private final String value;

        ServiceReview(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static ServiceReview fromValue(String value) {
            for (ServiceReview serviceReview : ServiceReview.values()) {
                if (serviceReview.getValue().equals(value)) {
                    return serviceReview;
                }
            }
            return null;
        }
    }

    public ServiceRating(int id, int rating, ServiceReview serviceReview, ReservationAccommodation reservationAccommodation) {
        this.id = id;
        this.rating = rating;
        this.serviceReview = serviceReview;
        this.reservationAccommodation = reservationAccommodation;
    }

    public ServiceRating() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ServiceReview getServiceReview() {
        return serviceReview;
    }

    public void setServiceReview(ServiceReview serviceReview) {
        this.serviceReview = serviceReview;
    }

    public ReservationAccommodation getReservationAccommodation() {
        return reservationAccommodation;
    }

    public void setReservationAccommodation(ReservationAccommodation reservationAccommodation) {
        this.reservationAccommodation = reservationAccommodation;
    }
}
