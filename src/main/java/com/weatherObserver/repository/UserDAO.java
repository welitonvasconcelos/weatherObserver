package com.weatherObserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.weatherObserver.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Long>{
	public User findByNome(String email);
}
