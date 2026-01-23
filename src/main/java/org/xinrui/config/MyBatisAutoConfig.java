package org.xinrui.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.xinrui.core.secure.utils.AuthUtil;

import java.util.Date;

/**
 * 类
 *
 * @author jiangxianliang
 * @since 2021/4/27
 */
@Component
public class MyBatisAutoConfig implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, "createUser", Long.class, AuthUtil.getUserId());
		this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
		this.strictInsertFill(metaObject, "tenantOid", Long.class, AuthUtil.getTenantOid());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, "updateUser", Long.class,AuthUtil.getUserId());
		this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
	}

	public Interceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		return mybatisPlusInterceptor;
	}
}
