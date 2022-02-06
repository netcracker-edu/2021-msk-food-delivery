package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByRole(Role role);
    Page<User> findAll(Pageable pageable);
    @Query(value = "SELECT u.* , ts_rank_cd(us.search_vector, query, 1) AS rank" +
            " FROM to_tsquery('russian', ?1) AS query, users AS u" +
            " JOIN users_search AS us" +
            " ON u.user_id = us.user_search_id" +
            " WHERE query @@ us.search_vector" +
            " ORDER BY rank DESC",
            countQuery = "SELECT count(*)" +
                    " FROM users_search AS us, to_tsquery('russian', ?1) AS query" +
                    " WHERE query @@ us.search_vector",
            nativeQuery = true)
    Page<User> searchUsers(String search, Pageable pageable);
}
