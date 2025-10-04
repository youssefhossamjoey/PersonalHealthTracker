package com.example.PersonalHealthTracker.mappers.Impl;

import com.example.PersonalHealthTracker.domain.dto.ItemDto;
import com.example.PersonalHealthTracker.domain.entities.ItemEntity;
import com.example.PersonalHealthTracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper implements Mapper<ItemEntity, ItemDto> {
    private ModelMapper modelMapper;


    public ItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ItemDto mapTo(ItemEntity itemEntity) {
        return modelMapper.map(itemEntity, ItemDto.class);
    }

    @Override
    public ItemEntity mapFrom(ItemDto itemDto) {
        return modelMapper.map(itemDto, ItemEntity.class);
    }
}
