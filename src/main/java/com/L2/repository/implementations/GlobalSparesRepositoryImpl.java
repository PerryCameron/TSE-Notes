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
import java.util.Arrays;
import java.util.Collections;
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

    /**
     * Searches the spares database for records matching the provided keywords, scoring matches based on keyword
     * occurrences across multiple columns and returning results ordered by relevance.
     * <p>
     * This method constructs a dynamic SQL query to search for keywords in the {@code spare_item},
     * {@code replacement_item}, {@code standard_exchange_item}, {@code spare_description}, {@code comments},
     * and {@code keywords} columns of the {@code spares} table. Each keyword match in these columns contributes
     * to a match score, calculated as the sum of binary indicators (1 for a match, 0 otherwise) per column per
     * keyword. The search is case-insensitive, and results are ordered by descending match score.
     * </p>
     * <p>
     * Keywords are normalized using {@link NoteTools#normalizeDate} to handle date formats, and the LIKE operator
     * with wildcards ({@code %keyword%}) is used for partial matches. The method uses prepared statements to safely
     * bind parameters and prevent SQL injection.
     * </p>
     *
     * @param keywords An array of keywords to search for in the spares database. Must not be null or empty.
     * @return A list of {@link SparesDTO} objects representing matching spares records, sorted by match score
     *         in descending order. Returns an empty list if no matches are found.
     * @throws org.springframework.jdbc.BadSqlGrammarException If the generated SQL query is invalid.
     * @throws // org.springframework.jdbc.JdbcSQLException If a database access error occurs.
     */
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
    """, scoreBuilder.toString().replaceAll("\\+\\s*$", ""), whereBuilder);
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

    /**
     * Searches the spares database for rows matching at least one range and one keyword.
     * - Ranges are matched against the 'pim' column using case-insensitive substring search
     *   (e.g., pim LIKE '%Galaxy VX%'). The pim column contains JSON data with 'range' and 'product_families' fields.
     * - Keywords are matched against fields (spare_item, replacement_item, standard_exchange_item, spare_description, comments, keywords)
     *   using case-insensitive substring search (e.g., spare_item LIKE '%0J-0360%').
     * - A match_score is calculated based on the number of keyword matches across the fields (1 point per match per field per keyword).
     * - Results are ordered by match_score in descending order, returning all matching rows without a limit.
     * - Debug logs include range match counts, combined range and keyword match counts, and sample matching rows with pim values.
     *
     * @param ranges Array of range strings to match against the pim column (e.g., ["Galaxy VX", "GVX"]). Leading/trailing spaces are trimmed.
     * @param keywords Array of keyword strings to match against the specified fields (e.g., ["0J-0360"]).
     * @return List of SparesDTO objects representing matching rows, sorted by match_score DESC.
     */
    @Override
    public List<SparesDTO> searchSparesWithRange(String[] ranges, String[] keywords) {
        // Trim ranges to remove leading/trailing spaces
        String[] cleanedRanges = Arrays.stream(ranges)
                .map(String::trim)
                .toArray(String[]::new);

        // Build WHERE clause for range filter (at least one match)
        StringBuilder rangeWhereBuilder = new StringBuilder("(");
        for (int k = 0; k < cleanedRanges.length; k++) {
            if (k > 0) {
                rangeWhereBuilder.append(" OR ");
            }
            String range = cleanedRanges[k].replace("'", "''");
            rangeWhereBuilder.append("pim LIKE '%").append(range).append("%' COLLATE NOCASE");
        }
        rangeWhereBuilder.append(")");

        // Count rows matching range condition (for debugging)
        String rangeCountSql = "SELECT COUNT(*) FROM spares WHERE " + rangeWhereBuilder;
        int rangeMatchCount;
        try {
            rangeMatchCount = jdbcTemplate.queryForObject(rangeCountSql, Integer.class);
            System.out.println("Range match count: " + rangeMatchCount + " rows for ranges: " + Arrays.toString(cleanedRanges));
        } catch (Exception e) {
            System.err.println("Range count query failed: " + e.getMessage());
        }

        // Build WHERE clause for keyword filter (at least one match)
        StringBuilder keywordWhereBuilder = new StringBuilder("(");
        for (int k = 0; k < keywords.length; k++) {
            if (k > 0) {
                keywordWhereBuilder.append(" OR ");
            }
            String keyword = keywords[k].replace("'", "''");
            keywordWhereBuilder.append("spare_item LIKE '%").append(keyword).append("%' COLLATE NOCASE OR ")
                    .append("replacement_item LIKE '%").append(keyword).append("%' COLLATE NOCASE OR ")
                    .append("standard_exchange_item LIKE '%").append(keyword).append("%' COLLATE NOCASE OR ")
                    .append("spare_description LIKE '%").append(keyword).append("%' COLLATE NOCASE OR ")
                    .append("comments LIKE '%").append(keyword).append("%' COLLATE NOCASE OR ")
                    .append("keywords LIKE '%").append(keyword).append("%' COLLATE NOCASE");
        }
        keywordWhereBuilder.append(")");

        // Debug: Count rows matching both range and keyword conditions
        String debugCountSql = "SELECT COUNT(*) FROM spares WHERE " + rangeWhereBuilder.toString() + " AND " + keywordWhereBuilder;
        int debugCount = 0;
        try {
            debugCount = jdbcTemplate.queryForObject(debugCountSql, Integer.class);
            System.out.println("Debug count: " + debugCount + " rows for ranges: " + Arrays.toString(cleanedRanges) + ", keywords: " + Arrays.toString(keywords));
        } catch (Exception e) {
            System.err.println("Debug count query failed: " + e.getMessage());
        }

        // Debug: Log sample matching rows with pim values
        if (debugCount > 0) {
            String debugRowsSql = "SELECT spare_description, pim, spare_item, replacement_item, standard_exchange_item, comments, keywords FROM spares WHERE " +
                    rangeWhereBuilder + " AND " + keywordWhereBuilder;
            try {
                jdbcTemplate.query(debugRowsSql, (rs, rowNum) -> {
                    System.out.println("Debug row - pim: " + rs.getString("pim") +
                                       ", spare_description: " + rs.getString("spare_description") +
                                       ", spare_item: " + rs.getString("spare_item") +
                                       ", replacement_item: " + rs.getString("replacement_item") +
                                       ", standard_exchange_item: " + rs.getString("standard_exchange_item") +
                                       ", comments: " + rs.getString("comments") +
                                       ", keywords: " + rs.getString("keywords"));
                    return null;
                });
            } catch (Exception e) {
                System.err.println("Debug rows query failed: " + e.getMessage());
            }
        }

        // Main query: Select all columns, score keyword matches, filter by range and keyword
        StringBuilder mainSql = new StringBuilder("SELECT *, (");
        for (int k = 0; k < keywords.length; k++) {
            if (k > 0) {
                mainSql.append(" + ");
            }
            String keyword = keywords[k].replace("'", "''");
            mainSql.append("(CASE WHEN spare_item LIKE '%").append(keyword).append("%' COLLATE NOCASE THEN 1 ELSE 0 END + ")
                    .append("CASE WHEN replacement_item LIKE '%").append(keyword).append("%' COLLATE NOCASE THEN 1 ELSE 0 END + ")
                    .append("CASE WHEN standard_exchange_item LIKE '%").append(keyword).append("%' COLLATE NOCASE THEN 1 ELSE 0 END + ")
                    .append("CASE WHEN spare_description LIKE '%").append(keyword).append("%' COLLATE NOCASE THEN 1 ELSE 0 END + ")
                    .append("CASE WHEN comments LIKE '%").append(keyword).append("%' COLLATE NOCASE THEN 1 ELSE 0 END + ")
                    .append("CASE WHEN keywords LIKE '%").append(keyword).append("%' COLLATE NOCASE THEN 1 ELSE 0 END)");
        }
        mainSql.append(") AS match_score FROM spares WHERE ");
        mainSql.append(rangeWhereBuilder);
        mainSql.append(" AND ");
        mainSql.append(keywordWhereBuilder);
        mainSql.append(" ORDER BY match_score DESC");

        // Log
        System.out.println("SQL: " + mainSql);
        System.out.println("Ranges: " + Arrays.toString(cleanedRanges));
        System.out.println("Keywords: " + Arrays.toString(keywords));

        // Execute main query
        try {
            List<SparesDTO> results = jdbcTemplate.query(mainSql.toString(), (rs, rowNum) -> {
                //                System.out.println("Matched row - pim: " + rs.getString("pim") +
//                                   ", spare_description: " + dto.getSpareDescription() +
//                                   ", match_score: " + rs.getInt("match_score") +
//                                   ", isArchived: " + dto.isArchived());
                return new SparesRowMapper().mapRow(rs, rowNum);
            });
            System.out.println("Results count: " + results.size());
            return results;
        } catch (Exception e) {
            System.err.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Searches the spares database for records where the part number matches the provided search term in either the
     * {@code spare_item} or {@code replacement_item} columns, returning distinct results grouped by {@code spare_item}.
     * <p>
     * This method constructs a SQL query to perform a case-sensitive partial match using the LIKE operator with wildcards
     * ({@code %searchTerm%}) on the {@code spare_item} and {@code replacement_item} columns of the {@code spares} table.
     * Results are grouped by {@code spare_item} to avoid duplicates. The {@code partOrderId} parameter is currently unused
     * but included for potential future filtering by part order. The query is executed using a prepared statement to prevent
     * SQL injection.
     * </p>
     *
     * @param searchTerm The search term to match against {@code spare_item} or {@code replacement_item}. Must not be null.
     * @param partOrderId The ID of the part order, currently unused but reserved for future functionality.
     * @return A list of {@link SparesDTO} objects representing matching spares records, grouped by {@code spare_item}.
     *         Returns an empty list if no matches are found or if a database error occurs.
     * @throws // org.springframework.jdbc.JdbcSQLException If a database access error occurs (logged and handled by returning an empty list).
     */
    @Override
    public List<SparesDTO> searchSparesByPartNumber(String searchTerm, int partOrderId) {
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

    /**
     * Counts the number of tuples in the spares database where the {@code pim} column matches any of the provided
     * range keywords, with a special case for the keyword "all" to return the total count of all tuples.
     * <p>
     * This method constructs a dynamic SQL query to perform a case-insensitive partial match using the LIKE operator
     * with wildcards ({@code %keyword%}) on the {@code pim} column of the {@code spares} table for each range keyword.
     * If the keyword "all" is included in the {@code ranges} array, the method returns the total count of tuples in the
     * {@code spares} table, ignoring other keywords. Otherwise, it sums the distinct counts of tuples matching each
     * range keyword to avoid double-counting overlapping matches. The query uses prepared statements to prevent SQL
     * injection.
     * </p>
     *
     * @param ranges An array of range keywords to search for in the {@code pim} column. Must not be null or empty.
     *               The keyword "all" triggers a count of all tuples in the {@code spares} table.
     * @return An integer representing the total count of matching tuples. If "all" is present, returns the count of all
     *         tuples in the {@code spares} table. Otherwise, returns the sum of distinct tuples matching each range keyword.
     *         Returns 0 if no matches are found, the input is invalid, or a database error occurs.
     * @throws // org.springframework.jdbc.JdbcSQLException If a database access error occurs (logged and handled by returning 0).
     */
    @Override
    public int countSparesByRanges(String[] ranges) {
        try {
            // Validate input
            if (ranges == null || ranges.length == 0) {
                logger.warn("Invalid input: ranges array is null or empty");
                return 0;
            }

            // Check for "all" keyword
            for (String range : ranges) {
                if ("all".equalsIgnoreCase(range)) {
                    String query = "SELECT COUNT(*) FROM spares";
                    logger.info("Counting all tuples in spares table...");
                    return jdbcTemplate.queryForObject(query, Integer.class);
                }
            }

            // Build dynamic query for range keywords
            StringBuilder whereBuilder = new StringBuilder();
            List<Object> params = new ArrayList<>();
            for (int i = 0; i < ranges.length; i++) {
                if (i > 0) {
                    whereBuilder.append(" OR ");
                }
                whereBuilder.append("pim LIKE ? COLLATE NOCASE");
                String searchPattern = "%" + ranges[i] + "%";
                params.add(searchPattern);
            }

            String query = String.format("""
            SELECT COUNT(DISTINCT spare_item) AS range_count
            FROM spares
            WHERE %s
        """, whereBuilder);

            logger.info("Counting spares for ranges: {}", Arrays.toString(ranges));
            Integer result = jdbcTemplate.queryForObject(query, Integer.class, params.toArray());
            return result != null ? result : 0;

        } catch (Exception e) {
            logger.error("Database error while counting spares by ranges: {}", e.getMessage());
            return 0; // Return 0 on error
        }
    }
//    C:\Users\sesa91827\IdeaProjects\TSE-Notes\src\main\java\com\L2\repository\implementations\GlobalSparesRepositoryImpl.java:540: warning: [deprecation] <T>queryForObject(String,Object[],Class<T>) in JdbcTemplate has been deprecated
//    Integer result = jdbcTemplate.queryForObject(query, params.toArray(), Integer.class);
//                                         ^
//    where T is a type-variable:
//    T extends Object declared in method <T>queryForObject(String,Object[],Class<T>)
//1 warning

}
