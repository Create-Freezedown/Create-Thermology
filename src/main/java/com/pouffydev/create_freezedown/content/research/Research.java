package com.pouffydev.create_freezedown.content.research;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Set;

public class Research {
    @Nullable
    private final Research parent;
    private final ResourceLocation id;
    private final Set<Research> children = Sets.newLinkedHashSet();
    private final ResearchTypes type;
    
    public Research(ResourceLocation id, @Nullable Research parent, ResearchTypes type) {
        this.id = id;
        this.parent = parent;
        this.type = type;
    }
    
    public ResearchTypes getType() {
        return type;
    }
    
    public Research getParent() {
        return parent;
    }
    
    public void addChild(Research child) {
        this.children.add(child);
    }
    
    public ResourceLocation getId() {
        return id;
    }
    
    public Set<Research> getChildren() {
        return children;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Research)) {
            return false;
        } else {
            Research research = (Research) o;
            return this.id.equals(research.id);
        }
    }
}
