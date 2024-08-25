package TeamUp.enums;

public enum EnumTipoChat {
	GROUP(0),
    CHAT(1),
    POST(2);

    private final int value;

    EnumTipoChat(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static EnumTipoChat fromValue(int value) {
        switch (value) {
            case 0: return GROUP;
            case 1: return CHAT;
            case 2: return POST;
            default: throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    @Override
    public String toString() {
        return this.name() + "(" + value + ")";
    }
}
