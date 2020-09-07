package com.example.managementsystem.clientmanagement.activity.tagname;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TagNameRepository extends JpaRepository<TagNameEntity, UUID> {

    //    @Query("SELECT message FROM #{#entityName} entity WHERE entity.key=:key AND entity.locale LIKE %:locale% AND entity.activity.id=:activityId")
    StringBuilder findByKeyAndLocaleAndActivity_Id(@Param("key") String key, @Param("locale") String locale, @Param("activityId") UUID activityId);

    Set<TagNameEntity> findAllByActivity_Id(UUID activityId);
}
