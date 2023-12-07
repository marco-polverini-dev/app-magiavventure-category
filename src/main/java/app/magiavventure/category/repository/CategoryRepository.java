package app.magiavventure.category.repository;

import app.magiavventure.category.entity.ECategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends MongoRepository<ECategory, UUID> { }
