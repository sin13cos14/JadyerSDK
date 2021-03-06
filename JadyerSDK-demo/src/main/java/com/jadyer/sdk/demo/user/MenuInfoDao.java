package com.jadyer.sdk.demo.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jadyer.sdk.demo.user.model.MenuInfo;

public interface MenuInfoDao extends JpaRepository<MenuInfo, Integer> {
	@Query("FROM MenuInfo WHERE uid=?1")
	List<MenuInfo> findMenuListByUID(int uid);
}