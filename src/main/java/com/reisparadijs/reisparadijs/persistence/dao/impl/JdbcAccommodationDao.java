package com.reisparadijs.reisparadijs.persistence.dao.impl;//package com.reisparadijs.reisparadijs.persistence.dao;

import com.reisparadijs.reisparadijs.business.domain.Accommodation;
import com.reisparadijs.reisparadijs.business.domain.AccommodationType;
import com.reisparadijs.reisparadijs.business.domain.AppUser;
import com.reisparadijs.reisparadijs.persistence.dao.AccommodationDao;
import com.reisparadijs.reisparadijs.persistence.dao.JdbcAreaDAO;
import com.reisparadijs.reisparadijs.persistence.repository.AccommodationTypeRepository;
import com.reisparadijs.reisparadijs.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/*********************************************
 * @Author Mirre Cicilia
 * @Studentennummer 500241293
 * @Project reisparadijs
 * @Created 07 August Wednesday 2024 - 21:31
 * @Korte beschrijving: Implementatie van de Data Access Object (DAO) voor accommodaties met gebruik van JDBC.
 *  Deze klasse biedt concrete implementaties voor de methoden gedefinieerd in de
 *  AccommodationDao interface, met gebruik van JDBC voor database-interacties.
 **********************************************/
@Repository
public class JdbcAccommodationDao implements AccommodationDao {

    private static final Logger logger = LoggerFactory.getLogger(JdbcAccommodationDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final AccommodationTypeRepository accommodationTypeRepository;

    public JdbcAccommodationDao(JdbcTemplate jdbcTemplate,
                                UserRepository userRepository, AccommodationTypeRepository accommodationTypeRepository) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.accommodationTypeRepository = accommodationTypeRepository;
        logger.info("New JdbcMemberDao.");
    }

    @Override
    public Accommodation save(Accommodation accommodation) {
        String sql = "INSERT INTO accommodation (zip_code, house_number, title, description, " +
                "price_per_day, number_of_guests, number_of_bedrooms, number_of_bathrooms, " +
                "number_of_beds, is_active, accommodation_type_id,host_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                accommodation.getZipCode(),
                accommodation.getHouseNumber(),
                accommodation.getTitle(),
                accommodation.getDescription(),
                accommodation.getPricePerDay(),
                accommodation.getNumberOfGuests(),
                accommodation.getNumberOfBedrooms(),
                accommodation.getNumberOfBathrooms(),
                accommodation.getNumberOfBeds(),
                accommodation.getActive(),
                accommodation.getAccommodationType().getId(),
                accommodation.getHost().getId() // die moet inlogggende user id
        );
        System.out.println(accommodation.getHost().getId());

