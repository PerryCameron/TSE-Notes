package com.L2.repository.implementations;

import com.L2.dto.global_spares.*;
import com.L2.repository.interfaces.GlobalSparesRepository;
import com.L2.repository.rowmappers.ProductToSparesRowMapper;
import com.L2.repository.rowmappers.RangesRowMapper;
import com.L2.repository.rowmappers.SparesRowMapper;
import com.L2.static_tools.DatabaseConnector;
import com.L2.static_tools.NoteTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
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
    public int insertConsolidatedProductToSpare(ProductToSparesDTO productToSpares) {
        try {
            String sql = "INSERT INTO spares (" +
                    "pim, spare_item, replacement_item, " +
                    "standard_exchange_item, spare_description, catalogue_version, " +
                    "end_of_service_date, last_update, added_to_catalogue, removed_from_catalogue, " +
                    "comments, keywords, archived, custom_add, last_updated_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, productToSpares.getPimRange());
                ps.setString(2, productToSpares.getSpareItem());
                ps.setString(3, productToSpares.getReplacementItem());
                ps.setString(4, productToSpares.getStandardExchangeItem());
                ps.setString(5, productToSpares.getSpareDescription());
                ps.setString(6, productToSpares.getCatalogueVersion());
                ps.setString(7, productToSpares.getProductEndOfServiceDate());
                ps.setString(8, productToSpares.getLastUpdate());
                ps.setString(9, productToSpares.getAddedToCatalogue());
                ps.setString(10, productToSpares.getRemovedFromCatalogue());
                ps.setString(11, productToSpares.getComments());
                ps.setString(12, productToSpares.getKeywords());
                ps.setBoolean(13, productToSpares.isArchived());
                ps.setBoolean(14, productToSpares.isCustomAdd());
                ps.setString(15, productToSpares.getLastUpdatedBy());
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
    public List<String> getDistinctSpareItems(boolean isArchived) {
        try {
            int archived = isArchived ? 1 : 0;
            String query = "SELECT DISTINCT spare_item FROM product_to_spares WHERE product_to_spares.archived = ? ORDER BY spare_item";
            List<String> spareItemsList = jdbcTemplate.query(query, (rs, rowNum) -> rs.getString("spare_item"), archived);
            return spareItemsList;
        } catch (Exception e) {
            logger.error("Error querying spare items: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public List<SparesDTO> searchSpares(String searchTerm, int partOrderId) {
        try {
            String query = """
                SELECT *
                FROM spares
                WHERE (spare_item LIKE ? OR replacement_item LIKE ?)
                GROUP BY spare_item;
                """;

            // Use % for partial matching
            String searchPattern = "%" + searchTerm + "%";
            logger.info("Searching for parts...");
            // Execute the query using JdbcTemplate
            return jdbcTemplate.query(query, new SparesRowMapper(), searchPattern, searchPattern); // Parameters for both LIKE conditions

        } catch (Exception e) {
            logger.error("Database error: {}", e.getMessage());
            return List.of(); // Return empty list on error
        }
    }

    @Override
    public List<String> getRangesFromSpareItem(String spare, boolean isArchived) {
        try {
            int archived = isArchived ? 1 : 0;
            String query = "SELECT DISTINCT pim_range FROM product_to_spares WHERE spare_item = ? AND product_to_spares.archived = ?";
            List<String> spareItemsList = jdbcTemplate.query(query, (rs, rowNum) -> rs.getString("pim_range"), spare, archived);
            return spareItemsList;
        } catch (Exception e) {
            logger.error("Error querying spare items: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public List<String> getProductsFromRange(String spare, String range, boolean isArchived) {
        try {
            int archived = isArchived ? 1 : 0;
            String query = "SELECT DISTINCT product_to_spares.pim_product_family from product_to_spares where spare_item = ? and pim_range = ? and product_to_spares.archived = ?";
            List<String> spareItemsList = jdbcTemplate.query(query, (rs, rowNum) -> rs.getString("pim_product_family"), spare, range, archived);
            return spareItemsList; // Return the list directly
        } catch (Exception e) {
            logger.error("Error querying spare items: {}", e.getMessage(), e);
            return List.of(); // Return empty list on error
        }
    }

    @Override
    public ProductToSparesDTO getProductToSpares(String spare, boolean isArchived) {
        try {
            String query = """
                    SELECT *
                    FROM product_to_spares
                    WHERE spare_item = ? and archived = ?
                    ORDER BY id
                    LIMIT 1;
                    """;
            return jdbcTemplate.queryForObject(query, new ProductToSparesRowMapper(), spare, isArchived);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<RangesDTO> findAllRanges() {
        try {
            String sql = "SELECT id, range, range_additional, range_type, last_update, last_updated_by FROM ranges";
            return jdbcTemplate.query(sql, new RangesRowMapper());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        return List.of();
    }

    @Override
    public List<SparesDTO> searchSparesScoring(String[] keywords) {
        StringBuilder scoreBuilder = new StringBuilder();
        StringBuilder whereBuilder = new StringBuilder();
        for (int k = 0; k < keywords.length; k++) {
            if (k > 0) {
                scoreBuilder.append(" + ");
                whereBuilder.append(" OR ");
            }
            scoreBuilder.append("""
            (CASE WHEN spare_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN replacement_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN standard_exchange_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN spare_description LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN comments LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN keywords LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END)
        """);
            // Remove trailing '+' from scoreBuilder if it's the last keyword
            if (k < keywords.length - 1) {
                scoreBuilder.append(" + ");
            }
            whereBuilder.append("""
            spare_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR replacement_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR standard_exchange_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR spare_description LIKE '%' || ? || '%' COLLATE NOCASE
            OR comments LIKE '%' || ? || '%' COLLATE NOCASE
            OR keywords LIKE '%' || ? || '%' COLLATE NOCASE
        """);
        }
        String sql = String.format("""
        SELECT *, (%s) AS match_score
        FROM spares
        WHERE %s
        ORDER BY match_score DESC
    """, scoreBuilder.toString().replaceAll("\\+\\s*$", ""), whereBuilder.toString());
        List<Object> params = new ArrayList<>();
        for (String keyword : keywords) {
            String normalizedKeyword = NoteTools.normalizeDate(keyword);
            String likeKeyword = "%" + normalizedKeyword + "%";
            // Add parameters for scoring (6 columns)
            for (int i = 0; i < 6; i++) {
                params.add(likeKeyword);
            }
            // Add parameters for WHERE clause (6 columns)
            for (int i = 0; i < 6; i++) {
                params.add(likeKeyword);
            }
        }
        return jdbcTemplate.query(sql, ps -> {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i)); // JDBC indices are 1-based
            }
        }, new SparesRowMapper());
    }

    @Override
    public List<SparesDTO> searchSparesScoringDual(String[] keywords1, String[] keywords2) {
        StringBuilder scoreBuilder = new StringBuilder();
        StringBuilder whereBuilder1 = new StringBuilder();
        StringBuilder whereBuilder2 = new StringBuilder();

        // Build scoring and WHERE clause for keywords1
        for (int k = 0; k < keywords1.length; k++) {
            if (k > 0) {
                scoreBuilder.append(" + ");
                whereBuilder1.append(" OR ");
            }
            scoreBuilder.append("""
            (CASE WHEN spare_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN replacement_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN standard_exchange_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN spare_description LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN comments LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN keywords LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END)
        """);
            whereBuilder1.append("""
            spare_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR replacement_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR standard_exchange_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR spare_description LIKE '%' || ? || '%' COLLATE NOCASE
            OR comments LIKE '%' || ? || '%' COLLATE NOCASE
            OR keywords LIKE '%' || ? || '%' COLLATE NOCASE
        """);
        }

        // Add separator between keywords1 and keywords2 scoring
        if (keywords1.length > 0 && keywords2.length > 0) {
            scoreBuilder.append(" + ");
        }

        // Build scoring and WHERE clause for keywords2
        for (int k = 0; k < keywords2.length; k++) {
            if (k > 0) {
                scoreBuilder.append(" + ");
                whereBuilder2.append(" OR ");
            }
            scoreBuilder.append("""
            (CASE WHEN spare_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN replacement_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN standard_exchange_item LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN spare_description LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN comments LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END) +
            (CASE WHEN keywords LIKE '%' || ? || '%' COLLATE NOCASE THEN 1 ELSE 0 END)
        """);
            whereBuilder2.append("""
            spare_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR replacement_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR standard_exchange_item LIKE '%' || ? || '%' COLLATE NOCASE
            OR spare_description LIKE '%' || ? || '%' COLLATE NOCASE
            OR comments LIKE '%' || ? || '%' COLLATE NOCASE
            OR keywords LIKE '%' || ? || '%' COLLATE NOCASE
        """);
        }

        // Combine WHERE clauses with AND
        String whereClause = String.format("(%s) AND (%s)",
                whereBuilder1.length() > 0 ? whereBuilder1.toString() : "1=1",
                whereBuilder2.length() > 0 ? whereBuilder2.toString() : "1=1");

        // Build the final SQL query
        String sql = String.format("""
        SELECT *, (%s) AS match_score
        FROM spares
        WHERE %s
        ORDER BY match_score DESC
    """, scoreBuilder.toString().replaceAll("\\+\\s*$", ""), whereClause);

        // Collect parameters
        List<Object> params = new ArrayList<>();
        for (String keyword : keywords1) {
            String normalizedKeyword = NoteTools.normalizeDate(keyword);
            String likeKeyword = "%" + normalizedKeyword + "%";
            // Scoring parameters (6 columns)
            for (int i = 0; i < 6; i++) {
                params.add(likeKeyword);
            }
            // WHERE parameters (6 columns)
            for (int i = 0; i < 6; i++) {
                params.add(likeKeyword);
            }
        }
        for (String keyword : keywords2) {
            String normalizedKeyword = NoteTools.normalizeDate(keyword);
            String likeKeyword = "%" + normalizedKeyword + "%";
            // Scoring parameters (6 columns)
            for (int i = 0; i < 6; i++) {
                params.add(likeKeyword);
            }
            // WHERE parameters (6 columns)
            for (int i = 0; i < 6; i++) {
                params.add(likeKeyword);
            }
        }

        // Execute query
        return jdbcTemplate.query(sql, ps -> {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i)); // JDBC indices are 1-based
            }
        }, new SparesRowMapper());
    }
}

