package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RowMapper<User> ROW_MAPPER_WITH_ROLES = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setId(resultSet.getInt(1));
            user.setName(resultSet.getString(2));
            user.setEmail(resultSet.getString(3));
            user.setPassword(resultSet.getString(4));
            user.setRegistered(resultSet.getDate(5));
            user.setEnabled(resultSet.getBoolean(6));
            user.setCaloriesPerDay(resultSet.getInt(7));
            String roles = resultSet.getString(8);
            Set<Role> roleSet = new HashSet<>();
            if (roles.contains(" ")) {
                String[] rolesArray = roles.split(" ");
                Arrays.stream(rolesArray).forEach(role -> roleSet.add(
                        Role.valueOf(role)));
            } else {
                roleSet.add(Role.valueOf(roles));
            }
            user.setRoles(roleSet);
            return user;
        }
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        ValidatorFactory validationFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validationFactory.getValidator();
    }

    @Override
    @Transactional
    public User save(@NotNull User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
               UPDATE users SET name=:name, email=:email, password=:password, 
               registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
            """, parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(@Positive int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(@Positive int id) {
        List<User> users = jdbcTemplate.query("""
        SELECT u.id, name, email, password, registered, enabled, calories_per_day, ur.role 
        FROM users u LEFT JOIN user_roles ur ON ur.user_id = u.id WHERE u.id=?
        """, ROW_MAPPER_WITH_ROLES , id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(@Email String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("""
        SELECT u.id, name, email, password, registered, enabled, calories_per_day, string_agg(ur.role, ' ') 
        FROM users u LEFT JOIN user_roles ur ON ur.user_id = u.id WHERE u.email=? GROUP BY u.id
        """, ROW_MAPPER_WITH_ROLES, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("""
                SELECT u.id, name, email, password, registered, enabled, calories_per_day, string_agg(ur.role, ' ')  
                FROM users u LEFT JOIN user_roles ur ON ur.user_id = u.id GROUP BY u.id
                """, ROW_MAPPER_WITH_ROLES);
        return users.stream().sorted(Comparator.comparing(User::getName)
                .thenComparing(User::getEmail)).collect(Collectors.toList());
    }

//    public boolean createRole(int userId, Role newRole) {
////        return jdbcTemplate.update("""
////
////""");
//
//    }

    public boolean deleteRole(@Positive int userId, @NotNull Role role) {
        return jdbcTemplate.update("""
                DELETE FROM user_roles ur WHERE ur.user_id=? AND ur.role=?
        """, userId, role.toString()) != 0;
    }
}
