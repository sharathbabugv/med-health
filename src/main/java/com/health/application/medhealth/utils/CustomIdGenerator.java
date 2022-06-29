package com.health.application.medhealth.utils;

import com.health.application.medhealth.dto.Patient;
import com.health.application.medhealth.exceptions.UnableToProcessException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        boolean isPatient = object instanceof Patient;
        String prefix = isPatient ? "PAT" : "DOC";
        String table = isPatient ? "patient" : "doctor";

        Connection connection = session.connection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select count(id) as Id from " + table);
            if (rs.next()) {
                int id = rs.getInt(1) + 101;
                return prefix + id;
            }
        } catch (SQLException e) {
            throw new UnableToProcessException(e.getMessage());
        }
        return null;
    }
}
