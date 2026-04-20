package com.gym.gym_backend.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter(autoApply = false)
public class LongUuidConverter implements AttributeConverter<Long, UUID> {

    @Override
    public UUID convertToDatabaseColumn(Long attribute) {
        if (attribute == null) {
            return null;
        }
        // keep high bits zero, low bits equals value; preserves round-trip for positive longs
        return new UUID(0L, attribute);
    }

    @Override
    public Long convertToEntityAttribute(UUID dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.getLeastSignificantBits();
    }
}
