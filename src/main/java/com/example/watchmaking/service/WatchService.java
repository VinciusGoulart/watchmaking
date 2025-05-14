package com.example.watchmaking.service;

import com.example.watchmaking.dto.watch.WatchCreateDto;
import com.example.watchmaking.dto.watch.WatchUpdateDto;
import com.example.watchmaking.dto.watch.WatchViewDto;
import com.example.watchmaking.entity.*;
import com.example.watchmaking.repository.WatchRepository;
import com.example.watchmaking.util.expcetions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.example.watchmaking.dto.watch.WatchViewDto.createListWatchViewDto;


@Service
public class WatchService {
    @Autowired
    private WatchRepository watchRepository;
    @Autowired
    private BrandService brandService;
    @Autowired
    private WatchCategoryService watchCategoryService;
    @Autowired
    private WatchTypeService watchTypeService;

    @Transactional
    public Watch createWatch(WatchCreateDto dto) {
        Brand brand = dto.getBrandUuid() != null ? brandService.findByUuid(dto.getBrandUuid()) : null;
        WatchType watchType = dto.getWatchTypeCode() != null ? watchTypeService.findWatchTypeByCode(dto.getWatchTypeCode()) : null;
        WatchCategory watchCategory = dto.getWatchCategoryCode() != null ? watchCategoryService.findWatchCategoryByCode(dto.getWatchCategoryCode()) : null;
        Watch watch = new Watch(dto, brand, watchType, watchCategory, null);
        return watchRepository.save(watch);
    }

    public Watch findByUuid(UUID uuid) {
        return watchRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Relógio não encontrado!"));
    }

    public Watch updateWatch(UUID brandUuid, UUID storageUuid, WatchUpdateDto dto) {
        if (!watchRepository.existsByUuid(brandUuid)) {
            throw new NotFoundException("Relógio não encontrado!");
        }
        dto.setImageUuid(storageUuid);
        Watch newWatch = new Watch(brandUuid, dto);
        return watchRepository.save(newWatch);
    }

    public Page<WatchViewDto> listWatch(Pageable pageable) {
        Page<Watch> watches = watchRepository.findAll(pageable);
        List<WatchViewDto> watchViewDto = createListWatchViewDto(watches);
        return new PageImpl<>(watchViewDto, pageable, watches.getTotalElements());
    }

}
