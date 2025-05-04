package com.L2.repository.implementations;

import com.L2.dto.global_spares.ProductToSparesDTO;
import com.L2.dto.global_spares.PropertiesDTO;
import com.L2.dto.global_spares.ReplacementCrDTO;
import com.L2.repository.interfaces.GlobalSparesRepository;
import com.L2.repository.rowmappers.ProductToSparesRowMapper;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

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
                    "end_of_service_date, last_update, added_to_catalogue, removed_from_catalogue, " +
                    "comments, keywords, archived, custom_add, last_updated_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                ps.setString(11, productToSpares.getRemovedFromCatalogue());
                ps.setString(12, productToSpares.getComments());
                ps.setString(13, productToSpares.getKeywords());
                ps.setBoolean(14, productToSpares.isArchived());
                ps.setBoolean(15, productToSpares.isCustomAdd());
                ps.setString(16, productToSpares.getLastUpdatedBy());
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

    public List<ProductToSparesDTO> searchSpares(String searchTerm, int partOrderId) {
        try {
            String query = """
                SELECT *
                FROM product_to_spares
                WHERE (spare_item LIKE ? OR replacement_item LIKE ?)
                GROUP BY spare_item;
                """;

            // Use % for partial matching
            String searchPattern = "%" + searchTerm + "%";
            logger.info("Searching for parts...");
            // Execute the query using JdbcTemplate
            return jdbcTemplate.query(query, new ProductToSparesRowMapper(), searchPattern, searchPattern); // Parameters for both LIKE conditions

        } catch (Exception e) {
            logger.error("Database error: {}", e.getMessage());
            return List.of(); // Return empty list on error
        }
    }
}

