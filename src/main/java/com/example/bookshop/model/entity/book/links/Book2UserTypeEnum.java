package com.example.bookshop.model.entity.book.links;


public enum Book2UserTypeEnum {
    CART("В корзине"), KEPT("Отложена"), PAID("Куплена"), ARCHIVED("В архиве");

    private final String typeName;

    Book2UserTypeEnum(String typeName) {
        this.typeName = typeName;
    }

    public static String getCartName() {
        return Book2UserTypeEnum.CART.typeName;
    }
    public static String getKeptName() {
        return Book2UserTypeEnum.KEPT.typeName;
    }

    public static String getPaidName() {
        return Book2UserTypeEnum.PAID.typeName;
    }
    public static String getArchivedName() {
        return Book2UserTypeEnum.ARCHIVED.typeName;
    }
}
