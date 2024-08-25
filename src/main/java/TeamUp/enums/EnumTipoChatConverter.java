package TeamUp.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EnumTipoChatConverter implements AttributeConverter<EnumTipoChat, String> {

    @Override
    public String convertToDatabaseColumn(EnumTipoChat attribute) {
        if (attribute == null) {
            return null;
        }
        return String.valueOf(attribute.getValue());
    }

    @Override
    public EnumTipoChat convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return EnumTipoChat.fromValue(Integer.parseInt(dbData));
    }
}
