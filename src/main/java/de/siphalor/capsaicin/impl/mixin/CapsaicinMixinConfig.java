package de.siphalor.capsaicin.impl.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused") // declared in mixin config json
@ApiStatus.Internal
public class CapsaicinMixinConfig implements IMixinConfigPlugin {
	private final boolean appleskinLoaded = FabricLoader.getInstance().isModLoaded("appleskin");
	private final boolean polymerLoaded = FabricLoader.getInstance().isModLoaded("polymer");
	private String ItemStack$getMaxUseTime$remapped;
	private String EatingTimeHandler$getEatingTime$desc;

	@Override
	public void onLoad(String mixinPackage) {
		MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		String ItemStack$remapped = mappingResolver.mapClassName("intermediary", "net.minecraft.class_1799");
		ItemStack$getMaxUseTime$remapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1799", "method_7935", "()I");
		EatingTimeHandler$getEatingTime$desc = "(L" + ItemStack$remapped.replace('.', '/') + ";I)I";
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.startsWith("de.siphalor.capsaicin.impl.mixin.client.appleskin")) {
			return appleskinLoaded;
		} else if (mixinClassName.startsWith("de.siphalor.capsaicin.impl.mixin.client.polymer")) {
			return polymerLoaded;
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("de.siphalor.capsaicin.impl.mixin.MixinItemStack".equals(mixinClassName)) {
			for (MethodNode method : targetClass.methods) {
				if (ItemStack$getMaxUseTime$remapped.equals(method.name)) {
					targetClass.methods.remove(method);
					method.accept(new GetMaxUseTimeTransformer(
							targetClass.visitMethod(method.access, method.name, method.desc, method.signature, method.exceptions.toArray(new String[0])),
							method.access, method.name, method.desc
					));
					break;
				}
			}
		}
	}

	private class GetMaxUseTimeTransformer extends GeneratorAdapter {
		public GetMaxUseTimeTransformer(MethodVisitor methodVisitor, int access, String name, String descriptor) {
			super(Opcodes.ASM9, methodVisitor, access, name, descriptor);
		}

		@Override
		public void visitInsn(int opcode) {
			if (opcode == Opcodes.IRETURN) {
				super.loadThis();
				super.swap();
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "de/siphalor/capsaicin/impl/food/eatingtime/EatingTimeHandler", "getEatingTime", EatingTimeHandler$getEatingTime$desc, false);
			}
			super.visitInsn(opcode);
		}
	}
}
