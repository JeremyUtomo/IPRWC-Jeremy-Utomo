package com.example.iprwcspringbootjeremy.Repository;

import com.example.iprwcspringbootjeremy.Model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

}
