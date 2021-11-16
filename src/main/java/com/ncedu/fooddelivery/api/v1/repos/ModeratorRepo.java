package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import org.springframework.data.repository.CrudRepository;

public interface ModeratorRepo extends CrudRepository<Moderator, Long> {
}
