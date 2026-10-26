package com.eventProApi.eventpro.repository;

import com.eventProApi.eventpro.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AdminRepository
        extends JpaRepository<Admin, UUID>  {
}