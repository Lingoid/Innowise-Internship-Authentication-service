package com.innowise.authservice.authservice.repository;

import com.innowise.authservice.authservice.model.AuthUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Optional;

@Repository
public class AuthUserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AuthUserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class SQL {
        private static final String INSERT_USER =
                "INSERT INTO auth_users (user_name, email, password) " +
                        "VALUES (:userName, :email, :password) RETURNING id";

        private static final String SELECT_BY_USERNAME =
                "SELECT * FROM auth_users WHERE user_name = :userName";

        private static final String DELETE_ALL =
                "DELETE FROM auth_users";
    }

    public Optional<AuthUser> findByUserName(String userName) {
        try {
            AuthUser user = jdbcTemplate.queryForObject(
                    SQL.SELECT_BY_USERNAME,
                    Collections.singletonMap("userName", userName),
                    new BeanPropertyRowMapper<>(AuthUser.class)
            );
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public AuthUser save(AuthUser user) {
        Long id = jdbcTemplate.queryForObject(
                SQL.INSERT_USER,
                new BeanPropertySqlParameterSource(user),
                Long.class
        );
        user.setId(id);
        return user;
    }

    public void deleteAll() {
        jdbcTemplate.update(SQL.DELETE_ALL, Collections.emptyMap());
    }
}
