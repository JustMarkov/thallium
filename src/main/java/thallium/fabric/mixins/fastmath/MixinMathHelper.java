package thallium.fabric.mixins.fastmath;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.MathHelper;
import thallium.fabric.math.MathUtils;
import thallium.fabric.gui.ThalliumOptions;

@Mixin(MathHelper.class)
public class MixinMathHelper {

    @Shadow
    @Final
    private static float[] SINE_TABLE;

    @Overwrite
    public static float sin(float value) {
        return ThalliumOptions.useFastMath ? MathUtils.fastSin(value) : SINE_TABLE[(int)(value * 10430.378F) & 65535];
    }

    @Overwrite
    public static float cos(float value) {
        return ThalliumOptions.useFastMath ? MathUtils.fastCos(value)  : SINE_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
    }

}