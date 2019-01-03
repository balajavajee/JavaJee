package com.bala.accesscontrol.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bala.accesscontrol.model.BaseEntity;
import com.bala.accesscontrol.model.UserSetting;
import com.bala.accesscontrol.repository.BaseRepository;

@Repository
public interface UserSettingsRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID>, PagingAndSortingRepository<T, ID> /*extends BaseRepository<UserSetting, Long>*/ {
	Optional<UserSetting> findByProperty(String property);

}
