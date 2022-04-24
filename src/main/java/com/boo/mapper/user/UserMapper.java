package com.boo.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.entity.user.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author song
 * @Date 2022/4/10 15:23
 * @Description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert("replace into sys_avatar (user_id,avatar_bytes) " +
            "values( #{id},#{bytes} );")
    boolean saveOrUpdateAvatarBytes(Long id, byte[] bytes);
}
