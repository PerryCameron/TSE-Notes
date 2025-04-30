package com.L2.repository.implementations;

import com.L2.dto.global_spares.ProductToSparesDTO;
import com.L2.repository.interfaces.GlobalSparesRepository;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;

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


}
