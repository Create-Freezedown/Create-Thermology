package com.pouffydev.create_freezedown.content.fluids.boiler.boiler_tank;

import com.pouffydev.create_freezedown.content.fluids.boiler.bronze_fluid_tank.BronzeTankBlockEntity;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoilerData {
    static final int SAMPLE_RATE = 5;
    
    int gatheredSupply;
    float[] supplyOverTime = new float[10];
    int ticksUntilNextSample;
    int currentIndex;
    
    // heat score
    
    public int attachedWhistles;
    public boolean bronzeTankBelow;
    // display
    private int minValue = 0;
    private int maxValue = 0;
    
    public LerpedFloat gauge = LerpedFloat.linear();
    
    public void tick(BoilerTankBlockEntity controller) {
        if (!isActive())
            return;
        if (controller.getLevel().isClientSide) {
            gauge.tickChaser();
            float current = gauge.getValue(1);
            if (current > 1 && Create.RANDOM.nextFloat() < 1 / 2f)
                gauge.setValueNoUpdate(current + Math.min(-(current - 1) * Create.RANDOM.nextFloat(), 0));
            return;
        }
        ticksUntilNextSample--;
        if (ticksUntilNextSample > 0)
            return;
        int capacity = controller.tankInventory.getCapacity();
        if (capacity == 0)
            return;
        
        ticksUntilNextSample = SAMPLE_RATE;
        supplyOverTime[currentIndex] = gatheredSupply / (float) SAMPLE_RATE;
        currentIndex = (currentIndex + 1) % supplyOverTime.length;
        gatheredSupply = 0;
        
        bronzeTankBelow = controller.getLevel().getBlockEntity(controller.getBlockPos().below()) instanceof BronzeTankBlockEntity;
        
        controller.notifyUpdate();
    }
    
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, int boilerSize) {
        if (!isActive())
            return false;
        
        Component indent = Components.literal(IHaveGoggleInformation.spacing);
        Component indent2 = Components.literal(IHaveGoggleInformation.spacing + " ");
        
        tooltip.add(Components.immutableEmpty());
        
        Lang.translate("tooltip.capacityProvided")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        
        return false;
    }
    
    private MutableComponent componentHelper(String label, int level, boolean forGoggles, boolean useBlocksAsBars,
                                             ChatFormatting... styles) {
        MutableComponent base = useBlocksAsBars ? blockComponent(level) : barComponent(level);
        
        if (!forGoggles)
            return base;
        
        ChatFormatting style1 = styles.length >= 1 ? styles[0] : ChatFormatting.GRAY;
        ChatFormatting style2 = styles.length >= 2 ? styles[1] : ChatFormatting.DARK_GRAY;
        
        return Lang.translateDirect("boiler." + label)
                .withStyle(style1)
                .append(Lang.translateDirect("boiler." + label + "_dots")
                        .withStyle(style2))
                .append(base);
    }
    
    private MutableComponent blockComponent(int level) {
        return Components.literal(
                "" + "\u2588".repeat(minValue) + "\u2592".repeat(level - minValue) + "\u2591".repeat(maxValue - level));
    }
    
    private MutableComponent barComponent(int level) {
        return Components.empty()
                .append(bars(Math.max(0, minValue - 1), ChatFormatting.DARK_GREEN))
                .append(bars(minValue > 0 ? 1 : 0, ChatFormatting.GREEN))
                .append(bars(Math.max(0, level - minValue), ChatFormatting.DARK_GREEN))
                .append(bars(Math.max(0, maxValue - level), ChatFormatting.DARK_RED))
                .append(bars(Math.max(0, Math.min(18 - maxValue, ((maxValue / 5 + 1) * 5) - maxValue)),
                        ChatFormatting.DARK_GRAY));
        
    }
    
    private MutableComponent bars(int level, ChatFormatting format) {
        return Components.literal(Strings.repeat('|', level))
                .withStyle(format);
    }
    
    public boolean evaluate(BronzeTankBlockEntity controller) {
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        int prevWhistles = attachedWhistles;
        attachedWhistles = 0;
        
        return prevWhistles != attachedWhistles;
    }
    
    public void checkPipeOrganAdvancement(BronzeTankBlockEntity controller) {
        if (!controller.getBehaviour(AdvancementBehaviour.TYPE)
                .isOwnerPresent())
            return;
        
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        Set<Integer> whistlePitches = new HashSet<>();
        
        if (whistlePitches.size() >= 12)
            controller.award(AllAdvancements.PIPE_ORGAN);
    }
    
    public boolean updateTemperature(BronzeTankBlockEntity controller) {
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        
        
        
        return false;
    }
    
    public boolean isActive() {
        return attachedWhistles > 0 || bronzeTankBelow;
    }
    
    public void clear() {
        Arrays.fill(supplyOverTime, 0);
    }
    
    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("Whistles", attachedWhistles);
        nbt.putBoolean("BronzeTankBelow", bronzeTankBelow);
        return nbt;
    }
    
    public void read(CompoundTag nbt, int boilerSize) {
        attachedWhistles = nbt.getInt("Whistles");
        bronzeTankBelow = nbt.getBoolean("BronzeTankBelow");
    }
    
    public BoilerFluidHandler createHandler() {
        return new BoilerFluidHandler();
    }
    
    public class BoilerFluidHandler implements IFluidHandler {
        
        @Override
        public int getTanks() {
            return 1;
        }
        
        @Override
        public FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }
        
        @Override
        public int getTankCapacity(int tank) {
            return 10000;
        }
        
        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return FluidHelper.isTag(stack.getFluid(), AllTags.forgeFluidTag("steam"));
        }
        
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!isFluidValid(0, resource))
                return 0;
            int amount = resource.getAmount();
            if (action.execute())
                gatheredSupply += amount;
            return amount;
        }
        
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }
        
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }
        
    }
    
}
