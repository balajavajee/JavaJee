/*package com.bala.accesscontrol.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bala.accesscontrol.model.BaseEntity;
import com.bala.accesscontrol.model.Role;
import com.bala.accesscontrol.model.User;
import com.bala.accesscontrol.model.UserRole;
import com.bala.accesscontrol.repository.BaseRepository;

@Repository
public interface UserRoleRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> extends BaseRepository<UserRole, Long> {

	List<UserRole> findByuser(User user);

	List<UserRole> findByRole(Role role);

}
*/