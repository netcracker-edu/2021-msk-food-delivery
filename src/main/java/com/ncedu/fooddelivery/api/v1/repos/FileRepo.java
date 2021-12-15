package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.File;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FileRepo extends CrudRepository<File, UUID> {

}
