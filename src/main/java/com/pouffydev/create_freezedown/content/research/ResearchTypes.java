package com.pouffydev.create_freezedown.content.research;

import com.pouffydev.create_freezedown.Thermology;

public enum ResearchTypes {
    itemUse("item_use", "item"),
    itemSwing("item_swing", "item"),
    blockPlace("block_place", "item"),
    blockBreak("block_break", "block"),
    blockInteract("block_interact", "block"),
    entityHurt("entity_hurt", "entity"),
    entityInteract("entity_interact", "entity")
    ;
    
    private final String type;
    private final String category;
    
    ResearchTypes(String type, String category) {
        this.type = type;
        this.category = category;
    }
    
    public String getCategory() {
        return category;
    }
    public String getType() {
        return type;
    }
    public static ResearchTypes getResearchType(String type) {
        for (ResearchTypes researchType : ResearchTypes.values()) {
            if (researchType.getType().equals(type)) {
                return researchType;
            }
        }
        return null;
    }
    public static ResearchTypes getResearchTypeFromCategory(String category) {
        for (ResearchTypes researchType : ResearchTypes.values()) {
            if (researchType.getCategory().equals(category)) {
                return researchType;
            }
        }
        return null;
    }
    public String getTranslationKey() {
        return Thermology.ID + ".research." + type;
    }
}
