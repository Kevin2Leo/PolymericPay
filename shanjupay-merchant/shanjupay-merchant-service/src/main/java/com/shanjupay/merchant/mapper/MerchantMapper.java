package com.shanjupay.merchant.mapper;

import com.shanjupay.merchant.entity.Merchant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2019-12-01
 */
@Repository
@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {

}
