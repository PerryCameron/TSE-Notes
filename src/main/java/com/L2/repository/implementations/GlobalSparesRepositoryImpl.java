package com.L2.repository.implementations;

import com.L2.dto.PartDTO;
import com.L2.dto.global_spares.ProductToSparesDTO;
import com.L2.dto.global_spares.PropertiesDTO;
import com.L2.dto.global_spares.ReplacementCrDTO;
import com.L2.repository.interfaces.GlobalSparesRepository;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalSparesRepositoryImpl implements GlobalSparesRepository {
    private static final Logger logger = LoggerFactory.getLogger(GlobalSparesRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public GlobalSparesRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getGlobalSparesDataSource("Global Spares Repo"));
    }

    @Override
    public int insertProductToSpare(ProductToSparesDTO productToSpares) {
        try {
            String sql = "INSERT INTO product_to_spares (" +
                    "pim_range, pim_product_family, spare_item, replacement_item, " +
                    "standard_exchange_item, spare_description, catalogue_version, " +
                    "end_of_service_date, last_update, added_to_catalogue, archived, custom_add) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, productToSpares.getPimRange());
                ps.setString(2, productToSpares.getPimProductFamily());
                ps.setString(3, productToSpares.getSpareItem());
                ps.setString(4, productToSpares.getReplacementItem());
                ps.setString(5, productToSpares.getStandardExchangeItem());
                ps.setString(6, productToSpares.getSpareDescription());
                ps.setString(7, productToSpares.getCatalogueVersion());
                ps.setString(8, productToSpares.getProductEndOfServiceDate());
                ps.setString(9, productToSpares.getLastUpdate());
                ps.setString(10, productToSpares.getAddedToCatalogue());
                ps.setBoolean(11, productToSpares.isArchived());
                ps.setBoolean(12, productToSpares.isCustom_add());
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue(); // Returns the generated ID
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(productToSpares.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int insertReplacementCr(ReplacementCrDTO replacementCrDTO) {
        try {
            String sql = "INSERT INTO replacement_cr (" +
                    "item, replacement, comment, old_qty, new_qty)" +
                    "VALUES (?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, replacementCrDTO.getItem());
                ps.setString(2, replacementCrDTO.getReplacement());
                ps.setString(3, replacementCrDTO.getComment());
                ps.setDouble(4, replacementCrDTO.getOld_qty()); // error: incompatible types: int cannot be converted to String
                ps.setDouble(5, replacementCrDTO.getNew_qty());
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue(); // Returns the generated ID
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int insertWorkbookProperties(PropertiesDTO propertiesDTO) {
        try {
            String sql = "INSERT INTO properties (" +
                    "created_by, last_modified_by, creation_date, last_modification_date)" +
                    "VALUES (?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, propertiesDTO.getCreatedBy());
                ps.setString(2, propertiesDTO.getLastModifiedBy());
                ps.setString(3, propertiesDTO.getCreationDate());
                ps.setString(4, propertiesDTO.getLastModifiedDate()); // error: incompatible types: int cannot be converted to String
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue(); // Returns the generated ID
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<PartDTO> searchSpares(String searchTerm, int partOrderId) {
        /*
         * Search for a single-word term in spare_item and replacement_item columns
         * and return a list of unique PartDTO objects based on partNumber.
         *
         * @param searchTerm   Single word to search for
         * @param partOrderId  ID to set in PartDTO
         * @return List of unique PartDTO objects for matching records
         */
        try {
            String query = """
                        SELECT id, spare_item, replacement_item, spare_description 
                        FROM product_to_spares 
                        WHERE (spare_item LIKE ? OR replacement_item LIKE ?)
                        AND archived = 0
                    """;

            // Use % for partial matching
            String searchPattern = "%" + searchTerm + "%";

            // Map to store unique PartDTOs by partNumber
            Map<String, PartDTO> uniqueParts = new HashMap<>();

            // RowMapper for PartDTO
            RowMapper<PartDTO> rowMapper = (rs, rowNum) -> {
                PartDTO part = new PartDTO(partOrderId);
                // Set partNumber: prefer replacement_item if not null/empty, else spare_item
                String replacementItem = rs.getString("replacement_item");
                String partNumber = (replacementItem != null && !replacementItem.isEmpty())
                        ? replacementItem
                        : rs.getString("spare_item");
                part.setPartNumber(partNumber);
                // Set partDescription
                String description = rs.getString("spare_description");
                part.setPartDescription(description != null ? description : "");

                // Only add to map if partNumber is not already present
                uniqueParts.putIfAbsent(partNumber, part);
                return part;
            };

            // Use non-deprecated query method
            jdbcTemplate.query(query, rowMapper, searchPattern, searchPattern);

            // Return list of unique PartDTOs
            return new ArrayList<>(uniqueParts.values());

        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return List.of(); // Return empty list on error
        }
    }
}

