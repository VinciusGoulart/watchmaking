package com.example.watchmaking.repository;

import com.example.watchmaking.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"userType"})
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsUserByPerson_Cpf(String cpf);

    @Modifying
    @Query("UPDATE User u SET u.isDeleted = true WHERE u.email = :email")
    void softDeleteByEmail(@Param("email") String email);

    @Query("""
            SELECT u FROM User u
            JOIN u.userType ut
            WHERE u.uuid = :technicianUuid
              AND ut.code = '102'
            """)
    Optional<User> findTechnicalByUuid(@Param("technicianUuid") UUID technicianUuid);

    @EntityGraph(attributePaths = {"userType"})
    Optional<User> findByEmailAndIsDeletedFalse(String email);
}
