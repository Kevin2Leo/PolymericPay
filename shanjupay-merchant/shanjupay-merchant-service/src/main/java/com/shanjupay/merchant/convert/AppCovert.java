package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.entity.App;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;


import java.util.List;

/**
 * @Description:
 * @Date Created in 20:51 2022/6/26
 * @Author: Chen_zhuo
 * @Modified By
 */
@Mapper
public interface AppCovert {

    AppCovert INSTANCE = Mappers.getMapper(AppCovert.class);

    AppDTO entity2dto(App entity);
    App dto2entity(AppDTO dto);

    List<AppDTO> listentity2dto(List<App> app);
}
