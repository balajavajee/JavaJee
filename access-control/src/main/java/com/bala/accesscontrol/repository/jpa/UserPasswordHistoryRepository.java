/*package com.bala.accesscontrol.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bala.accesscontrol.model.BaseEntity;
import com.bala.accesscontrol.model.UserPasswordHistory;
import com.bala.accesscontrol.repository.BaseRepository;

@Repository
public interface UserPasswordHistoryRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> extends BaseRepository<UserPasswordHistory, Long> {

   List<UserPasswordHistory> findByuserId(Integer userId);

}
*/