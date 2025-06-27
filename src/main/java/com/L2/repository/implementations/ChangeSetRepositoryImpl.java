package com.L2.repository.implementations;

import com.L2.dto.global_spares.SparesDTO;
import com.L2.repository.interfaces.ChangeSetRepository;
import com.L2.static_tools.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ChangeSetRepositoryImpl implements ChangeSetRepository {
    private static final Logger logger = LoggerFactory.getLogger(GlobalSparesRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public ChangeSetRepositoryImpl() {
        this.jdbcTemplate = new JdbcTemplate(DatabaseConnector.getChangeSetDataSource("Change Set Repo"));
    }

    @Override
    public int insertSpare(SparesDTO sparesDTO) {
        try {
            String sql = "INSERT INTO spares (" +
                    "pim, spare_item, replacement_item, standard_exchange_item, " +
                    "spare_description, catalogue_version, end_of_service_date, " +
                    "last_update, added_to_catalogue, removed_from_catalogue, " +
                    "comments, keywords, archived, custom_add, last_updated_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            int rowsAffected = jdbcTemplate.update(sql,
                    sparesDTO.getPim(),
                    sparesDTO.getSpareItem(),
                    sparesDTO.getReplacementItem(),
                    sparesDTO.getStandardExchangeItem(),
                    sparesDTO.getSpareDescription(),
                    sparesDTO.getCatalogueVersion(),
                    sparesDTO.getProductEndOfServiceDate(),
                    sparesDTO.getLastUpdate(),
                    sparesDTO.getAddedToCatalogue(),
                    sparesDTO.getRemovedFromCatalogue(),
                    sparesDTO.getComments(),
                    sparesDTO.getKeywords(),
                    sparesDTO.getArchived() ? 1 : 0,
                    sparesDTO.getCustomAdd() ? 1 : 0,
                    sparesDTO.getLastUpdatedBy()
            );

            if (rowsAffected == 1) {
                return 1;
            } else {
                logger.warn("Unexpected number of rows affected: {}", rowsAffected);
                return 0;
            }
        } catch (Exception e) {
            logger.error("Error inserting spare: {}", e.getMessage());
            return 0;
        }
    }

}