        // haal de gegenereerde id op en sla deze op in de accommodatie
        Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        if (id != null) {
            accommodation.setId(id);
        } else {
            throw new RuntimeException("Failed to retrieve the generated ID for the new accommodation.");
        }
        return accommodation;
    }

    @Override
    public void update(Accommodation accommodation) {
        String sql = "UPDATE accommodation SET zip_code = ?, house_number = ?, title = ?, description = ?, " +
                "price_per_day = ?, number_of_guests = ?, number_of_bedrooms = ?, number_of_bathrooms = ?, " +
                "number_of_beds = ?, published_at = ?, is_active = ?," +
                "host_id = ?, park_location_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, accommodation.getZipCode(), accommodation.getHouseNumber(), accommodation.getTitle(),
                accommodation.getDescription(), accommodation.getPricePerDay(), accommodation.getNumberOfGuests(),
                accommodation.getNumberOfBedrooms(), accommodation.getNumberOfBathrooms(),
                accommodation.getNumberOfBeds(), accommodation.getPublishedAt(), accommodation.getActive(),
                accommodation.getHost().getId(),
                accommodation.getParkLocationId(),
                accommodation.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM accommodation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Accommodation> findById(int id) {
        String sql = "SELECT * FROM accommodation WHERE id = ?";
        try {
            Accommodation accommodation = jdbcTemplate.queryForObject(sql, new Object[]{id}, new AccommodationRowMapper());
            return Optional.ofNullable(accommodation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Accommodation> findByHouseNumerAndZipCode(String housenumber, String zipcode){
        try {
            String sql = "SELECT * FROM accommodation WHERE house_number = ? AND zip_code = ?";
            Accommodation accommodationEx = jdbcTemplate.queryForObject(sql, new Object[] {housenumber, zipcode}, new AccommodationRowMapper());

            return Optional.ofNullable(accommodationEx);
        }
        catch (DataAccessException e){

            return Optional.empty();
        }
    }

    @Override
    public List<Accommodation> findAll() {
        String sql = "SELECT * FROM accommodation";
        return jdbcTemplate.query(sql, new AccommodationRowMapper());
    }

    //TODO: overleggen of we dit als team willen, zo ja implementeren
    @Override
    public List<Accommodation> findAvailableAccommodations(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    public List<Integer> findAllAvailableAccommodationIds(Date availableFrom, Date availableUntill) {
        String sql = "SELECT accommodation_id FROM Availability2 where availableFrom <= ? and availableUntill >= ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("accommodation_id"), availableFrom, availableUntill);
    }

    //TODO: overleggen of we dit als team willen, zo ja implementeren
    @Override
    public void updateAccommodationStatus(int id, boolean isAvailable) {

    }

    @Override
    public List<Accommodation> getFavoriteAccommodationsByUserId(int userId) {
        // fixme : return list of Accommodation {id, title, images , price .... } Accommodation module moet zijn
        String sql = """
                SELECT a.*  FROM accommodation as  a
                    join favorite_accommodation as f on a.id = f.accommodation_id
                where f.guest_id = ?;
                """;
        return jdbcTemplate.query(sql, new AccommodationRowMapper(), userId);
    }

    @Override
    public void addOrRemoveFavoriteAccommodation(Accommodation accommodation, AppUser user) {
        if (isFavoriteAccommodation(accommodation, user)) {
            deleteFavoriteAccommodation(accommodation, user);
        } else {
            insertFavoriteAccommodation(accommodation, user);
        }

    }

    @Override
    public List<Accommodation> findAccommodationsByNumberOfGuests(int numberOfGuests) {
        String sql = """
        Select * from accommodation where number_of_guests >= ? and is_active = true;
        """;
        return jdbcTemplate.query(sql, new AccommodationRowMapper(), numberOfGuests);
    }

    /**
     * Inserts favorite accommodation for a given user into the database.
     *
     * @param accommodation the accommodation to be added as a favorite
     * @param user          the user for whom the accommodation is to be added as a favorite
     */
    private void insertFavoriteAccommodation(Accommodation accommodation, AppUser user) {
        String sql = """
                INSERT INTO favorite_accommodation (guest_id, accommodation_id)
                VALUES (?, ?);
                """;
        jdbcTemplate.update(sql, user.getId(), accommodation.getId());
    }

    /**
     * Deletes       * favorite accommodation for a given user.
     *
     * @param accommodation the accommodation to be deleted from favorites
     * @param user          the user for whom the accommodation is to be deleted
     */
    private void deleteFavoriteAccommodation(Accommodation accommodation, AppUser user) {
        String sql = """
                DELETE FROM favorite_accommodation
                WHERE guest_id = ? AND accommodation_id = ?;
                """;
        jdbcTemplate.update(sql, user.getId(), accommodation.getId());
    }

    /**
     * Checks whether accommodation is marked as favorite by a user.
     *
     * @param accommodation the accommodation to check
     * @param user          the user to check for
     * @return true if the accommodation is a favorite of the user, false otherwise
     */
    private boolean isFavoriteAccommodation(Accommodation accommodation, AppUser user) {
        String sql = """
                SELECT count(*) FROM favorite_accommodation
                WHERE guest_id = ? AND accommodation_id = ?;
                """;
        int count;
        try {
            count = jdbcTemplate.queryForObject(sql, Integer.class, user.getId(), accommodation.getId());
            return count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

    }

    private static final class AccommodationRowMapper implements RowMapper<Accommodation> {

        @Override
        public Accommodation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Accommodation accommodation = new Accommodation();
            accommodation.setId(rs.getInt("id"));
            accommodation.setZipCode(rs.getString("zip_code"));
            accommodation.setHouseNumber(rs.getString("house_number"));
            accommodation.setTitle(rs.getString("title"));
            accommodation.setDescription(rs.getString("description"));
            accommodation.setPricePerDay(rs.getDouble("price_per_day"));
            accommodation.setNumberOfGuests(rs.getInt("number_of_guests"));
            accommodation.setNumberOfBedrooms(rs.getInt("number_of_bedrooms"));
            accommodation.setNumberOfBathrooms(rs.getInt("number_of_bathrooms"));
            accommodation.setNumberOfBeds(rs.getInt("number_of_beds"));

            // timestamp instellen - als er iets misgaat
            Timestamp publishedAtTimestamp = rs.getTimestamp("published_at");
            LocalDateTime publishedAt = (publishedAtTimestamp != null) ? publishedAtTimestamp.toLocalDateTime() : null;
            accommodation.setPublishedAt(publishedAt);

            accommodation.setActive(rs.getBoolean("is_active"));

            // haal het accommodatie type op
            int accommodationTypeId = rs.getInt("accommodation_type_id");
            if (!rs.wasNull()) {
                AccommodationType accommodationType = new AccommodationType();
                accommodationType.setId(accommodationTypeId);
                accommodation.setAccommodationType(accommodationType);
            }
            return accommodation;
        }
    }

}
