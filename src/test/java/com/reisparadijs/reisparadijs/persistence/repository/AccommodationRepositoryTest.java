package com.reisparadijs.reisparadijs.persistence.repository;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AccommodationType;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.persistence.dao.AccommodationDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project Accommodation.java
 * @Created 19 August Monday 2024 - 14:39
 * @Korte beschrijving: Deze AccommodationRepositoryTest-klasse test de save, findById, update, delete en findAll methoden.
 * De klasse gebruikt mockito om de interactie met de dao te 'simuleren'.
 **********************************************/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccommodationRepositoryTest {

    // test subject
    private AccommodationRepository accommodationRepository;

    // mock dependencies
    private final AccommodationDao accommodationDaoMock = mock(AccommodationDao.class);

    // test data
    // app user
    private static final AppUser.Gender gender_1 = AppUser.Gender.FEMALE;
    private static final AppUser host_1 = new AppUser(1, "Blud", "wachtwoord", "voornaam", null, "achternaam", "email@email.nl", gender_1, null, null,
            true);
    private static final AppUser host_2 = new AppUser(2);
    private static final AppUser host_3 = new AppUser(3);

    // accommodation types
    private static final AccommodationType accommodationType_House = new AccommodationType("House");
    private static final AccommodationType accommodationType_GuestHouse = new AccommodationType("GuestHouse");
    private static final AccommodationType accommodationType_Hotel = new AccommodationType("Hotel");

    // accommodation
    private static final Accommodation accommodation_1 = new Accommodation(1, "1111aa", "12a", "Huis", "Mooi huis in de buurt van de zee.",
            90.00, 2, 1, 2, 1, null, true, host_1, accommodationType_House, null, null);

    private static final Accommodation accommodation_2 = new Accommodation(1, "1111aa", "12a", "Gasthuis", "Mooi huis in het centrum.",
            190.00, 4, 2, 2, 4, null, true, host_2, accommodationType_GuestHouse, null, null);

    private static final Accommodation accommodation_3 = new Accommodation(1, "1111aa", "12a", "Gasthuis", "Mooi huis in het centrum.",
            190.00, 3, 2, 1, 2, null, true, host_3, accommodationType_Hotel, null, null);

    @BeforeAll
    public void setUp() {
        accommodationRepository = new AccommodationRepository(accommodationDaoMock);
        accommodation_1.setId(1);
    }

    @Test
    public void saveAccommodationMethodTest() {
        // arrange
        Accommodation accommodation = accommodation_1;
        // act
        accommodationRepository.save(accommodation);
        // assert
        assertNotNull(accommodation);
        verify(accommodationDaoMock, times(1)).save(accommodation);
        assertThat(accommodation.getZipCode()).isEqualTo("1111aa");
        assertThat(accommodation.getHouseNumber()).isEqualTo("12a");
        assertThat(accommodation.getDescription()).contains("Mooi huis");
        assertThat(accommodation.getPricePerDay()).isGreaterThan(50.00);
        assertThat(accommodation.getNumberOfBeds()).isEqualTo(1);
        assertThat(accommodation.getNumberOfBathrooms()).isEqualTo(2);
        assertThat(accommodation.getNumberOfBedrooms()).isEqualTo(1);
        assertThat(accommodation.getNumberOfGuests()).isEqualTo(2);
    }


    @Test
    public void findByIdMethodTest() {
        // arrange
        int id = 1;
        when(accommodationDaoMock.findById(id)).thenReturn(Optional.of(accommodation_1));

        // act
        Optional<Accommodation> foundAccommodationOptional = accommodationRepository.findById(id);

        // assert
        assertThat(foundAccommodationOptional).isPresent();
        Accommodation foundAccommodation = foundAccommodationOptional.get();
        assertThat(foundAccommodation.getId()).isEqualTo(id);
        assertThat(foundAccommodation.getTitle()).isEqualTo("Huis");
        verify(accommodationDaoMock, times(1)).findById(id);
    }

    @Test
    void update() {
        // arrange
        doNothing().when(accommodationDaoMock).update(any(Accommodation.class));

        //acti
        accommodationRepository.update(accommodation_1);

        //assert
        verify(accommodationDaoMock, times(1)).update(any(Accommodation.class));
    }

    @Test
    public void deleteTest() {
        // arrange
        int id = 1;
        doNothing().when(accommodationDaoMock).delete(anyInt());

        // act
        accommodationRepository.delete(1);

        // assert
        verify(accommodationDaoMock, times(1)).delete(id);
    }


    @Test
    void findAllMethodTest() {
        // Arrange
        List<Accommodation> expectedAccommodations = Arrays.asList(accommodation_1, accommodation_2, accommodation_3);
        when(accommodationDaoMock.findAll()).thenReturn(expectedAccommodations);

        // Act
        List<Accommodation> actualAccommodations = accommodationRepository.findAll();

        // Assert
        assertThat(actualAccommodations).isNotNull();
        assertThat(actualAccommodations).hasSize(expectedAccommodations.size());
        assertThat(actualAccommodations).containsExactlyElementsOf(expectedAccommodations);
        verify(accommodationDaoMock, times(1)).findAll();
    }


}
