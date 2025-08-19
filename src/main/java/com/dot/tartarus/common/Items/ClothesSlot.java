package com.dot.tartarus.common.Items;

public enum ClothesSlot {
    HEAD(0),SHIRT(1),PANTS(2),SHOES(3),BACK(4),JACKET(5),GLOVES(6),PANTIES(7),BELT(8);

    private int id;

    ClothesSlot(int id) {
        this.id = id;
    }

    public int getID(){
        return id;
    }

    public static ClothesSlot getFromID(int id){
        switch (id){
            default:
            case 0:
                return HEAD;
            case 1:
                return SHIRT;
            case 2:
                return PANTS;
            case 3:
                return SHOES;
            case 4:
                return BACK;
            case 5:
                return JACKET;
            case 6:
                return GLOVES;
            case 7:
                return PANTIES;
            case 8:
                return BELT;
        }
    }
}
