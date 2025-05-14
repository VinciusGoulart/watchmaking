package com.example.watchmaking.dto.watch;

import com.example.watchmaking.entity.Watch;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class WatchViewDto {
    private UUID uuid;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String reference;
    private String mechanism;
    private String gender;
    private String material;
    private String waterResistance;
    private String dialColor;
    private String strapMaterial;
    private UUID brandUuid;
    private UUID watchTypeUuid;
    private UUID watchCategoryUuid;
    private UUID imageUuid;

    public static List<WatchViewDto> createListWatchViewDto(Page<Watch> watches) {
        List<WatchViewDto> watchViewDtos = new ArrayList<WatchViewDto>();
        for(Watch watch : watches) {
            WatchViewDto dto = new WatchViewDto();
            dto.setUuid(watch.getUuid());
            dto.setName(watch.getName());
            dto.setDescription(watch.getDescription());
            dto.setPrice(watch.getPrice());
            dto.setQuantity(watch.getQuantity());
            dto.setReference(watch.getReference());
            dto.setMechanism(watch.getMechanism());
            dto.setGender(watch.getGender());
            dto.setMaterial(watch.getMaterial());
            dto.setWaterResistance(watch.getWaterResistance());
            dto.setDialColor(watch.getDialColor());
            dto.setStrapMaterial(watch.getStrapMaterial());
            dto.setBrandUuid(watch.getBrand() != null ? watch.getBrand().getUuid() : null);
            dto.setWatchTypeUuid(watch.getWatchType() != null ? watch.getWatchType().getUuid() : null);
            dto.setWatchCategoryUuid(watch.getWatchCategory() != null ? watch.getWatchCategory().getUuid() : null);
            dto.setImageUuid(watch.getImage() != null ? watch.getImage().getUuid() : null);
            watchViewDtos.add(dto);
        }
        return watchViewDtos;
    }
}
