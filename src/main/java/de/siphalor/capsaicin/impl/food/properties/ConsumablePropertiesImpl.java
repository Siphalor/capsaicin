package de.siphalor.capsaicin.impl.food.properties;

import de.siphalor.capsaicin.api.food.ConsumableProperties;
import de.siphalor.capsaicin.impl.util.MutationDetectingList;
import lombok.Getter;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.ArrayList;
import java.util.List;

//# if MC_VERSION_NUMBER >= 12102
@Getter
public class ConsumablePropertiesImpl implements ConsumableProperties {
	private boolean changed;
	private float consumeSeconds;
	private List<ConsumeEffect> onConsumeEffects;

	public static ConsumablePropertiesImpl from(Consumable consumable) {
		return new ConsumablePropertiesImpl(consumable.consumeSeconds(), consumable.onConsumeEffects());
	}

	public static ConsumablePropertiesImpl copy(ConsumableProperties properties) {
		return new ConsumablePropertiesImpl(properties.getConsumeSeconds(), properties.getOnConsumeEffects());
	}

	public ConsumablePropertiesImpl(float consumeSeconds, List<ConsumeEffect> onConsumeEffects) {
		this.consumeSeconds = consumeSeconds;
		this.onConsumeEffects = new MutationDetectingList<>(new ArrayList<>(onConsumeEffects), () -> changed = true);
	}

	@Override
	public void setConsumeSeconds(float consumeSeconds) {
		if (this.consumeSeconds != consumeSeconds) {
			this.consumeSeconds = consumeSeconds;
			this.changed = true;
		}
	}

	public void setOnConsumeEffects(List<ConsumeEffect> onConsumeEffects) {
		this.onConsumeEffects = onConsumeEffects;
		this.changed = true;
	}
}
//# end
